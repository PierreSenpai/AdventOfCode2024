package day21;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private Map<String, String> cache = new HashMap<>();
    private Map<List<Object>, Long> numCache = new HashMap<>();

    Day21Part2() {
        input = InputReader.readInputByLine("src/main/resources/day21.txt");
    }

    public void processInput() {
        long sum = 0;
        long start = System.currentTimeMillis();
        for (String code : input) {
            System.out.print(code + ": ");
            sum += complexityOf(code);
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        System.out.println(sum);
    }

    public long complexityOf(String code) {
        long length = shortestSequence(code);
        int numericPart = Integer.parseInt(code.substring(0, code.length() - 1));
        System.out.println(length);
        return length * numericPart;
    }

    public long shortestSequence(String code) {
        // sequences to control the first robot
        Set<String> sequencesFor1 = findNextSequence(code, numKeypad);
        // sequences to control the second to 26th robot
        long leastPresses = Long.MAX_VALUE;
        for (String sequence : sequencesFor1) {
            long nPresses = function(sequence, 25);
            if (nPresses < leastPresses) {
                leastPresses = nPresses;
            }
        }
        return leastPresses;
    }

    public Set<String> findNextSequence(String sequence, Map<Character, List<Integer>> keypad) {
        // stores sequences, that all lead to the same button presses
        // (these different sequences are caused by either moving with horizontal
        //  or vertical preference)
        Set<String> sequences = new HashSet<>(Set.of(""));

        int currX = 0;
        int currY = 0;
        for (int i = 0; i < sequence.length(); i++) {
            Set<String> newSequences = new HashSet<>();
            // finds the next position for the arm
            char button = sequence.charAt(i);
            List<Integer> nextPos = keypad.get(button);
            // moves arm of the robot
            for (String aSequence : sequences) {
                // tries with horizontal preference
                newSequences.add(aSequence + moveArm(currX, currY, nextPos) + 'A');
                // tries with vertical preference
                // newSequences.add(aSequence + moveArm(currX, currY, nextPos, false, keypad) + 'A');
            }
            currX = nextPos.get(0);
            currY = nextPos.get(1);
            // updates the sequences
            sequences = newSequences;
        }
        // test
        // String best = findBestSequence(sequences);
        // cache.putIfAbsent(sequence, best);
        return sequences;
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

    public static void main(String[] args) {
        Day21Part2 d21p2 = new Day21Part2();
        d21p2.processInput();
    }

    // find sequence for the first robot on the numeric keypad normally
    // use caching for "subsequences"
    // create a function that determines the best sequence out of a list
    // (maybe check the manhattan distance for each move in the following sequence)
    // (-> can the next robot click the same buttons multiple times in a row
    // or does he have to switch a lot)
    public long function(String s, int nRobotsAfter) {
        if (nRobotsAfter == 0) {
            return s.length();
        }
        // System.out.println("robot: " + (25 - nRobotsAfter + 2));
        List<Object> key = List.of(s, nRobotsAfter);
        if (numCache.containsKey(key)) {
            // System.out.println("found in numCache");
            return numCache.get(key);
        }
        String original = s;
        long sum = 0;
        while (true) {
            if (s.equals("")) {
                break;
            }
            int startNextBlock = s.indexOf('A') + 1;
            String subsequence = s.substring(0, startNextBlock);
            String subsequenceForNext = function2(subsequence);
            sum += function(subsequenceForNext, nRobotsAfter - 1);
            s = s.substring(startNextBlock);
        }
        numCache.put(List.of(original, nRobotsAfter), sum);
        // if (nRobotsAfter > 15) {
        // System.out.println("finished robot: " + (25 - nRobotsAfter + 2));
        // }
        return sum;
    }

    public String function2(String sequence) {
        if (cache.containsKey(sequence)) {
            return cache.get(sequence);
        }
        // move arm stuff
        // -> returns a list of sequences

        Set<String> sequences = findNextSequence(sequence, dirKeypad);
        String best = findBestSequence(sequences);
        cache.put(sequence, best);
        // System.out.println(cache.size());
        return best;
    }

    public String findBestSequence(Set<String> sequences) {
        String bestSequence = "";
        int lowestManhattan = Integer.MAX_VALUE;
        for (String sequence : sequences) {
            int thisManhattan = totalManhattan(sequence);
            if (thisManhattan < lowestManhattan) {
                bestSequence = sequence;
                lowestManhattan = thisManhattan;
            }
        }
        return bestSequence;
    }

    public int totalManhattan(String sequence) {
        int total = 0;
        for (int i = 1; i < sequence.length(); i++) {
            char prevButton = sequence.charAt(i - 1);
            char followButton = sequence.charAt(i);
            total += findManhattan(prevButton, followButton);
        }
        // System.out.println(total);
        return total;
    }

    public int findManhattan(char button1, char button2) {
        int dx = dirKeypad.get(button2).get(0) - dirKeypad.get(button1).get(0);
        int dy = dirKeypad.get(button2).get(1) - dirKeypad.get(button1).get(1);
        return Math.abs(dx) + Math.abs(dy);
    }
}
