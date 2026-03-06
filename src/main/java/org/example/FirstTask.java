package main.java.org.example;

import java.util.Arrays;
import java.util.Random;


import static java.lang.Math.abs;


public class FirstTask {

    public static void main()
    {
        int[] array = new int[50];

        int currentNumber = 0;


        Random random = new Random();


        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100);
        }


        for (int num : array) {
            if(num > abs(currentNumber))
            {
                currentNumber = num;
            }

        }

        System.out.println("Текущий массив:" + Arrays.toString(array));

        System.out.println("Максимальное число по модулю:" + currentNumber);
    }
}
