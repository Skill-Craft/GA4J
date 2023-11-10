package genetic.algorithm;

import java.util.*;
import java.util.function.Function;

public interface IGA {
    String[] optionsSelectionAlgorithms = {"roulette", "tournament", "rank", "random"};
    String[] optionsCrossoverAlgorithms = {"one-point", "two-point", "uniform"};

    Float getMutationRate();
    Float getProbabilityOfMutation();
    Float getElitismRate();
    Float getCrossoverRate();
    Integer getPopulationSize();
    Integer getChromosomeSize();
    Integer getMaxGenerations();
    Function<Chromosome, Float> fitnessFunction();
    List<List<Chromosome>> getAllPopulations();
    Integer getGeneration();
    String getCrossoverAlgorithm();
    void setCrossoverAlgorithm(String crossoverAlgorithm);
    void setSelectionAlgorithm(String selectionAlgorithm);
    String getSelectionAlgorithm();

    default List<Chromosome> getPopulation(){
        if(!getAllPopulations().isEmpty()){
            return getAllPopulations().get(getGeneration()-1);
        }
        return null;
    }
    default Integer getEliteNumber(){
        return (int) (getPopulationSize() * getElitismRate());
    }

    default Integer getNonEliteNumber(){
        return getPopulationSize() - getEliteNumber();
    }

    default void generateInitialPopulation(){
        getAllPopulations().get(getGeneration()).addAll(Chromosome.generatePopulation(getPopulationSize(), getChromosomeSize(), fitnessFunction()));
    }

    default void evaluateChromosomes(boolean verbose){
        getAllPopulations().get(getGeneration()).stream().parallel().forEach(Chromosome::computeFitness);
        if(verbose){
            for(int i = 0; i<getPopulationSize(); i++){
                getAllPopulations().get(getGeneration()).get(i).printFitness(i);
            }
        }
    }

    default void elitifyChromosomes(){
        List<Chromosome> aux = getAllPopulations().get(getGeneration()-1).stream().parallel().sorted().toList();
        getAllPopulations().add(aux.subList(0, getEliteNumber()));
    }

    default void preProcessRoulette(){
        Float sum = getPopulation().stream().reduce(0f, (acc, c) -> {
            c.setRouletteValue(acc + c.getFitness());
            return acc + c.getFitness();
        }, Float::sum);
        getPopulation().stream().parallel().forEach(c -> c.setRouletteValue(c.getRouletteValue()/sum));
    }

    default List<Chromosome> roulette(Integer numberOfChromosomes) throws VerifyError{
        preProcessRoulette();
        for(int i=0; i<numberOfChromosomes; i++){
            List<Chromosome> aux = getAllPopulations().get(getGeneration()-1).stream().parallel().filter(c -> c.getRouletteValue() >= Math.random()).toList();
            Optional<Chromosome> chromosome = aux.stream().min(Comparator.comparing(Chromosome::getRouletteValue));
            if(chromosome.isPresent()){
                getPopulation().add(chromosome.get());
            } else{
                throw new VerifyError("Unexpected error occurred");
            }
        }
        return null;
    }

    default List<Chromosome> tournament(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    default List<Chromosome> rank(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    default List<Chromosome> random(Integer numberOfChromosomes){
        Random rand = new Random();
        for(int i=0; i<getPopulationSize();i++){
            getPopulation().add(getAllPopulations().get(getGeneration()-1).get(rand.nextInt(getPopulationSize())));
        }
        return null;
    }


    default List<Chromosome> selectChromosomes(){
        switch (getSelectionAlgorithm()) {
            case "roulette" -> {
                return roulette(getNonEliteNumber());
            }
            case "tournament" -> {
                return tournament(getNonEliteNumber());
            }
            case "rank" -> {
                return rank(getNonEliteNumber());
            }
            case "random" -> {
                return random(getNonEliteNumber());
            }
            default -> {
                return null;
            }
        }
    }


    default List<Chromosome> crossoverChromosomes(List<Chromosome> nonElites){
        List<Chromosome> result = new ArrayList<>();
        //!!!!!!!!!!!!!!!!!!
        for(int i=0; i<(int)(getPopulationSize()*getCrossoverRate());i++){
            Thread actor = new Thread(() -> {
                Random rand = new Random();
                int i1 = rand.nextInt();
                int i2 = rand.nextInt();
                while(i2 == i1){
                    i2 = rand.nextInt();
                }
                Chromosome[] children = nonElites.get(i1).crossover(nonElites.get(i2), getCrossoverAlgorithm());
                result.add(children[0]);
                result.add(children[1]);
            });
            actor.start();
        }
        return result;
    }


    default List<Chromosome> mutateChromosomes(List<Chromosome> nonElites){
        List<Chromosome> result = new ArrayList<>();
        nonElites.stream().parallel().forEach(c -> {
            if (Math.random() < getMutationRate()){
                result.add(c.createMutant(getProbabilityOfMutation()));
            }
        });
        return result;
    }

    default void addNonElites(List<Chromosome> nonElites){
        getPopulation().addAll(nonElites);
    }

    void next(boolean verbose);

    void run(boolean verbose);


    default List<Chromosome> getBestChromosomes() throws NoSuchElementException {
        return getAllPopulations().stream().parallel().map(arr -> {
            ArrayList<Chromosome> aux = new ArrayList<>(arr);
            var res = aux.stream().max(Comparator.comparing(Chromosome::getFitness));
            return res.get();
        }).toList();
    }

}
