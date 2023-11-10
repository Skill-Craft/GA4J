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
            return getAllPopulations().get(getGeneration());
        }
        throw new RuntimeException("Population not initialized");
    }
    default Integer getEliteNumber(){
        return (int) (getPopulationSize() * getElitismRate());
    }

    default Integer getNonEliteNumber(){
        return getPopulationSize() - getEliteNumber();
    }

    default void generateInitialPopulation(){
        getAllPopulations().add(new ArrayList<>());
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
        List<Chromosome> aux = getAllPopulations().get(getPreviousGeneration()).stream().sorted(Comparator.comparing(Chromosome::getFitness)).toList();
        getAllPopulations().add(new ArrayList<>(aux.subList(getPopulationSize()-getEliteNumber(), getPopulationSize())));
    }

    default int getPreviousGeneration() {
        return getGeneration() - 1;
    }

    default void preProcessRoulette(){
        Float sum = getAllPopulations().get(getPreviousGeneration()).stream().reduce(0f, (acc, c) -> {
            c.setRouletteValue(acc + c.getFitness());
            return acc + c.getFitness();
        }, Float::sum);
        getAllPopulations().get(getPreviousGeneration()).stream().parallel().forEach(c -> c.setRouletteValue(c.getRouletteValue()/sum));
    }

    default List<Chromosome> roulette(Integer numberOfChromosomes) throws VerifyError{
        preProcessRoulette();
        List<Chromosome> res = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<numberOfChromosomes; i++){
            Thread worker = new Thread(() -> {
                List<Chromosome> aux = getAllPopulations()
                        .get(getPreviousGeneration())
                        .stream()
                        .parallel()
                        .filter(c -> c.getRouletteValue() >= Math.random())
                        .toList();

                Optional<Chromosome> chromosome = aux
                        .stream()
                        .min(Comparator.comparing(Chromosome::getRouletteValue));

                if(chromosome.isPresent()){
                    res.add(chromosome.get());
                } else{
                    throw new VerifyError("Unexpected error occurred");
                }
            });
            threads.add(worker);
            worker.start();
        }
        for(var thread: threads){
            try{
                thread.join();
            } catch(InterruptedException ignored){
            }
        }
        return res;
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
        List<Chromosome> aux = new ArrayList<>(getPopulationSize());
        for(int i=0; i<numberOfChromosomes;i++){
            aux.set(i, getAllPopulations().get(getPreviousGeneration()).get(Math.floorMod(getPopulationSize(),getPopulationSize())));
        }
        return aux;
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


    default void crossoverChromosomes(List<Chromosome> nonElites){
        List<Thread> threads = new ArrayList<>();
        for(int i=0; i<(int)(getPopulationSize()*getCrossoverRate());i++){
            Thread actor = new Thread(() -> {
                Random rand = new Random();
                int i1 = Math.floorMod(rand.nextInt(), nonElites.size());
                int i2 = Math.floorMod(rand.nextInt(), nonElites.size());
                while(i2 == i1){
                    i2 = Math.floorMod(rand.nextInt(), nonElites.size());
                }
                Chromosome[] children = nonElites.get(i1).crossover(nonElites.get(i2), getCrossoverAlgorithm());
                nonElites.set(i1, children[0]);
                nonElites.set(i2, children[1]);
            });
            threads.add(actor);
            actor.start();
        }

        for(var thread: threads){
            try{
                thread.join();
            } catch(InterruptedException ignored){
            }
        }
//        System.out.println(nonElites.size());
        //!!!!!!!!!!!!!!!!!!!!!!!!!! HANDLE THE LACK OF WAITING FOR THE THREADS TO FINISH
    }

    void setUniformCrossoverMask(List<Boolean> mask);
    List<Boolean> getUniformCrossoverMask();


    default void mutateChromosomes(List<Chromosome> nonElites){
        nonElites.stream().parallel().forEach(c -> {
            if (Math.random() < getMutationRate()){
                c.mutate(getProbabilityOfMutation());
            }
        });
    }

    default void addNonElites(List<Chromosome> nonElites){
        getPopulation().addAll(nonElites);
    }

    void next(boolean verbose);

    void run(boolean verbose);


    default List<Chromosome> getBestChromosomes() throws NoSuchElementException {
        return getAllPopulations().stream()
                .map(arr -> {
            var res = arr.stream().max(Comparator.comparing(Chromosome::getFitness));
            if(res.isPresent()){
                return res.get();
            } else{
                throw new NoSuchElementException("Unexpected error occurred");
            }
        }).toList();
    }

}
