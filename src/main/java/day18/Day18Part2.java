package day18;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
    private HashSet<List<Integer>> previousPath = new HashSet<>();

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
            // only looks for a new path, when the previous one gets blocked
            if (letByteFall(i)) {
                continue;
            }
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

    public boolean letByteFall(int index) {
        // returns true, if the previous path isn't blocked
        List<Integer> coords = fallingBytes.get(index);
        map[coords.get(0)][coords.get(1)] = '#';
        if (previousPath.size() == 0 || previousPath.contains(coords)) {
            return false;
        }
        return true;
    }

    public void findPath() {
        findPath(0, 0, 0, new HashSet<>());
    }

    public void findPath(int x, int y, int movesMade, HashSet<List<Integer>> path) {
        // stops if a shorter path to that position was already taken
        List<Integer> currCoords = List.of(x, y);
        if (lowestMovesAt.get(currCoords) != null &&
                lowestMovesAt.get(currCoords) <= movesMade) {
            return;
        } else {
            lowestMovesAt.put(currCoords, movesMade);
            path.add(currCoords);
        }
        if (x == maxX - 1 && y == maxY - 1) {
            lowestMovesToEnd = movesMade;
            previousPath = path;
            return;
        }
        // copies of the hashsets are made so the different function calls dont
        // interfere with each other

        // check up
        if (checkAdjacent(x, y - 1)) {
            findPath(x, y - 1, movesMade + 1, new HashSet<>(path));
        }
        // check down
        if (checkAdjacent(x, y + 1)) {
            findPath(x, y + 1, movesMade + 1, new HashSet<>(path));
        }
        // check left
        if (checkAdjacent(x - 1, y)) {
            findPath(x - 1, y, movesMade + 1, new HashSet<>(path));
        }
        // check right
        if (checkAdjacent(x + 1, y)) {
            // for one direction, you can keep the parameter path
            findPath(x + 1, y, movesMade + 1, path);
        }
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
