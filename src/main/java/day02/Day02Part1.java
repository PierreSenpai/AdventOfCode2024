package day02;

import java.util.List;
import java.lang.Math;

import util.InputReader;

public class Day02Part1 {
    private List<String> reports;

    Day02Part1() {
        reports = InputReader.readInputByLine("src/main/resources/day02.txt");
    }

    public int checkReport(String report) {
        boolean safe = true;
        boolean undetermined = true; // saves if increase or decrease is already determined
        boolean increasing = true; // stores true for increasing values, false for decreasing ones

        String[] levels = report.split(" ");
        for (int i = 1; i < levels.length; i++) {
            int before = Integer.valueOf(levels[i-1]);
            int current = Integer.valueOf(levels[i]);

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

    public void checkAllReports() {
        int sum = 0;
        for (String report : reports) {
            sum += checkReport(report);
        }
        System.out.println(sum);
    }

    public static void main(String[] args) {
        Day02Part1 d2p1 = new Day02Part1();
        d2p1.checkAllReports();;
    }
}
