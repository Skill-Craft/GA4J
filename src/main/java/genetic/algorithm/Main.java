package genetic.algorithm;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

class Aux{
    public Float val = 0f;
    public Float fitness;
    public Float getFitness(){
        return fitness;
    }

    public Aux(Float fitness){
        this.fitness = fitness;
    }
}
public class Main {

    public static void main(String[] args) {
        Integer populationSize = 100;
        Integer chromosomeSize = 100;
        Integer maxGenerations = 100;
        Float elitismRate = 0.1f;
        Float mutationRate = 0.1f;
        Float probabilityMutation = 0.1f;
        Float crossoverRate = 0.1f;
        Function<Chromosome, Float> fitnessFunction = (Function<Chromosome, Float>) t -> t.getState().stream().reduce(0, Integer::sum).floatValue();
        String selectionAlgorithm = "roulette";
        BaseGeneticAlgorithm ga = new BaseGeneticAlgorithm(
                populationSize,
                chromosomeSize,
                maxGenerations,
                elitismRate,
                mutationRate,
                probabilityMutation,
                crossoverRate,
                fitnessFunction,
                selectionAlgorithm
        );
        ga.run(true);
    }
}