package day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.InputReader;

public class Day18Part1 {
    private final int maxX = 71;
    private final int maxY = 71;
    private char[][] map = new char[maxX][maxY];
    private List<List<Integer>> fallingBytes = new ArrayList<>();
    private HashMap<List<Integer>, Integer> lowestMovesAt = new HashMap<>();
    private int lowestMovesToEnd;

    Day18Part1() {
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

        findPath();
        System.out.println(lowestMovesToEnd);
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

    public void findPath() {
        findPath(0, 0, 0);
    }

    public void findPath(int x, int y, int movesMade) {
        // stops if a shorter path to that position was already taken
        List<Integer> currCoords = List.of(x, y);
        if (lowestMovesAt.get(currCoords) != null &&
        lowestMovesAt.get(currCoords) <= movesMade) {
            return;
        } else {
            lowestMovesAt.put(currCoords, movesMade);
        }
        if (x == maxX - 1 && y == maxY - 1) {
            lowestMovesToEnd = movesMade;
            return;
        }
        // check up
        if (checkAdjacent(x, y - 1)) {
            findPath(x, y - 1, movesMade + 1);
        }
        // check down
        if (checkAdjacent(x, y + 1)) {
            findPath(x, y + 1, movesMade + 1);
        }
        // check left
        if (checkAdjacent(x - 1, y)) {
            findPath(x - 1, y, movesMade + 1);
        }
        // check right
        if (checkAdjacent(x + 1, y)) {
            findPath(x + 1, y, movesMade + 1);
        }
    }

    public boolean checkAdjacent(int x, int y) {
        if (x >= 0 && x < maxX && y >= 0 && y < maxY && map[x][y] == '.') {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Day18Part1 d18p1 = new Day18Part1();
        d18p1.processInput();;
    }
}
