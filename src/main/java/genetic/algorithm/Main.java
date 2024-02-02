package genetic.algorithm;

import java.util.function.Function;


public class Main {

    public static void main(String[] args) {
        Integer populationSize = 1000;
        Integer chromosomeSize = 1000;
        Integer maxGenerations = 200;
        Float elitismRate = 0.1f;
        Float mutationRate = 0.1f;
        Float probabilityMutation = 0.2f;
        Float crossoverRate = 0.8f;
        Function<Chromosome, Float> fitnessFunction = t -> t.getState().stream().reduce(0, Integer::sum).floatValue();
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
        ga.run(false);
        var aux = ga.getBestFromEachGeneration();
        for(int i=0; i<aux.size(); i++)
            aux.get(i).printFitness(i);
    }
}