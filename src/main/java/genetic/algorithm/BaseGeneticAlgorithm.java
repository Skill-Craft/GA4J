package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseGeneticAlgorithm implements IGA {

    private final Function<Chromosome, Float> fitnessFunction;
    private final Float elitismRate;
    private List<Boolean> uniformCrossoverMask;
    private String selectionAlgorithm;

    private String crossoverAlgorithm = "one-point";
    private final Float crossoverRate;
    private final Float mutationRate;
    private final Float probabilityOfMutation;
    private final List<List<Chromosome>> populations;
    private final Integer populationSize;
    private final Integer chromosomeSize;
    private final Integer maxGenerations;
    private Integer generation;


    public BaseGeneticAlgorithm(Integer populationSize,
                                Integer chromosomeSize,
                                Integer maxGenerations,
                                Float elitismRate,
                                Float mutationRate,
                                Float probabilityMutation,
                                Float crossoverRate,
                                Function<Chromosome, Float> fitnessFunction,
                                String selectionAlgorithm)
    {
        setSelectionAlgorithm(selectionAlgorithm);
        setCrossoverAlgorithm("one-point");
        this.chromosomeSize = chromosomeSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismRate = elitismRate;
        this.fitnessFunction = fitnessFunction;
        this.populationSize = populationSize;
        this.probabilityOfMutation = probabilityMutation;
        this.crossoverAlgorithm = "one-point";
        this.uniformCrossoverMask = new ArrayList<>();

        populations = new ArrayList<>();
        this.generation = 0;

    }

    @Override
    public List<List<Chromosome>> getAllPopulations() {
        return populations;
    }

    @Override
    public Function<Chromosome, Float> fitnessFunction() {
        return this.fitnessFunction;
    }

    @Override
    public Float getMutationRate() {
        return mutationRate;
    }

    @Override
    public Float getProbabilityOfMutation() {
        return probabilityOfMutation;
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
    public Integer getGeneration() {
        return generation;
    }

    @Override
    public String getSelectionAlgorithm() {
        return this.selectionAlgorithm;
    }

    @Override
    public void setSelectionAlgorithm(String selectionAlgorithm) {
        if (Arrays.stream(optionsSelectionAlgorithms).anyMatch(Predicate.isEqual(selectionAlgorithm))) {
            this.selectionAlgorithm = selectionAlgorithm;
        }
    }
    @Override
    public String getCrossoverAlgorithm() {
        return crossoverAlgorithm;
    }

    @Override
    public void setCrossoverAlgorithm(String crossoverAlgorithm) {
        if (Arrays.stream(optionsCrossoverAlgorithms).anyMatch(Predicate.isEqual(crossoverAlgorithm))) {
            this.crossoverAlgorithm = crossoverAlgorithm;
        }
    }

    @Override
    public void setUniformCrossoverMask(List<Boolean> mask){
        this.uniformCrossoverMask = mask;
    }

    @Override
    public List<Boolean> getUniformCrossoverMask(){
        return this.uniformCrossoverMask;
    }

    @Override
    public void next(boolean verbose){
        if(generation == 0){
            generateInitialPopulation();
        }
        if(verbose)
            System.out.printf("GENERATION %d: %n", generation);
        evaluateChromosomes(verbose);
        generation++;
        elitifyChromosomes();
        List<Chromosome> nonElites = selectChromosomes();
        crossoverChromosomes(nonElites);
        mutateChromosomes(nonElites);
        addNonElites(nonElites);
    }

    @Override
    public void run(boolean verbose){
        while (generation < getMaxGenerations()){
            next(verbose);
        }
        evaluateChromosomes(verbose);
    }

    public List<Chromosome> getBestFromEachGeneration() throws NoSuchElementException {
        return getBestChromosomes();
    }
}
