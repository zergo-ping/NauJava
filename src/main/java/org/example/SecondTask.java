package main.java.org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondTask {


    public static void mergeSort(ArrayList<Double> list) {
        if (list.size() <= 1) {
            return;
        }

        int mid = list.size() / 2;

        ArrayList<Double> left = new ArrayList<>();
        ArrayList<Double> right = new ArrayList<>();

        for (int i = 0; i < mid; i++) {
            left.add(list.get(i));
        }
        for (int i = mid; i < list.size(); i++) {
            right.add(list.get(i));
        }

        mergeSort(left);
        mergeSort(right);

        merge(list, left, right);
    }

    private static void merge(ArrayList<Double> list, ArrayList<Double> left, ArrayList<Double> right) {
        int leftIndex = 0;
        int rightIndex = 0;
        int listIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                list.set(listIndex++, left.get(leftIndex++));
            } else {
                list.set(listIndex++, right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            list.set(listIndex++, left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            list.set(listIndex++, right.get(rightIndex++));
        }
    }

        public static void main() {
            ArrayList<Double> list =  new ArrayList<>();
            Random random = new Random();
            for (int i = 0; i < 50; i++) {
                list.add(random.nextDouble(1000));
            }
            List<Double> copyArr = new ArrayList<>(list);

            mergeSort(list);

           System.out.println("Исходный список:" + copyArr.toString());
           System.out.println("Отсортированный список:" + list.toString());
        }


    }

