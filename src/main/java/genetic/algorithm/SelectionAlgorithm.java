package genetic.algorithm;

import java.util.List;

public interface SelectionAlgorithm {
    String[] optionsSelectionAlgorithms = {"roulette", "tournament", "rank", "random"};
    private void roulette(){
        // Selection
    }

    private void tournament(){
        // Selection
    }

    private void rank(){
        // Selection
    }

    private void random(){
        // Selection
    }

    String getSelectionAlgorithm();

    List<Chromosome> getPopulation();

    default void select(){
        switch (getSelectionAlgorithm()) {
            case "roulette" -> roulette();
            case "tournament" -> tournament();
            case "rank" -> rank();
            case "random" -> random();
        }
    }
}
