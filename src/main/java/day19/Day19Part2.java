package day19;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import util.InputReader;

public class Day19Part2 {
    List<String> towels;
    List<String> patterns;
    HashMap<String, Long> cache = new HashMap<>();

    Day19Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day19.txt");
        towels = Arrays.asList(input.getFirst().split(",\s"));

        patterns = input.subList(2, input.size());
    }

    public void processInput() {
        long sum = 0;
        for (String pattern : patterns) {
            sum += checkPattern(pattern);
        }
        System.out.println(sum);
    }

    public long checkPattern(String pattern) {
        return checkPattern(pattern, 0);
    }

    public long checkPattern(String pattern, int startSubstr) {
        long sum = 0;
        // checks if the pattern was already completely processed
        if (startSubstr >= pattern.length()) {
            return 1;
        }

        String patternLeft = pattern.substring(startSubstr);
        if (cache.containsKey(patternLeft)) {
            return cache.get(patternLeft);
        }
        for (String towel : towels) {
            if (patternLeft.startsWith(towel)) {
                sum += checkPattern(pattern, startSubstr + towel.length());
            }
        }
        cache.put(patternLeft, sum);
        return sum;
    }

    public static void main(String[] args) {
        Day19Part2 d19p2 = new Day19Part2();
        d19p2.processInput();
    }
}
