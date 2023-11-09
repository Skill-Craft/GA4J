package genetic.algorithm;

import java.util.*;
import java.util.function.Function;

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
    Integer getGeneration();
    String getCrossoverAlgorithm();
    void setCrossoverAlgorithm(String crossoverAlgorithm);
    void setSelectionAlgorithm(String selectionAlgorithm);
    String getSelectionAlgorithm();

    default List<Chromosome> getPopulation(){
        if(!getAllPopulations().isEmpty()){
            return getAllPopulations().get(getGeneration()-1);
        }
        return null;
    }
    default Integer getEliteNumber(){
        return (int) (getPopulationSize() * getElitismRate());
    }

    default Integer getNonEliteNumber(){
        return getPopulationSize() - getEliteNumber();
    }

    default void generateInitialPopulation(){
        getAllPopulations().get(getGeneration()).addAll(Chromosome.generatePopulation(getPopulationSize(), getChromosomeSize(), fitnessFunction()));
    }

    default void evaluateChromosomes(boolean verbose){
        getAllPopulations().get(getGeneration()).stream().parallel().forEach(Chromosome::computeFitness);
        if(verbose){
            for(int i = 0; i<getPopulationSize(); i++){
                getAllPopulations().get(getGeneration()).get(i).printFitness(i);
            }
        }
    }

    default void elitifyChromosomes(){
        List<Chromosome> aux = getAllPopulations().get(getGeneration()-1).stream().parallel().sorted().toList();
        getAllPopulations().add(aux.subList(0, getEliteNumber()));
    }

    default void preProcessRoulette(){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    default List<Chromosome> roulette(Integer numberOfChromosomes) throws VerifyError{
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
        return null;
    }

    default List<Chromosome> tournament(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    default List<Chromosome> rank(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    default List<Chromosome> random(Integer numberOfChromosomes){
        Random rand = new Random();
        for(int i=0; i<getPopulationSize();i++){
            getPopulation().add(getAllPopulations().get(getGeneration()-1).get(rand.nextInt(getPopulationSize())));
        }
        return null;
    }


    default List<Chromosome> selectChromosomes(){
        switch (getSelectionAlgorithm()) {
            case "roulette" -> {
                return roulette(getNonEliteNumber());
            }
            case "tournament" -> {
                return tournament(getNonEliteNumber());
            }
            case "rank" -> {
                return rank(getNonEliteNumber());
            }
            case "random" -> {
                return random(getNonEliteNumber());
            }
            default -> {
                return null;
            }
        }
    }


    default void crossoverChromosomes(List<Chromosome> nonElites){
        //!!!!!!!!!!!!!!!!!!
    }


    default void mutateChromosomes(List<Chromosome> nonElites){
        nonElites.stream().parallel().forEach(c -> {
            if (Math.random() < getProbabilityOfMutation()){
                c.mutate(getMutationRate());
            }
        });
    }

    default void addNonElites(List<Chromosome> nonElites){
        getPopulation().addAll(nonElites);
    }

    void next(boolean verbose);

    void run(boolean verbose);


    default List<Chromosome> getBestChromosomes(){
        return getAllPopulations().stream().parallel().map(arr -> {
            ArrayList<Chromosome> aux = new ArrayList<>(arr);
            aux.sort(Comparator.comparing(Chromosome::getFitness));
            return aux.get(0);
        }).toList();
    }

}
