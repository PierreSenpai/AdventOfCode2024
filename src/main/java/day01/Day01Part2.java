package day01;

import util.InputReader;
import java.util.ArrayList;
import java.util.List;

public class Day01Part2 {
    private List<String> content;
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    Day01Part2(String path) {
        content = InputReader.readInputByLine(path);
    }

    public void fillLists() {
        for (String line : content) {
            String[] numbers = line.split("   ");
            list1.add(Integer.valueOf(numbers[0]));
            list2.add(Integer.valueOf(numbers[1]));
        }

        // Collections.sort(list1);
        // Collections.sort(list2);
    }

    public int calculateSimilarity() {
        int sum = 0;
        for (int number : list1) {
            sum += similarityOf(number);
        }
        return sum;
    }

    public int similarityOf(int number) {
        int frequency = 0;
        for (int numberRight : list2) {
            if (number == numberRight) {frequency++;}
        }
        return number * frequency;
    }


    public static void main(String[] args) {
        Day01Part2 d1p2 = new Day01Part2("src/main/resources/day01.txt");
        d1p2.fillLists();
        System.out.println(d1p2.calculateSimilarity());
    }
}
