package day22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import util.InputReader;

public class Day22Part2 {
    private List<Integer> buyers;
    // stores all 4-change-sequences that result in an equal or better price
    private List<List<Integer>> possibleSequences = new ArrayList<>();
    // for each buyer a map stores "sequence:priceAfterFirstOccurence"-pairs
    private List<Map<List<Integer>, Integer>> mapPerBuyer = new ArrayList<>();

    Day22Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day22.txt");
        buyers = input.stream().map(Integer::parseInt).collect(Collectors.toList());
    }

    public void processInput() {
        generateAllSecretNumbers();
        generatePossibleSequences();
        int mostBananas = 0;
        for (List<Integer> sequence : possibleSequences) {
            System.out.print("doing sequence: " + sequence + "   \r");
            int bananas = bananasForSequence(sequence);
            if (bananas > mostBananas) {
                mostBananas = bananas;
            }
        }
        System.out.println("\n" + mostBananas);
    }

    public void generateAllSecretNumbers() {
        for (int secretNumber : buyers) {
            generateSecretNumbers(secretNumber, 2000);
        }
    }

    public void generateSecretNumbers(int secretNumber, int iterations) {
        Map<List<Integer>, Integer> map = new HashMap<>();
        // holds the last 4 changes before the current price
        List<Integer> changes = new ArrayList<>();
        // price is the last digit -> modulo 10
        int price = secretNumber % 10;
        for (int i = 0; i < iterations; i++) {
            secretNumber = nextSecretNumber(secretNumber);
            int newPrice = secretNumber % 10;
            changes.add(newPrice - price);
            price = newPrice;
            // the first 4 prices (including the initial one) don't have
            // enough changes before them to make a valid sequence
            if (changes.size() < 4) {
                continue;
            }
            // once a price has more than 4 changes before it, the oldest of them
            // is removed
            if (changes.size() > 4) {
                changes.remove(0);
            }
            map.putIfAbsent(new ArrayList<>(changes), price);
        }
        mapPerBuyer.add(map);
    }

    public int nextSecretNumber(long secretNumber) {
        secretNumber = (secretNumber ^ (secretNumber * 64)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber / 32)) % 16777216;
        secretNumber = (secretNumber ^ (secretNumber * 2048)) % 16777216;
        return (int) secretNumber;
    }

    public void generatePossibleSequences() {
        for (int change1 = -9; change1 < 10; change1++) {
            for (int change2 = -9; change2 < 10; change2++) {
                for (int change3 = -9; change3 < 10; change3++) {
                    for (int change4 = -9; change4 < 10; change4++) {
                        // any sequence of changes, that leads to a lower price than before
                        // is discarded
                        if (change1 + change2 + change3 + change4 < 0) {
                            continue;
                        }
                        possibleSequences.add(List.of(change1, change2, change3, change4));
                    }
                }
            }
        }
    }

    public int bananasForSequence(List<Integer> sequence) {
        int sumOfBananas = 0;
        // iterates through each buyer
        for (int i = 0; i < buyers.size(); i++) {
            // checks if this sequence occurs for that buyer and
            // adds the price to the sum of bananas if it does occur
            if (mapPerBuyer.get(i).containsKey(sequence)) {
                sumOfBananas += mapPerBuyer.get(i).get(sequence);
            }
        }
        return sumOfBananas;
    }

    public static void main(String[] args) {
        Day22Part2 d22p2 = new Day22Part2();
        d22p2.processInput();
    }
}
