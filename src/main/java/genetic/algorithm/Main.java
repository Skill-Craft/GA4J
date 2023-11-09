package genetic.algorithm;


import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Integer populationSize = 100;
        Integer chromosomeSize = 100;
        Integer maxGenerations = 100;
        Float elitismRate = 0.1f;
        Float mutationRate = 0.1f;
        Float probabilityMutation = 0.1f;
        Float crossoverRate = 0.1f;
        Float crossoverProbability = 0.1f;
        Function fitnessFunction = new Function<Chromosome, Float>(){
            @Override
            public Float apply(Chromosome t) {
                return t.getState().stream().reduce(0, Integer::sum).floatValue();
            }
        };
        String selectionAlgorithm = "roulette";
        BaseGeneticAlgorithm ga = new BaseGeneticAlgorithm(
                populationSize,
                chromosomeSize,
                maxGenerations,
                elitismRate,
                mutationRate,
                probabilityMutation,
                crossoverRate,
                crossoverProbability,
                fitnessFunction,
                selectionAlgorithm
        );

        ga.run(true);
        ga.getBestFromEachGeneration().forEach(c -> c.printFitness(0));
    }
}