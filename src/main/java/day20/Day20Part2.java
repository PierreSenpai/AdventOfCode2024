package day20;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import util.InputReader;

public class Day20Part2 {
    private char[][] map;
    private int maxX, maxY, startX, startY, endX, endY;
    // stores how many picoseconds a cheat should at least save
    private final int minimumTimeSaved = 100;
    // stores the time passed, once a position is reached in a standard run
    private HashMap<List<Integer>, Integer> standardPicosAt = new HashMap<>();
    private HashSet<List<Integer>> goodEnoughCheats = new HashSet<>();
    private final int maxPicosPerCheat = 20;

    Day20Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day20.txt");
        maxX = input.getFirst().length();
        maxY = input.size();
        map = new char[maxX][maxY];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                map[x][y] = input.get(y).charAt(x);
            }
        }
    }

    public void processInput() {
        findStartAndEnd();
        // find the time it takes to reach the end without any cheats
        findShortestPath();

        // goes through all tiles of the path
        for (List<Integer> coords : standardPicosAt.keySet()) {
            int x = coords.get(0);
            int y = coords.get(1);
            findCheats(x, y);
        }
        System.out.println(goodEnoughCheats.size());
    }

    public void findCheats(int x, int y) {
        // tries for all possible positions around the current tile that
        // can be reached withing the duration of a cheat
        for (int dy = -maxPicosPerCheat; dy <= maxPicosPerCheat; dy++) {
            for (int dx = -maxPicosPerCheat; dx <= maxPicosPerCheat; dx++) {
                // checks Manhattan distance
                if (Math.abs(dx) + Math.abs(dy) > maxPicosPerCheat) {
                    continue;
                }
                if (!checkIfPath(x + dx, y + dy)) {
                    continue;
                }
                // checks if enough time was saved
                int newTime = standardPicosAt.get(List.of(x, y)) + Math.abs(dx) + Math.abs(dy);
                int previousTime = standardPicosAt.get(List.of(x + dx, y + dy));
                if (previousTime - newTime >= minimumTimeSaved) {
                    goodEnoughCheats.add(List.of(x, y, x + dx, y + dy));
                }
            }
        }
    }

    // rest unchanged except renaming checkAdjacent() to checkIfPath()

    public void findStartAndEnd() {
        boolean foundStart = false;
        boolean foundEnd = false;
        search: for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (foundStart && foundEnd) {
                    break search;
                }
                if (!foundStart && map[x][y] == 'S') {
                    startX = x;
                    startY = y;
                    foundStart = true;
                    continue;
                }
                if (!foundEnd && map[x][y] == 'E') {
                    endX = x;
                    endY = y;
                    foundEnd = true;
                }
            }
        }
    }

    public void findShortestPath() {
        int x = startX;
        int y = startY;
        int movesMade = 0;
        char dir = '?';
        // conditions include the opposite direction to prohibit moving backwards
        while (x != endX || y != endY) {
            standardPicosAt.put(List.of(x, y), movesMade);
            // check up
            if (checkIfPath(x, y - 1) && dir != 'v') {
                y--;
                dir = '^';
                movesMade++;
                continue;
            }
            // check down
            if (checkIfPath(x, y + 1) && dir != '^') {
                y++;
                dir = 'v';
                movesMade++;
                continue;
            }
            // check left
            if (checkIfPath(x - 1, y) && dir != '>') {
                x--;
                dir = '<';
                movesMade++;
                continue;
            }
            // check right
            if (checkIfPath(x + 1, y) && dir != '<') {
                x++;
                dir = '>';
                movesMade++;
            }
        }
        standardPicosAt.put(List.of(x, y), movesMade);
    }

    public boolean checkIfPath(int x, int y) {
        if (x < 0 || x >= maxX || y < 0 || y >= maxY) {
            return false;
        }
        if (map[x][y] == '#') {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Day20Part2 d20p2 = new Day20Part2();
        d20p2.processInput();
    }
}
