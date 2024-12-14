package day08;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import util.InputReader;

public class Day08Part1 {
    private int nRows, nCols;
    private char[][] map;
    private List<Character> frequencies = new ArrayList<>();
    private List<int[]> antennaCoords;
    private Set<List<Integer>> antinodes = new HashSet<>();

    Day08Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day08.txt");
        nRows = input.size();
        nCols = input.get(0).length();
        map = new char[nRows][nCols];
        // presenting map as 2D array of chars
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                map[row][col] = input.get(row).charAt(col);
            }
        }
    }

    public void processInput() {
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                char c = map[row][col];
                // skips positions without antennas/with already checked antennas
                if (c == '.' || frequencies.contains(c)) continue;

                frequencies.add(c);
                findAll(c);

                checkAllPairs();
            }
        }
        System.out.println(antinodes.size());
    }

    public void findAll(char c) {
        antennaCoords = new ArrayList<>();
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                if (map[row][col] == c) {
                    antennaCoords.add(new int[] { row, col });
                }
            }
        }
    }

    public void checkAllPairs() {
        // going through all possible antenna pairs
        int i = 0;
        for (int[] coords1 : antennaCoords) {
            // only pairing with coords coming after current coords to avoid
            // duplicate pairing or pairing with itself
            i++;
            for (int[] coords2 : antennaCoords.subList(i, antennaCoords.size())) {
                int dRow = coords2[0] - coords1[0]; // vertical change (dy) (-> rows)
                int dCol = coords2[1] - coords1[1]; // horizontal change (dx) (-> cols)

                // adding change to coords2 / substracting change from coords1
                // -> distance to other antenna is twice the change
                // -> position of an antinode
                markAntinode(coords2[0] + dRow, coords2[1] + dCol);
                markAntinode(coords1[0] - dRow, coords1[1] - dCol);
            }
        }
    }

    public void markAntinode(int row, int col) {
        if (isInsideMap(row, col)) {
            antinodes.add(Arrays.asList(row, col));
        }
    }

    public boolean isInsideMap(int row, int col) {
        if (0 <= row && row < nRows &&
            0 <= col && col < nCols) {
                return true;
            }
        return false;
    }

    public static void main(String[] args) {
        Day08Part1 d8p1 = new Day08Part1();
        d8p1.processInput();
    }
}
