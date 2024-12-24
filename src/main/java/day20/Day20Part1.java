package day20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import util.InputReader;

public class Day20Part1 {
    private char[][] map;
    private int maxX, maxY, startX, startY, endX, endY;
    // stores how many picoseconds a cheat should at least save
    private final int minimumTimeSaved = 100;
    // stores the time passed, once a position is reached in a standard run
    private HashMap<List<Integer>, Integer> standardPicosAt = new HashMap<>();
    private HashSet<List<Integer>> goodEnoughCheats = new HashSet<>();

    Day20Part1() {
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
        // checks for each wall that doesn't belong to the outside border
        // if cheating there saves enough time
        for (int y = 1; y < maxY - 1; y++) {
            for (int x = 1; x < maxX - 1; x++) {
                if (map[x][y] == '#') {
                    tryCheat(x, y);
                }
            }
        }
        System.out.println(goodEnoughCheats.size());
    }

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
            if (checkAdjacent(x, y - 1) && dir != 'v') {
                y--;
                dir = '^';
                movesMade++;
                continue;
            }
            // check down
            if (checkAdjacent(x, y + 1) && dir != '^') {
                y++;
                dir = 'v';
                movesMade++;
                continue;
            }
            // check left
            if (checkAdjacent(x - 1, y) && dir != '>') {
                x--;
                dir = '<';
                movesMade++;
                continue;
            }
            // check right
            if (checkAdjacent(x + 1, y) && dir != '<') {
                x++;
                dir = '>';
                movesMade++;
            }
        }
        standardPicosAt.put(List.of(x, y), movesMade);
    }

    public boolean checkAdjacent(int x, int y) {
        if (x < 0 || x >= maxX && y < 0 && y >= maxY) {
            return false;
        }
        if (map[x][y] == '#') {
            return false;
        }
        return true;
    }

    public void tryCheat(int x, int y) {
        List<Integer> picosWhenAdjacent = new ArrayList<>();
        // check if the wall has at least to adjacent free spaces
        if (adjacentSpaces(x, y, picosWhenAdjacent) < 2) {
            return;
        }
        // sort the 'adjacent seconds'
        picosWhenAdjacent.sort(null);
        // determines if the cheat is good enough by looking at the difference
        // of the highest and lowest adjacent seconds
        // (the "- 2" account for the to steps that perform the cheat)
        if (picosWhenAdjacent.getLast() - picosWhenAdjacent.getFirst() - 2 >= minimumTimeSaved) {
            goodEnoughCheats.add(List.of(x, y));
        }
    }

    public int adjacentSpaces(int x, int y, List<Integer> list) {
        // returns how many adjacent free spaces the wall has and stores
        // the seconds passed when arriving at them in the given list
        int adjSpaces = 0;
        // check up
        if (checkAdjacent(x, y - 1)) {
            list.add(standardPicosAt.get(List.of(x, y - 1)));
            adjSpaces++;
        }
        // check down
        if (checkAdjacent(x, y + 1)) {
            list.add(standardPicosAt.get(List.of(x, y + 1)));
            adjSpaces++;
        }
        // check left
        if (checkAdjacent(x - 1, y)) {
            list.add(standardPicosAt.get(List.of(x - 1, y)));
            adjSpaces++;
        }
        // check right
        if (checkAdjacent(x + 1, y)) {
            list.add(standardPicosAt.get(List.of(x + 1, y)));
            adjSpaces++;
        }
        return adjSpaces;
    }

    public static void main(String[] args) {
        Day20Part1 d20p1 = new Day20Part1();
        d20p1.processInput();
    }
}
