package genetic.algorithm;

import java.util.ArrayList;
import java.util.function.Function;

public interface BaseGeneticAlgorithm {
    ArrayList<Integer> population(); // PopulationSize, ChromosomeSize, MaxGenerations
    ArrayList<Float> rates(); // Mutation, Crossover, Elitism, Selection

    Function fitnessFunction(); // FitnessFunction

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
