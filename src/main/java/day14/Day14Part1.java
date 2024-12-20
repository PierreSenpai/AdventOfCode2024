package day14;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import util.InputReader;

public class Day14Part1 {
    // matches the coords of a position with the number of robots on that position
    private HashMap<List<Integer>, Integer> map = new HashMap<>();
    // both max values are excluded, as counting starts with 0
    private int maxX = 101;
    private int maxY = 103;
    private List<String> input;
    
    Day14Part1() {
        input = InputReader.readInputByLine("src/main/resources/day14.txt");
    }

    public void processInput() {
        for (String robot : input) {
            processRobot(robot);
        }
        int q1 = processQuadrant(1);
        int q2 = processQuadrant(2);
        int q3 = processQuadrant(3);
        int q4 = processQuadrant(4);

        System.out.println(q1 * q2 * q3 * q4);
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

        final int seconds = 100;

        int newX = (seconds * (maxX + dx) + x) % maxX; // adding maxX and maxY to
        int newY = (seconds * (maxY + dy) + y) % maxY; // prevent negative coords

        // sets value to 1 for the first bot (-> list doesn't contain this key already)
        // on that position and adds 1 for any additional bot
        map.merge(List.of(newX, newY), 1, (a, b) -> a + b);
    }

    public int processQuadrant(int n) {
        int middleX = maxX / 2;
        int middleY = maxY / 2;
        // naming the quadrants with 1 to 4 like in maths
        if (n == 1)
            return robotsInQuadrant(middleX + 1, maxX, 0, middleY);
        if (n == 2)
            return robotsInQuadrant(0, middleX, 0, middleY);
        if (n == 3)
            return robotsInQuadrant(0, middleX, middleY + 1, maxY);
        // n == 4
        return robotsInQuadrant(middleX + 1, maxX, middleY + 1, maxY);
    }

    public int robotsInQuadrant(int lowerX, int upperX, int lowerY, int upperY) {
        int nRobots = 0;
        for (int x = lowerX; x < upperX; x++) {
            for (int y = lowerY; y < upperY; y++) {
                if (map.containsKey(List.of(x, y)))
                    nRobots += map.get(List.of(x, y));
            }
        }
        return nRobots;
    }

    public static void main(String[] args) {
        Day14Part1 d14p1 = new Day14Part1();
        d14p1.processInput();
    }
}
