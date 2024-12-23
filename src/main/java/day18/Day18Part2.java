package day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.InputReader;

public class Day18Part2 {
    private final int maxX = 71;
    private final int maxY = 71;
    private char[][] map = new char[maxX][maxY];
    private List<List<Integer>> fallingBytes = new ArrayList<>();
    private HashMap<List<Integer>, Integer> lowestMovesAt = new HashMap<>();
    private int lowestMovesToEnd;

    Day18Part2() {
        for (String aByte : InputReader.readInputByLine("src/main/resources/day18.txt")) {
            String[] coords = aByte.split(",");
            fallingBytes.add(Stream.of(coords)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList()));
        }
    }

    public void processInput() {
        fillMap('.');

        final int nBytesFalling = 1024;
        letBytesFall(nBytesFalling);

        for (int i = nBytesFalling + 1; i < fallingBytes.size(); i++) {
            System.out.println(i);
            letByteFall(i);
            lowestMovesAt = new HashMap<>();
            lowestMovesToEnd = 0;
            findPath();
            // checks if no path was found
            if (lowestMovesToEnd == 0) {
                System.out.println(fallingBytes.get(i));
                return;
            }
        }
    }

    public void fillMap(char c) {
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                map[x][y] = c;
            }
        }
    }

    public void letBytesFall(int amount) {
        for (int i = 0; i < amount; i++) {
            List<Integer> coords = fallingBytes.get(i);
            map[coords.get(0)][coords.get(1)] = '#';
        }
    }

    public void letByteFall(int index) {
        // returns true, if the previous path isn't blocked
        List<Integer> coords = fallingBytes.get(index);
        map[coords.get(0)][coords.get(1)] = '#';
    }

    public void findPath() {
        findPath(0, 0, 0);
    }

    public boolean findPath(int x, int y, int movesMade) {
        // using booleans to stop the whole recursion once one path was found since
        // we don't need to find the shortest one now

        // stops if a shorter path to that position was already taken
        List<Integer> currCoords = List.of(x, y);
        if (lowestMovesAt.get(currCoords) != null &&
                lowestMovesAt.get(currCoords) <= movesMade) {
            return false;
        } else {
            lowestMovesAt.put(currCoords, movesMade);
        }
        // stops successfully when the end position is reached
        if (x == maxX - 1 && y == maxY - 1) {
            lowestMovesToEnd = movesMade;
            return true;
        }

        // check down
        if (checkAdjacent(x, y + 1)) {
            if (findPath(x, y + 1, movesMade + 1)) {
                return true;
            }
        }
        // check right
        if (checkAdjacent(x + 1, y)) {
            if (findPath(x + 1, y, movesMade + 1)) {
                return true;
            }
        }
        // check up
        if (checkAdjacent(x, y - 1)) {
            if (findPath(x, y - 1, movesMade + 1)) {
                return true;
            }
        }
        // check left
        if (checkAdjacent(x - 1, y)) {
            if (findPath(x - 1, y, movesMade + 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAdjacent(int x, int y) {
        if (x >= 0 && x < maxX && y >= 0 && y < maxY && map[x][y] == '.') {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Day18Part2 d18p2 = new Day18Part2();
        d18p2.processInput();;
    }
}
