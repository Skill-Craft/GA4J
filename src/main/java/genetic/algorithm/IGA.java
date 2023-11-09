package genetic.algorithm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IGA {
    String[] optionsSelectionAlgorithms = {"roulette", "tournament", "rank", "random"};
    String[] optionsCrossoverAlgorithms = {"one-point", "two-point", "uniform"};

    Float getMutationRate();
    Float getProbabilityOfMutation();
    Float getElitismRate();
    Float getCrossoverRate();
    Float getCrossoverProbability();
    Integer getPopulationSize();
    Integer getChromosomeSize();
    Integer getMaxGenerations();

    Function fitnessFunction();

    List<List<Chromosome>> getAllPopulations();

    List<Chromosome> getPopulation();
    Integer getGeneration();

    default void crossoverChromosomes(){
    }

    default void mutateChromosomes(){
        getPopulation().stream().parallel().forEach(c -> {
            if (Math.random() < getProbabilityOfMutation()){
                c.mutate(getMutationRate());
            }
        });
    }

    default void elitifyChromosomes(){
        List<Chromosome> aux = getAllPopulations().get(getGeneration()-1).stream().parallel().sorted().toList();
        getAllPopulations().add(aux.subList(0, getEliteNumber()));
    }

    default void evaluateChromosomes(boolean verbose){
        getAllPopulations().get(getGeneration()).stream().parallel().forEach(Chromosome::computeFitness);
        if(verbose){
            for(int i = 0; i<getPopulationSize(); i++){
                getAllPopulations().get(getGeneration()).get(i).printFitness(i);
            }
        }
    }

    default void generateInitialPopulation(){
        getAllPopulations().get(getGeneration()).addAll(Chromosome.generatePopulation(getPopulationSize(), getChromosomeSize(), fitnessFunction()));
    }

    void next(boolean verbose);

    void run(boolean verbose);

    default void preProcessRoulette(){

    }

    default void roulette(Integer numberOfChromosomes) throws VerifyError{
        preProcessRoulette();
        for(int i=0; i<numberOfChromosomes; i++){
            List<Chromosome> aux = getAllPopulations().get(getGeneration()-1).stream().parallel().filter(c -> c.getRouletteValue() >= Math.random()).toList();
            Optional<Chromosome> chromosome = aux.stream().min(Comparator.comparing(Chromosome::getRouletteValue));
            if(chromosome.isPresent()){
                getPopulation().add(chromosome.get());
            } else{
                throw new VerifyError("Unexpected error occurred");
            }
        }
    }

    default void tournament(Integer numberOfChromosomes){
    }

    default void rank(Integer numberOfChromosomes){
    }

    default void random(Integer numberOfChromosomes){
        Random rand = new Random();
        for(int i=0; i<getPopulationSize();i++){
            getPopulation().add(getAllPopulations().get(getGeneration()-1).get(rand.nextInt(getPopulationSize())));
        }
    }

    String getSelectionAlgorithm();

    default Integer getEliteNumber(){
        return (int) (getPopulationSize() * getElitismRate());
    }

    default Integer getNonEliteNumber(){
        return getPopulationSize() - getEliteNumber();
    }

    default void selectChromosomes(){
        switch (getSelectionAlgorithm()) {
            case "roulette" -> roulette(getNonEliteNumber());
            case "tournament" -> tournament(getNonEliteNumber());
            case "rank" -> rank(getNonEliteNumber());
            case "random" -> random(getNonEliteNumber());
        }
    }

    default List<Chromosome> getBestChromosomes(){
        return getAllPopulations().stream().parallel().map(arr -> {
            ArrayList<Chromosome> aux = new ArrayList<>(arr);
            aux.sort(Comparator.comparing(Chromosome::getFitness));
            return aux.get(0);
        }).toList();
    }

    String getCrossoverAlgorithm();

    void setCrossoverAlgorithm(String crossoverAlgorithm);
    void setSelectionAlgorithm(String selectionAlgorithm);
}
