package day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.InputReader;

public class Day08Part2 {
    private int nRows, nCols;
    private char[][] map;
    private List<Character> frequencies = new ArrayList<>();
    private List<int[]> antennaCoords;
    private Set<List<Integer>> antinodes = new HashSet<>();

    Day08Part2() {
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

                // adding/substracting change leads to next position in line with the two antennas
                // (doing this with coords 2 or coords 1 leads to the same results)
                int currRow = coords1[0];
                int currCol = coords1[1];
                markAntinode(currRow, currCol); // don't forget position of the antenna itself
                while (isInsideMap(currRow + dRow, currCol + dCol)) {
                    currRow += dRow;
                    currCol += dCol;
                    markAntinode(currRow, currCol);
                }

                currRow = coords1[0];
                currCol = coords1[1];
                while (isInsideMap(currRow - dRow, currCol - dCol)) {
                    currRow -= dRow;
                    currCol -= dCol;
                    markAntinode(currRow, currCol);
                }
            }
        }
    }

    public void markAntinode(int row, int col) {
        // condition would be redundant (see part 1)
        antinodes.add(Arrays.asList(row, col));
    }

    public boolean isInsideMap(int row, int col) {
        if (0 <= row && row < nRows &&
            0 <= col && col < nCols) {
                return true;
            }
        return false;
    }

    public static void main(String[] args) {
        Day08Part2 d8p2 = new Day08Part2();
        d8p2.processInput();
    }
}
