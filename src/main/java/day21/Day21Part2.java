package day21;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.InputReader;

public class Day21Part2 {
    private List<String> input;
    // setting the starting button (A) as 0,0
    // -> gap is always -2,0
    private Map<Character, List<Integer>> numKeypad = Map.ofEntries(
            Map.entry('7', List.of(-2, -3)),
            Map.entry('8', List.of(-1, -3)),
            Map.entry('9', List.of( 0, -3)),
            Map.entry('4', List.of(-2, -2)),
            Map.entry('5', List.of(-1, -2)),
            Map.entry('6', List.of( 0, -2)),
            Map.entry('1', List.of(-2, -1)),
            Map.entry('2', List.of(-1, -1)),
            Map.entry('3', List.of( 0, -1)),
            // gap
            Map.entry('0', List.of(-1,  0)),
            Map.entry('A', List.of( 0,  0)));

    private Map<Character, List<Integer>> dirKeypad = Map.of(
                                 '^', List.of(-1, 0), 'A', List.of(0, 0),
            '<', List.of(-2, 1), 'v', List.of(-1, 1), '>', List.of(0, 1));

    // stores the shortest nextSequence to avoid recomputing it every time
    private Map<String, String> nextSeqCache = new HashMap<>();
    // stores the final length for each pair of subsequence and remaining number of robots
    private Map<List<Object>, Long> lengthCache = new HashMap<>();

    Day21Part2() {
        input = InputReader.readInputByLine("src/main/resources/day21.txt");
    }

    public void processInput() {
        long sum = 0;
        for (String code : input) {
            System.out.print(code + ": ");
            sum += complexityOf(code);
        }
        System.out.println(sum);
    }

    public long complexityOf(String code) {
        long length = shortestSequence(code);
        int numericPart = Integer.parseInt(code.substring(0, code.length() - 1));
        System.out.println(length);
        return length * numericPart;
    }

    public long shortestSequence(String code) {
        // sequence to control the first robot
        String sequenceForFirst = findNextSequence(code, numKeypad);
        // calculates length of the sequence for the 26th robot
        return lengthFinalSequence(sequenceForFirst, 25);
    }

    public String findNextSequence(String sequence, Map<Character, List<Integer>> keypad) {
        String nextSequence = "";

        int currX = 0;
        int currY = 0;
        for (int i = 0; i < sequence.length(); i++) {
            // finds the next position for the arm
            char button = sequence.charAt(i);
            List<Integer> nextPos = keypad.get(button);
            // moves arm of the robot
            nextSequence += moveArm(currX, currY, nextPos) + 'A';
            currX = nextPos.get(0);
            currY = nextPos.get(1);
        }
        return nextSequence;
    }

    public String moveArm(int x, int y, List<Integer> newPos) {
        int dx = newPos.get(0) - x;
        int dy = newPos.get(1) - y;
        boolean horizontalFirst;
        // preference of the four directions
        // (due to distance from the A key on the directional keypad):
        // left > down > up = right
        if (dx < 0) { // moving left
            horizontalFirst = true;
        }
        else { // moving up/down is always prefered or equal to moving right
            horizontalFirst = false;
        }
        // checks if the preference of horizontal/vertical would let the arm be above
        // the gap and switches the prefence if that is the case
        if (horizontalFirst && isGap(x + dx, y)) {
            horizontalFirst = false;
        } else if (!horizontalFirst && isGap(x, y + dy)) {
            horizontalFirst = true;
        }

        if (horizontalFirst) {
            return moveHorizontally(dx) + moveVertically(dy);
        } else {
            return moveVertically(dy) + moveHorizontally(dx);
        }
    }

    public boolean isGap(int x, int y) {
        // when the position doesn't belong to any button of the keypad
        // it must be the gap
        if (x == -2 && y == 0) {
            return true;
        }
        return false;
    }

    public String moveVertically(int diff) {
        if (diff == 0) {
            return "";
        }
        String sequence = "";
        // -1 for going up, // +1 for going down
        int step = diff / Math.abs(diff);
        for (int i = 0; i < Math.abs(diff); i++) {
            if (step == -1) {
                sequence += '^';
            } else { // step == 1
                sequence += 'v';
            }
        }
        return sequence;
    }

    public String moveHorizontally(int diff) {
        if (diff == 0) {
            return "";
        }
        String sequence = "";
        // -1 for going left, // +1 for going right
        int step = diff / Math.abs(diff);
        for (int i = 0; i < Math.abs(diff); i++) {
            if (step == -1) {
                sequence += '<';
            } else { // step == 1
                sequence += '>';
            }
        }
        return sequence;
    }

    public long lengthFinalSequence(String sequence, int nRobotsAfter) {
        // if the last robot layer is reached, it returns the length of that final subsequence
        if (nRobotsAfter == 0) {
            return sequence.length();
        }
        // tries to avoid recomputation when identical function call already happened
        List<Object> key = List.of(sequence, nRobotsAfter);
        if (lengthCache.containsKey(key)) {
            return lengthCache.get(key);
        }
        String original = sequence;
        long sum = 0;
        // splits the sequence into units that end with 'A', since all of them
        // start and end on the button 'A'. without splitting, the strings would
        // get too long
        while (true) {
            if (sequence.equals("")) {
                break;
            }
            int startNextBlock = sequence.indexOf('A') + 1;
            String subsequence = sequence.substring(0, startNextBlock);
            // finds the matching nextSsequence to each subsequence and runs the same function
            // with that sequence -> this continues until the last "robot layer is reached"
            // and the lengths of all subsequences in the last layer are added together
            String subsequenceForNext = findNextSequence(subsequence);
            sum += lengthFinalSequence(subsequenceForNext, nRobotsAfter - 1);
            sequence = sequence.substring(startNextBlock);
        }
        // saves result for further function calls that are identical
        lengthCache.put(List.of(original, nRobotsAfter), sum);
        return sum;
    }

    public String findNextSequence(String sequence) {
        if (nextSeqCache.containsKey(sequence)) {
            return nextSeqCache.get(sequence);
        }
        String next = findNextSequence(sequence, dirKeypad);
        nextSeqCache.put(sequence, next);
        return next;
    }

    public static void main(String[] args) {
        Day21Part2 d21p2 = new Day21Part2();
        d21p2.processInput();
    }
}
