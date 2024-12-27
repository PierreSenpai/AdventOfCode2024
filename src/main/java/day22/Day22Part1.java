package day22;

import java.util.List;
import java.util.stream.Collectors;

import util.InputReader;

public class Day22Part1 {
    private List<Integer> secretNumbers;

    Day22Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day22.txt");
        secretNumbers = input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public void processInput() {
        long sum = 0;
        for (int secretNumber : secretNumbers) {
            sum += finalSecretNumber(secretNumber, 2000);
        }
        System.out.println(sum);
    }

    public int finalSecretNumber(int secretNumber, int iterations) {
        for (int i = 0; i < iterations; i++) {
            secretNumber = nextSecretNumber(secretNumber);
        }
        return secretNumber;
    }

    public int nextSecretNumber(long secretNumber) {
        secretNumber = (secretNumber ^ (secretNumber * 64)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber / 32)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber * 2048)) % 16777216;
        return (int) secretNumber;
    }

    public static void main(String[] args) {
        Day22Part1 d22p1 = new Day22Part1();
        d22p1.processInput();
    }
}
