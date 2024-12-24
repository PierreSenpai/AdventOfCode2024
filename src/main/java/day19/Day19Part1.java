package day19;

import java.util.Arrays;
import java.util.List;

import util.InputReader;

public class Day19Part1 {
    List<String> towels;
    List<String> patterns;

    Day19Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day19.txt");
        towels = Arrays.asList(input.getFirst().split(",\s"));

        patterns = input.subList(2, input.size());
    }

    public void processInput() {
        int sum = 0;
        for (String pattern : patterns) {
            sum += checkPattern(pattern);
        }
        System.out.println(sum);
    }

    public int checkPattern(String pattern) {
        if (checkPattern(pattern, 0)) {
            return 1;
        }
        return 0;
    }

    public boolean checkPattern(String pattern, int startSubstr) {
        // checks if the pattern was already completely processed
        if (startSubstr >= pattern.length()) {
            return true;
        }

        String patternLeft = pattern.substring(startSubstr);
        for (String towel : towels) {
            if (patternLeft.startsWith(towel) &&
                    checkPattern(pattern, startSubstr + towel.length())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Day19Part1 d19p1 = new Day19Part1();
        d19p1.processInput();
    }
}
