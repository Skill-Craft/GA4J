package genetic.algorithm;

import java.util.Objects;

public interface SelectionAlgorithm {
    private void roulette(Float rate){
        // Selection
    }

    private void tournament(Float rate){
        // Selection
    }

    private void rank(Float rate){
        // Selection
    }

    private void random(Float rate){
        // Selection
    }

    String getSelectionAlgorithm();

    Float getSelectionRate();

    default void select(){
        if(Objects.equals(getSelectionAlgorithm(), "roulette")){
            roulette(getSelectionRate());
        } else if(Objects.equals(getSelectionAlgorithm(), "tournament")){
            tournament(getSelectionRate());
        } else if(Objects.equals(getSelectionAlgorithm(), "rank")){
            rank(getSelectionRate());
        } else if(Objects.equals(getSelectionAlgorithm(), "random")){
            random(getSelectionRate());
        }
    }
}
