package genetic.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface IGA {
    List<Integer> populationInfo(); // PopulationSize, ChromosomeSize, MaxGenerations
    List<Float> rates(); // Mutation, Crossover, Elitism, Selection

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

    default void evaluateChromosomes(){
        // FitnessFunction
    }
}
