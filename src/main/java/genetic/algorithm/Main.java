package genetic.algorithm;
import genetic.algorithm.BaseGeneticAlgorithm;
import java.util.ArrayList;
import java.util.function.Function;


public class Main {

    static class A implements BaseGeneticAlgorithm{
        public ArrayList<Integer> population(){
            return new ArrayList<Integer>();
        }
        public ArrayList<Float> rates(){
            return new ArrayList<Float>();
        }
        public Function fitnessFunction(){
            return o -> {
                return null;
            };
        }
    }

    public static void main(String[] args) {

        A a = new A();
        a.selectChromosomes();

    }
}