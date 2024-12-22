package day15;

import java.util.List;
import java.util.Map;

import util.InputReader;

public class Day15Part2 {
private char[][] map;
    private int nX, nY, robotX, robotY;
    private String instructions = "";
    private Map<Character, List<Integer>> directions = Map.of(
            '^', List.of(0, -1),
            '>', List.of(1, 0),
            'v', List.of(0, 1),
            '<', List.of(-1, 0));

    Day15Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day15.txt");
        int split = input.indexOf("");

        List<String> inputMap = input.subList(0, split);
        nX = 2 * inputMap.getFirst().length();
        nY = inputMap.size();
        map = new char[nX][nY];

        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX / 2; x++) {
                char c = inputMap.get(y).charAt(x);;
                if (c == '@') {
                    map[2 * x][y] = c;
                    map[2 * x + 1][y] = '.';
                    continue;
                }
                if (c == 'O') {
                    map[2 * x][y] = '[';
                    map[2 * x + 1][y] = ']';
                    continue;
                }
                // wall or free space
                map[2 * x][y] = c;
                map[2 * x + 1][y] = c;
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

            System.out.println(i + ": "+instructions.charAt(i));
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
        if (map[adjX][adjY] == '[' || map[adjX][adjY] == ']') {
            // looks further until no more box is present
            // -> coords of the position right after the last box
            while (map[adjX][adjY] == '[' || map[adjX][adjY] == ']') {
                adjX += dx;
                adjY += dy;
            }
            // no space after row of boxes
            if (map[adjX][adjY] == '#')
                return;

            if (tryMoveBox(robotX + dx, robotY + dy, dir))
                moveRobot(dir);
            return;
        }
        // in front of free space
        moveRobot(dir);
    }

    public boolean tryMoveBox(int boxX, int boxY, char dir) {
        if (dir == '^' || dir == 'v') {
            // when moving up/down, you have to look for walls, that aren't
            // on the same y as the robot but still obstruct the way
            if (!checkUpDownObstruction(boxX, boxY, dir)) {
                return false;
            }
        }
        moveBox(boxX, boxY, dir);
        return true;
    }

    public boolean checkUpDownObstruction(int boxX, int boxY, char dir) { // recursive
        // returns false, if at least one wall obstructs the way
        if (map[boxX][boxY] == '.')
            return true;

        int dy = directions.get(dir).get(1);
        int leftX, rightX;
        // finding both parts of the box
        if (map[boxX][boxY] == '[') {
            leftX = boxX;
            rightX = boxX + 1;
        } else {
            rightX = boxX;
            leftX = boxX - 1;
        }

        if (map[leftX][boxY + dy] == '#' || map[rightX][boxY + dy] == '#')
            return false;
        if (map[leftX][boxY + dy] == '.' && map[rightX][boxY + dy] == '.')
            return true;
        return checkUpDownObstruction(leftX, boxY + dy, dir) &&
            checkUpDownObstruction(rightX, boxY + dy, dir);
    }

    public void moveBox(int boxX, int boxY, char dir) { // recursive
        // checks if that part of a box was already moved
        // or if the end of the row of boxes was reached
        if (map[boxX][boxY] == '.')
            return;
        int leftX, rightX;
        // finding both parts of the box
        if (map[boxX][boxY] == '[') {
            leftX = boxX;
            rightX = boxX + 1;
        } else {
            rightX = boxX;
            leftX = boxX - 1;
        }

        int dx = directions.get(dir).get(0);
        int dy = directions.get(dir).get(1);
        // when moving to the right, the right part must move first
        // in the other directions, the left part can move first
        if (dir == '>') {
            // checks for box right behind current box
            if (map[rightX + dx][boxY] == '[')
                moveBox(rightX + dx, boxY, dir);
            move(rightX, boxY, dir);
            move(leftX, boxY, dir);
            return;
        }
        if (dir == '<') {
            // checks for box right behind current box
            if (map[leftX + dx][boxY] == ']')
                moveBox(leftX + dx, boxY, dir);
            move(leftX, boxY, dir);
            move(rightX, boxY, dir);
            return;
        }
        // when moving up and down, boxes are also moved when
        // only half of it is above the current box
        if (map[leftX][boxY + dy] == '[' || map[leftX][boxY + dy] == ']')
            moveBox(leftX, boxY + dy, dir);
        if (map[rightX][boxY + dy] == '[' || map[rightX][boxY + dy] == ']')
            moveBox(rightX, boxY + dy, dir);
        move(leftX, boxY, dir);
        move(rightX, boxY, dir);
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
                if (map[x][y] == '[')
                    sum += 100 * y + x;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Day15Part2 d15p2 = new Day15Part2();
        d15p2.processInput();
    }
}
