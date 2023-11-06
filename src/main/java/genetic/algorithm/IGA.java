package genetic.algorithm;

import java.util.ArrayList;
import java.util.function.Function;

public interface IGA {
    ArrayList<Integer> populationInfo(); // PopulationSize, ChromosomeSize, MaxGenerations
    ArrayList<Float> rates(); // Mutation, Crossover, Elitism, Selection

    Function fitnessFunction(); // FitnessFunction

    ArrayList<ArrayList<Chromosome>> getAllPopulations(); // Population

    ArrayList<Chromosome> getPopulation();
    Long getGeneration(); // Generation

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
