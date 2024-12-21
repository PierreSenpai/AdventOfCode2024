package day14;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.InputReader;

public class Day14Part2 {
    // both max values are excluded, as counting starts with 0
    private int maxX = 101;
    private int maxY = 103;
    private char[][] map= new char[maxX][maxY];
    private List<String> input;

    private int secondsPassed = 0;

    private List<List<Integer>> checkedPositions = new ArrayList<>();
    private int adjacentRobots;

    Day14Part2() {
        input = InputReader.readInputByLine("src/main/resources/day14.txt");
    }

    public void processInput() {
        // tries with increasing amount of seconds until a tree is found
        outerloop:
        while (true) {
            resetMap();
            checkedPositions.clear();
            // constructs map
            for (String robot : input) {
                processRobot(robot);
            }
            // looks for big group of adjacent robots
            for (int y = 0; y < maxY; y++) {
                for (int x = 0; x < maxX; x++) {
                    adjacentRobots = 0;
                    if (map[x][y] == '.')
                        continue;
                    if (checkedPositions.contains(List.of(x, y)))
                        continue;
                    findAdjacent(x, y);
                    // stops if enough (~50) adjacent robots were found
                    if (adjacentRobots >= 50) {
                        break outerloop;
                    }
                }
            }
            // visualize progress
            System.out.print(secondsPassed++ + " seconds passed\r");
        }
        // printing final result
        System.out.println("---------------after " + secondsPassed + " seconds---------------");
        printMap();
    }

    public void findAdjacent(int x, int y) {
        if (checkedPositions.contains(List.of(x, y)))
            return;
        adjacentRobots++;
        checkedPositions.add(List.of(x, y));
        // stops looking for more robots if enough (~50) were found
        if (adjacentRobots >= 50)
            return;
        // check up
        if (isRobot(x, y - 1))
            findAdjacent(x, y - 1);
        // check down
        if (isRobot(x, y + 1))
            findAdjacent(x, y + 1);
        // check left
        if (isRobot(x - 1, y))
            findAdjacent(x - 1, y);
        // check right
        if (isRobot(x + 1, y))
            findAdjacent(x + 1, y);
    }

    public boolean isRobot(int x, int y) {
        if (x < 0 || x >= maxX || y < 0 || y >= maxY)
            return false;
        if (map[x][y] == '#')
            return true;
        return false;
    }

    public void processRobot(String robot) {
        String pos = robot.substring(2, robot.indexOf(" "));
        String vel = robot.substring(robot.indexOf("v=") + 2);

        List<Integer> position = Stream.of(pos.split(","))
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        List<Integer> velocity = Stream.of(vel.split(","))
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        calculatePosition(position, velocity);
    }

    public void calculatePosition(List<Integer> pos, List<Integer> vel) {
        int x = pos.get(0);
        int y = pos.get(1);
        int dx = vel.get(0);
        int dy = vel.get(1);

        int newX = (secondsPassed * (maxX + dx) + x) % maxX; // adding maxX and maxY to
        int newY = (secondsPassed * (maxY + dy) + y) % maxY; // prevent negative coords

        map[newX][newY] = '#';
    }

    public void resetMap() {
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                map[x][y] = '.';
            }
        }
    }

    public void printMap() {
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                System.out.print(map[x][y]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Day14Part2 d14p2 = new Day14Part2();
        d14p2.processInput();
    }
}
