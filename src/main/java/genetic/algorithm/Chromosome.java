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

    void crossover(Chromosome other){

    }

    void elitify(){
       this.isElite = !this.isElite;
    }

}
