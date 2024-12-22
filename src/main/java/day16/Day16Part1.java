package day16;

import java.util.HashMap;
import java.util.List;

import util.InputReader;

public class Day16Part1 {
    private char[][] map;
    private int nX, nY;

    // indices: 0:right, 1:down, 2: left, 3: up
    private int[][] directions = new int[][] { { 1, 0 }, { 0, 1 }, { -1, 0 }, { 0, -1 } };
    // for each position that the reindeer visits, it stores x/y/dir/turnsLeft as
    // the key and the score as the value. if the position is visited again with
    // a higher score, the 'longer path' is blocked
    private HashMap<List<Integer>, Integer> minScoreAt = new HashMap<>();
    private int allowedTurns = 0;
    private int lowestScore = Integer.MAX_VALUE;

    Day16Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day16.txt");
        nX = input.get(0).length();
        nY = input.size();
        map = new char[nX][nY];

        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                map[x][y] = input.get(y).charAt(x);
            }
        }
    }

    public void processInput() {
        while (lowestScore == Integer.MAX_VALUE) {
            tryPaths();
            allowedTurns++;
        }
        System.out.println(lowestScore);
    }

    public void tryPaths() {
        // find starting position
        int startX = -1;
        int startY = -1;
        for (int y = 0; y < nY; y++) {
            for (int x = 0; x < nX; x++) {
                if (map[x][y] == 'S') {
                    startX = x;
                    startY = y;
                }
            }
        }
        // starts facing east -> dir = 0
        tryPath(startX, startY, 0, 0, 0);
    }

    public void tryPath(int x, int y, int turnsMade, int dir, int score) {
        if (allowedTurns - turnsMade < 0)
            return;

        int dx = directions[dir][0];
        int dy = directions[dir][1];

        int leftDir, leftX, leftY, rightDir, rightX, rightY;
        // initialize for the dead end check after the while loop
        leftDir = leftX = leftY = rightDir = rightX = rightY = -1;

        // moves straight until it hits a wall
        while (map[x][y] != '#') {
            // check if the end is reached
            if (map[x][y] == 'E') {
                if (score < lowestScore) {
                    lowestScore = score;
                    return;
                }
            }
            // prevents moving back the same path in went forward
            if (minScoreAt.containsKey(List.of(x, y, (dir + 2) % 4, turnsMade - 2))) {
                minScoreAt.put(List.of(x, y, dir, turnsMade), score);
                return;
            }

            // checks if a longer path was taken
            if (minScoreAt.get(List.of(x, y, dir, turnsMade)) != null &&
                    minScoreAt.get(List.of(x, y, dir, turnsMade)) < score) {
                // cancels this branch but doesn't block it, in case a branch with a lower
                // score passes this position the same way later on
                return;
            } else {
                minScoreAt.put(List.of(x, y, dir, turnsMade), score);
            }

            // checks left
            leftDir = (4 + dir - 1) % 4;
            leftX = x + directions[leftDir][0];
            leftY = y + directions[leftDir][1];
            if (map[leftX][leftY] == '.')
                tryPath(x, y, turnsMade + 1, leftDir, score + 1000);

            // checks right
            rightDir = (dir + 1) % 4;
            rightX = x + directions[rightDir][0];
            rightY = y + directions[rightDir][1];
            if (map[rightX][rightY] == '.')
                tryPath(x, y, turnsMade + 1, rightDir, score + 1000);

            // move straight
            x += dx;
            y += dy;
            score++;
        }
        // looks for dead end and fills it with '#'
        if (map[leftX][leftY] == '#' && map[rightX][rightY] == '#' && map[x - dx][y - dy] != 'S')
            map[x - dx][y - dy] = '#';
    }

    public static void main(String[] args) {
        Day16Part1 d16p1 = new Day16Part1();
        d16p1.processInput();
    }
}
