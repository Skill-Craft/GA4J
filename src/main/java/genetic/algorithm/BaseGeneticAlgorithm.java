package genetic.algorithm;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class BaseGeneticAlgorithm{
    String[] optionsSelectionAlgorithms = {"roulette", "tournament", "rank", "random"};
    String[] optionsCrossoverAlgorithms = {"one-point", "two-point", "uniform"};
    private final Function<Chromosome, Float> fitnessFunction;
    private final Float elitismRate;
    private List<Boolean> uniformCrossoverMask;
    private String selectionAlgorithm;

    private String crossoverAlgorithm = "one-point";
    private final Float crossoverRate;
    private final Float mutationRate;
    private final Float probabilityOfMutation;
    private final List<List<Chromosome>> populations;
    private final Integer populationSize;
    private final Integer chromosomeSize;
    private final Integer maxGenerations;
    private Integer generation;


    public BaseGeneticAlgorithm(){
        this(1000, 1000, 1000, 0.1f, 0.1f, 0.1f, 0.7f, t -> t.getState().stream().reduce(0, Integer::sum).floatValue(), "roulette");
    }


    public BaseGeneticAlgorithm(Integer populationSize,
                                Integer chromosomeSize,
                                Integer maxGenerations,
                                Float elitismRate,
                                Float mutationRate,
                                Float probabilityMutation,
                                Float crossoverRate,
                                Function<Chromosome, Float> fitnessFunction,
                                String selectionAlgorithm)
    {
        setSelectionAlgorithm(selectionAlgorithm);
        setCrossoverAlgorithm("one-point");
        this.chromosomeSize = chromosomeSize;
        this.maxGenerations = maxGenerations;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismRate = elitismRate;
        this.fitnessFunction = fitnessFunction;
        this.populationSize = populationSize;
        this.probabilityOfMutation = probabilityMutation;
        this.crossoverAlgorithm = "one-point";
        this.uniformCrossoverMask = new ArrayList<>();

        populations = new ArrayList<>();
        this.generation = 0;

    }

    public List<List<Chromosome>> getAllPopulations() {
        return populations;
    }

    public Function<Chromosome, Float> fitnessFunction() {
        return this.fitnessFunction;
    }

    public Float getMutationRate() {
        return mutationRate;
    }

    public Float getProbabilityOfMutation() {
        return probabilityOfMutation;
    }

    public Float getElitismRate() {
        return elitismRate;
    }

    public Float getCrossoverRate() {
        return crossoverRate;
    }

    public Integer getPopulationSize() {
        return populationSize;
    }

    public Integer getChromosomeSize() {
        return chromosomeSize;
    }

    public Integer getMaxGenerations() {
        return maxGenerations;
    }

    public Integer getGeneration() {
        return generation;
    }

    public String getSelectionAlgorithm() {
        return this.selectionAlgorithm;
    }

    public void setSelectionAlgorithm(String selectionAlgorithm) {
        if (Arrays.stream(optionsSelectionAlgorithms).anyMatch(Predicate.isEqual(selectionAlgorithm))) {
            this.selectionAlgorithm = selectionAlgorithm;
        }
    }

    public String getCrossoverAlgorithm() {
        return crossoverAlgorithm;
    }

    public void setCrossoverAlgorithm(String crossoverAlgorithm) {
        if (Arrays.stream(optionsCrossoverAlgorithms).anyMatch(Predicate.isEqual(crossoverAlgorithm))) {
            this.crossoverAlgorithm = crossoverAlgorithm;
        }
    }

    public void setUniformCrossoverMask(List<Boolean> mask){
        this.uniformCrossoverMask = mask;
    }

    public List<Boolean> getUniformCrossoverMask(){
        return this.uniformCrossoverMask;
    }

    public void next(boolean verbose){
        if(generation == 0){
            generateInitialPopulation();
        }
        if(verbose)
            System.out.printf("GENERATION %d: %n", generation);
        evaluateChromosomes(verbose);
        generation++;
        elitifyChromosomes();
        List<Chromosome> nonElites = selectChromosomes();
        crossoverChromosomes(nonElites);
        mutateChromosomes(nonElites);
        addNonElites(nonElites);
    }

    List<Chromosome> getPopulation(){
        if(!getAllPopulations().isEmpty()){
            return getAllPopulations().get(getGeneration());
        }
        throw new RuntimeException("Population not initialized");
    }
    Integer getEliteNumber(){
        return (int) (getPopulationSize() * getElitismRate());
    }

    Integer getNonEliteNumber(){
        return getPopulationSize() - getEliteNumber();
    }

    void generateInitialPopulation(){
        getAllPopulations().add(new ArrayList<>());
        getAllPopulations().get(getGeneration()).addAll(Chromosome.generatePopulation(getPopulationSize(), getChromosomeSize(), fitnessFunction()));
    }

    void evaluateChromosomes(boolean verbose){
        getAllPopulations().get(getGeneration()).stream().parallel().forEach(Chromosome::computeFitness);
        if(verbose){
            for(int i = 0; i<getPopulationSize(); i++){
                getAllPopulations().get(getGeneration()).get(i).printFitness(i);
            }
        }
    }

    void elitifyChromosomes(){
        List<Chromosome> aux = getAllPopulations().get(getPreviousGeneration()).stream().sorted(Comparator.comparing(Chromosome::getFitness)).toList();
        getAllPopulations().add(new ArrayList<>(aux.subList(getPopulationSize()-getEliteNumber(), getPopulationSize())));
    }

    int getPreviousGeneration() {
        return getGeneration() - 1;
    }

    void preProcessRoulette(){
        Float sum = getAllPopulations().get(getPreviousGeneration()).stream().reduce(0f, (acc, c) -> {
            c.setRouletteValue(acc + c.getFitness());
            return acc + c.getFitness();
        }, Float::sum);
        getAllPopulations().get(getPreviousGeneration()).stream().parallel().forEach(c -> c.setRouletteValue(c.getRouletteValue()/sum));
    }

    List<Chromosome> roulette(Integer numberOfChromosomes) throws VerifyError{
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

    List<Chromosome> tournament(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    List<Chromosome> rank(Integer numberOfChromosomes){
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return null;
    }

    List<Chromosome> random(Integer numberOfChromosomes){
        List<Chromosome> aux = new ArrayList<>(getPopulationSize());
        for(int i=0; i<numberOfChromosomes;i++){
            aux.set(i, getAllPopulations().get(getPreviousGeneration()).get(Math.floorMod(getPopulationSize(),getPopulationSize())));
        }
        return aux;
    }


    List<Chromosome> selectChromosomes(){
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


    void crossoverChromosomes(List<Chromosome> nonElites){
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
    }

    void mutateChromosomes(List<Chromosome> nonElites){
        nonElites.stream().parallel().forEach(c -> {
            if (Math.random() < getMutationRate()){
                c.mutate(getProbabilityOfMutation());
            }
        });
    }

    void addNonElites(List<Chromosome> nonElites){
        getPopulation().addAll(nonElites);
    }

    List<Chromosome> getBestChromosomes() throws NoSuchElementException {
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

    public void run(boolean verbose){
        while (generation < getMaxGenerations()){
            next(verbose);
        }
        evaluateChromosomes(verbose);
    }

    public List<Chromosome> getBestFromEachGeneration() throws NoSuchElementException {
        return getBestChromosomes();
    }
}
