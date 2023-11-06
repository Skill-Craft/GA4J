package genetic.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseGeneticAlgorithm implements IGA, SelectionAlgorithm{

    private Float crossoverRate;
    private Float elitismRate;
    private Float selectionRate;
    private Float mutationRate;
    private Function fitnessFunction;
    private String selectionAlgorithm;
    private ArrayList<ArrayList<Chromosome>> populations;


    @Override
    public ArrayList<Chromosome> getPopulation() {
        if(!populations.isEmpty()){
            return populations.get(populations.size()-1);
        }
        return null;
    }

    @Override
    public ArrayList<ArrayList<Chromosome>> getAllPopulations() {
        return populations;
    }

    @Override
    public ArrayList<Integer> populationInfo() { // PopulationSize, ChromosomeSize, MaxGenerations
        return null;
    }

    @Override
    public ArrayList<Float> rates() {
        ArrayList<Float> rates = new ArrayList<>();
        rates.add(this.mutationRate);
        rates.add(this.crossoverRate);
        rates.add(this.elitismRate);
        rates.add(this.selectionRate);
        return rates;
    }

    @Override
    public Function fitnessFunction() {
        return this.fitnessFunction;
    }

    @Override
    public Float getSelectionRate() {
        return this.selectionRate;
    }

    @Override
    public String getSelectionAlgorithm() {
        return this.selectionAlgorithm;
    }

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        if (Arrays.stream(optionsSelectionAlgorithms).anyMatch(Predicate.isEqual(selectionAlgorithm))) {
            this.selectionAlgorithm = selectionAlgorithm;
        }
    }

    @Override
    public Long getGeneration() {
        return null;
    }
}
