package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseGeneticAlgorithm implements IGA, SelectionAlgorithm{

    private final Float crossoverRate;
    private final Float elitismRate;
    private final Float selectionRate;
    private final Float mutationRate;
    private final Function fitnessFunction;
    private String selectionAlgorithm;
    private ArrayList<ArrayList<Chromosome>> populations;
    private final Integer populationSize;
    private final Integer chromosomeSize;
    private final Integer maxGenerations;
    private Integer generation;


    public BaseGeneticAlgorithm(Integer populationSize,
                                Integer chromosomeSize,
                                Integer maxGenerations,
                                Float mutationRate,
                                Float selectionRate,
                                Float elitismRate,
                                Float crossoverRate,
                                Function fitnessFunction,
                                String selectionAlgorithm)
    {
        setSelectionAlgorithm(selectionAlgorithm);
        this.chromosomeSize = chromosomeSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismRate = elitismRate;
        this.selectionRate = selectionRate;
        this.fitnessFunction = fitnessFunction;
        this.populationSize = populationSize;

        populations = new ArrayList<>();
        this.generation = 0;

    }

    public void next(boolean verbose){
        populations.add(new ArrayList<>());
        if(generation == 0){
            populations.get(generation).addAll(Chromosome.generatePopulation(populationSize, chromosomeSize, fitnessFunction));
        }
        populations.get(generation).stream().parallel().forEach(Chromosome::computeFitness);
        generation++;
    }

    public void run(boolean verbose){
        while (generation < maxGenerations){
            next(verbose);
            if(verbose){
                System.out.println("Generation: " + generation);
            }
        }
    }

    @Override
    public ArrayList<Chromosome> getPopulation() {
        if(!populations.isEmpty()){
            return populations.get(populations.size()-1);
        }
        return null;
    }

    @Override
    public ArrayList<ArrayList<Chromosome>> getAllPopulations() {
        return populations;
    }

    @Override
    public List<Integer> populationInfo() { // PopulationSize, ChromosomeSize, MaxGenerations
        return Arrays.asList(this.populationSize, this.chromosomeSize, this.maxGenerations);
    }

    @Override
    public List<Float> rates() {
        return Arrays.asList(this.mutationRate, this.crossoverRate, this.elitismRate, this.selectionRate);
    }

    @Override
    public Function fitnessFunction() {
        return this.fitnessFunction;
    }

    @Override
    public Float getSelectionRate() {
        return this.selectionRate;
    }

    @Override
    public String getSelectionAlgorithm() {
        return this.selectionAlgorithm;
    }

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        if (Arrays.stream(optionsSelectionAlgorithms).anyMatch(Predicate.isEqual(selectionAlgorithm))) {
            this.selectionAlgorithm = selectionAlgorithm;
        }
    }

    @Override
    public Integer getGeneration() {
        return generation;
    }
}
