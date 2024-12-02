package day02;

import java.util.List;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

import util.InputReader;

public class Day02Part2 {
    private List<String> reports;

    Day02Part2() {
        reports = InputReader.readInputByLine("src/main/resources/day02.txt");
    }

    public int checkTry(List<String> levels) {
        boolean safe = true;
        boolean undetermined = true; // saves if increase or decrease is already determined
        boolean increasing = true; // stores true for increasing values, false for decreasing ones

        for (int i = 1; i < levels.size(); i++) {
            int before = Integer.valueOf(levels.get(i-1));
            int current = Integer.valueOf(levels.get(i));

            if (undetermined) {
                if (before < current) {increasing = true;}
                else {increasing = false;}
                undetermined = false;
            }
            int absDiff = Math.abs(before - current);
            if ((absDiff == 0 || absDiff > 3) || (increasing && before > current) || (!increasing && before < current)) {
                safe = false;
                break;
            }
        }

        if (safe) {return 1;}

        return 0;
    }

    public int checkReport(String report) {
        List<String> levels = Arrays.asList(report.split(" "));

        if (checkTry(levels) == 1) {return 1;}

        int i = 0;
        int sum = 0;
        while (sum < 1 && i < levels.size()) {
            List<String> copy = new ArrayList<>(levels);
            copy.remove(i);
            sum += checkTry(copy);
            i++;
        }
        return sum;
    }

    public void checkAllReports() {
        int sum = 0;
        for (String report : reports) {
            sum += checkReport(report);
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        Day02Part2 d2p2 = new Day02Part2();
        d2p2.checkAllReports();;
    }
    
}
