package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public interface IGA {
    default List<Integer> populationInfo(){
        return Arrays.asList(getPopulationSize(), getChromosomeSize(), getMaxGenerations());
    }
    default List<Float> rates(){
        return Arrays.asList(getMutationRate(), getCrossoverRate(), getElitismRate(), getSelectionRate());
    }

    Float getMutationRate();
    Float getSelectionRate();
    Float getElitismRate();
    Float getCrossoverRate();

    Integer getPopulationSize();
    Integer getChromosomeSize();
    Integer getMaxGenerations();

    Function fitnessFunction(); // FitnessFunction

    ArrayList<ArrayList<Chromosome>> getAllPopulations(); // Population

    ArrayList<Chromosome> getPopulation();
    Integer getGeneration(); // Generation

    default void selectChromosomes(){
        // Selection
    }

    default void crossoverChromosomes(){
        // Crossover
    }

    default void mutateChromosomes(){
        // Mutation
    }

    default void elitifyChromosomes(){
        // Elitism
    }

    default void evaluateChromosomes(boolean verbose){
        getAllPopulations().get(getGeneration()).stream().parallel().forEach(Chromosome::computeFitness);
        if(verbose){
            for(Integer i=0; i<getPopulationSize();i++){
                getAllPopulations().get(getGeneration()).get(i).printFitness(i);
            }
        }
    }

    default void generateInitialPopulation(){
        getAllPopulations().get(getGeneration()).addAll(Chromosome.generatePopulation(getPopulationSize(), getChromosomeSize(), fitnessFunction()));
    }
}
