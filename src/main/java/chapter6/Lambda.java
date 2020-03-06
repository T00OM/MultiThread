package chapter6;

import java.util.Arrays;
import java.util.List;

public class Lambda {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        list.forEach((Integer value)->{
            System.out.println(value);
        });
    }
}
