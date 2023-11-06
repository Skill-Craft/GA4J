package genetic.algorithm;

public interface SelectionAlgorithm {
    String[] optionsSelectionAlgorithms = {"roulete", "tournament", "rank", "random"};
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
        switch (getSelectionAlgorithm()) {
            case "roulette" -> roulette(getSelectionRate());
            case "tournament" -> tournament(getSelectionRate());
            case "rank" -> rank(getSelectionRate());
            case "random" -> random(getSelectionRate());
        }
    }
}
