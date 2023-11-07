package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public class Chromosome {
    boolean isElite;
    private final Function fitnessFunction;
    private final Integer chromosomeSize;
    private Float val;

    ArrayList<Integer> state;


    public Chromosome(Integer chromosomeSize, Function fitnessFunction){
        this.fitnessFunction = fitnessFunction;
        this.chromosomeSize = chromosomeSize;
        this.isElite = false;
    }

    public static Collection<? extends Chromosome> generatePopulation(Integer populationSize, Integer chromosomeSize, Function fitnessFunction) {
        return Arrays.asList(new Chromosome[populationSize]);
    }


    Chromosome createMutant(){
        return null;  //new Chromosome(); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    void mutate(){

    }

    void crossover(Chromosome other, String crossoverType){
        switch (crossoverType) {
            case "one-point" -> onePointCrossover(other);
            case "two-point" -> twoPointCrossover(other);
            case "uniform" -> uniformCrossover(other);
        }
    }

    void onePointCrossover(Chromosome other){
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    void twoPointCrossover(Chromosome other){
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    void uniformCrossover(Chromosome other){
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    void switchEliteStatus(){
       this.isElite = !this.isElite;
    }

    public void computeFitness() {
        this.val = (Float) this.fitnessFunction.apply(this);
    }

    public void printFitness(Integer i) {
        System.out.printf("Individual %d -> fitness %f%n", i, val);
    }
}
