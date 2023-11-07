package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public interface IGA {
    String[] optionsSelectionAlgorithms = {"roulette", "tournament", "rank", "random"};

    default List<Integer> populationInfo(){
        return Arrays.asList(getPopulationSize(), getChromosomeSize(), getMaxGenerations());
    }
    default List<Float> rates(){
        return Arrays.asList(getMutationRate(), getCrossoverRate(), getElitismRate());
    }

    Float getMutationRate();
    Float getElitismRate();
    Float getCrossoverRate();

    Integer getPopulationSize();
    Integer getChromosomeSize();
    Integer getMaxGenerations();

    Function fitnessFunction(); // FitnessFunction

    ArrayList<ArrayList<Chromosome>> getAllPopulations(); // Population

    ArrayList<Chromosome> getPopulation();
    Integer getGeneration(); // Generation

    default void crossoverChromosomes(){
        // Crossover
    }

    default void mutateChromosomes(){
        // Mutation
    }

    default void elitifyChromosomes(){
        List<Chromosome> aux = getPopulation().stream().parallel().sorted().toList();
        Integer numElites = (int) (getPopulationSize() * getElitismRate());
        aux.subList(0, numElites).forEach(Chromosome::switchEliteStatus);
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

    private void roulette(){
        // Selection

    }

    private void tournament(){
        // Selection
    }

    private void rank(){
        // Selection
    }

    private void random(){
        // Selection
    }

    String getSelectionAlgorithm();

    default List<Chromosome> getNonElites(){
        return getPopulation().stream().parallel().filter(c -> !c.isElite).toList();
    }

    default void selectChromosomes(){
        switch (getSelectionAlgorithm()) {
            case "roulette" -> roulette();
            case "tournament" -> tournament();
            case "rank" -> rank();
            case "random" -> random();
        }
    }

    default List<Chromosome> getBestChromosomes(){
        return getAllPopulations().stream().parallel().map(arr -> {
            ArrayList<Chromosome> aux = new ArrayList<>(arr);
            aux.sort(Comparator.comparing(Chromosome::getFitness));
            return aux.get(0);
        }).toList();
    }
}
