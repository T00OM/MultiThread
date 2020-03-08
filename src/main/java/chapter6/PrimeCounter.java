package chapter6;

import java.util.stream.IntStream;

public class PrimeCounter {

    public static boolean isPrime(int i) {
        if(i <= 2)
            return false;
        for(int j = 2; j <= Math.sqrt(i); j++){
            if(i % j == 0)
                return false;
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println(IntStream.range(1, 1000000).filter(PrimeCounter::isPrime).count());
        System.out.println(IntStream.range(1, 1000000).parallel().filter(PrimeCounter::isPrime).count());
    }
}
