package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseGeneticAlgorithm implements IGA, SelectionAlgorithm{

    private final Float crossoverRate;
    private final Float elitismRate;
    private final Float selectionRate;
    private final Float mutationRate;
    private final Function fitnessFunction;
    private String selectionAlgorithm;
    private final ArrayList<ArrayList<Chromosome>> populations;
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

    @Override
    public ArrayList<ArrayList<Chromosome>> getAllPopulations() {
        return populations;
    }

    @Override
    public Function fitnessFunction() {
        return this.fitnessFunction;
    }

    @Override
    public Float getMutationRate() {
        return mutationRate;
    }

    @Override
    public Float getSelectionRate() {
        return this.selectionRate;
    }

    @Override
    public Float getElitismRate() {
        return elitismRate;
    }

    @Override
    public Float getCrossoverRate() {
        return crossoverRate;
    }

    @Override
    public Integer getPopulationSize() {
        return populationSize;
    }

    @Override
    public Integer getChromosomeSize() {
        return chromosomeSize;
    }

    @Override
    public Integer getMaxGenerations() {
        return maxGenerations;
    }

    @Override
    public String getSelectionAlgorithm() {
        return this.selectionAlgorithm;
    }

    @Override
    public ArrayList<Chromosome> getPopulation() {
        if(!populations.isEmpty()){
            return populations.get(populations.size()-1);
        }
        return null;
    }

    @Override
    public Integer getGeneration() {
        return generation;
    }

    public void next(boolean verbose){
        populations.add(new ArrayList<>());
        if(generation == 0){
            generateInitialPopulation();
        }
        System.out.printf("GENERATION %d: %n", generation);
        evaluateChromosomes(verbose);
        generation++;
    }

    public void run(boolean verbose){
        while (generation < maxGenerations){
            next(verbose);
        }
    }

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        if (Arrays.stream(optionsSelectionAlgorithms).anyMatch(Predicate.isEqual(selectionAlgorithm))) {
            this.selectionAlgorithm = selectionAlgorithm;
        }
    }
}
