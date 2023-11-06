package genetic.algorithm;

import java.util.ArrayList;

public class Chromosome {
    boolean isElite;

    ArrayList<Integer> state;


    public Chromosome(){
    }


    Chromosome createMutant(){
        return new Chromosome(); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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

    void elitify(){
       this.isElite = !this.isElite;
    }

}
