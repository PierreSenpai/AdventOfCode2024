package day15;

import java.util.List;
import java.util.Map;

import util.InputReader;

public class Day15Part1 {
    private char[][] map;
    private int nX, nY, robotX, robotY;
    private String instructions = "";
    private Map<Character, List<Integer>> directions = Map.of(
            '^', List.of(0, -1),
            '>', List.of(1, 0),
            'v', List.of(0, 1),
            '<', List.of(-1, 0));

    Day15Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day15.txt");
        int split = input.indexOf("");

        List<String> inputMap = input.subList(0, split);
        nX = inputMap.getFirst().length();
        nY = inputMap.size();
        map = new char[nX][nY];

        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                map[x][y] = inputMap.get(y).charAt(x);
            }
        }

        List<String> instrs = input.subList(split + 1, input.size());
        // concatenate instructions to one string
        for (String instr : instrs)
            instructions += instr;
    }

    public void processInput() {
        findRobot();

        for (int i = 0; i < instructions.length(); i++) {
            performInstruction(instructions.charAt(i));
        }
        System.out.println(sumGPS());
    }

    public void findRobot() {
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                if (map[x][y] == '@') {
                    robotX = x;
                    robotY = y;
                    return;
                }
            }
        }
    }

    public void performInstruction(char dir) {
        int dx = directions.get(dir).get(0);
        int dy = directions.get(dir).get(1);
        int adjX = robotX + dx;
        int adjY = robotY + dy;

        // in front of wall
        if (map[adjX][adjY] == '#')
            return;
        // in front of box
        if (map[adjX][adjY] == 'O') {
            // looks further until no more box is present
            // -> coords of the position right after the last box
            while (map[adjX += dx][adjY += dy] == 'O') {
            }
            // no space after row of boxes
            if (map[adjX][adjY] == '#')
                return;

            // moves all boxes from last to first by moving back
            // until the position right in front of the robot
            // (go back first as it starts with the coords after the last box)
            adjX -= dx;
            adjY -= dy;
            while ((adjX != robotX) || (adjY != robotY)) {
                moveBox(adjX, adjY, dir);
                adjX -= dx;
                adjY -= dy;
            }
            moveRobot(dir);
            return;
        }
        // in front of free space
        moveRobot(dir);
    }

    public void moveBox(int boxX, int boxY, char dir) {
        move(boxX, boxY, dir);
    }

    public void moveRobot(char dir) {
        move(robotX, robotY, dir);
        robotX += directions.get(dir).get(0);
        robotY += directions.get(dir).get(1);
    }

    public void move(int x, int y, char dir) {
        int dx = directions.get(dir).get(0);
        int dy = directions.get(dir).get(1);
        map[x + dx][y + dy] = map[x][y];
        map[x][y] = '.';
    }

    public int sumGPS() {
        int sum = 0;
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                if (map[x][y] == 'O')
                    sum += 100 * y + x;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Day15Part1 d15p1 = new Day15Part1();
        d15p1.processInput();
    }
}
