package day10;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import util.InputReader;

public class Day10Part1 {
    private int[][] map;
    // stores coordinate pairs for each 0 on the map
    private List<int[]> pathStarts = new ArrayList<>();
    private HashSet<List<Integer>> visited9s;

    Day10Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day10.txt");

        int height = input.size();
        int width = input.getFirst().length();
        map = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = Character.getNumericValue(input.get(y).charAt(x));
            }
        }
    }

    public void processInput() {
        findStarts();
        int sum = 0;
        for (int[] start : pathStarts) {
            calculateScore(start[0], start[1]);
            sum += visited9s.size();
        }
        System.out.println(sum);
    }

    public void findStarts() {
        // iterates through each x/y location and store pair when it's a path start (0)
        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (map[x][y] == 0)
                    pathStarts.add(new int[] { x, y });
            }
        }
    }

    public void calculateScore(int x, int y) {
        visited9s = new HashSet<>();
        calculateScore(x, y, 0);
    }

    public void calculateScore(int x, int y, int value) {
        if (value == 9)
            visited9s.add(List.of(x, y));

        // checks for each direction if the adjacent value is greater by 1
        // if that's the case, it performs the same on the next tile until it reaches 9

        if (x + 1 < map.length && map[x + 1][y] == value + 1)
            calculateScore(x + 1, y, value + 1);

        if (y + 1 < map[x].length && map[x][y + 1] == value + 1)
            calculateScore(x, y + 1, value + 1);

        if (x - 1 >= 0 && map[x - 1][y] == value + 1)
            calculateScore(x - 1, y, value + 1);

        if (y - 1 >= 0 && map[x][y - 1] == value + 1)
            calculateScore(x, y - 1, value + 1);
    }

    public static void main(String[] args) {
        Day10Part1 d10p1 = new Day10Part1();
        d10p1.processInput();
    }
}
