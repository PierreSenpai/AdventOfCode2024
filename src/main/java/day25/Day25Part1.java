package day25;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day25Part1 {
    private List<int[]> locks = new ArrayList<>();
    private List<int[]> keys = new ArrayList<>();
    private final int space = 5;
    private final int nCols = 5;

    Day25Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day25.txt");
        final int splitAfter = 7;
        while (true) {
            List<String> schematic = input.subList(0, splitAfter);
            if (schematic.get(0).charAt(0) == '#') {
                addLock(schematic);
            } else {
                addKey(schematic);
            }
            if (splitAfter == input.size()) {
                break;
            }
            input = input.subList(splitAfter + 1, input.size());
        }
    }

    public void addLock(List<String> schematic) {
        int[] lock = new int[nCols];
        for (int col = 0; col < nCols; col++) {
            int height = -1;
            for (int h = 0; h < space + 2; h++) {
                if (schematic.get(h).charAt(col) == '#') {
                    height = h;
                }
            }
            lock[col] = height;
        }
        locks.add(lock);
    }

    public void addKey(List<String> schematic) {
        int[] key = new int[nCols];
        for (int col = 0; col < nCols; col++) {
            int height = -1;
            for (int h = 0; h < space + 2; h++) {
                if (schematic.get(space + 1 - h).charAt(col) == '#') {
                    height = h;
                }
            }
            key[col] = height;
        }
        keys.add(key);
    }

    public void processInput() {
        int nPairs = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                nPairs += isPair(key, lock);
            }
        }
        System.out.println(nPairs);
    }

    public int isPair(int[] key, int[] lock) {
        // returns 1 if the lock and key fit together
        // else it returns 0
        for (int col = 0; col < nCols; col++) {
            if (key[col] + lock[col] > space) {
                return 0;
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Day25Part1 d25p1 = new Day25Part1();
        d25p1.processInput();
    }
}
