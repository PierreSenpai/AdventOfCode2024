package day21;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.InputReader;

public class Day21Part1 {
    private List<String> input;
    private Map<Character, List<Integer>> numKeypad = Map.ofEntries(
            Map.entry('7', List.of(0, 0)),
            Map.entry('8', List.of(1, 0)),
            Map.entry('9', List.of(2, 0)),
            Map.entry('4', List.of(0, 1)),
            Map.entry('5', List.of(1, 1)),
            Map.entry('6', List.of(2, 1)),
            Map.entry('1', List.of(0, 2)),
            Map.entry('2', List.of(1, 2)),
            Map.entry('3', List.of(2, 2)),
            // gap
            Map.entry('0', List.of(1, 3)),
            Map.entry('A', List.of(2, 3)));

    private Map<Character, List<Integer>> dirKeypad = Map.of(
            '^', List.of(1, 0), 'A', List.of(2, 0),
            '<', List.of(0, 1), 'v', List.of(1, 1), '>', List.of(2, 1));

    Day21Part1() {
        input = InputReader.readInputByLine("src/main/resources/day21.txt");
    }

    public void processInput() {
        int sum = 0;
        for (String code : input) {
            sum += complexityOf(code);
        }
        System.out.println(sum);
    }

    public int complexityOf(String code) {
        int length = shortestSequence(code).length();
        int numericPart = Integer.parseInt(code.substring(0, code.length() - 1));
        return length * numericPart;
    }

    public String shortestSequence(String code) {
        // sequences to control the first robot
        Set<String> sequencesFor1 = nextSequence(code, numKeypad);
        // sequences to control the second robot
        Set<String> sequencesFor2 = new HashSet<>();
        for (String sequence : sequencesFor1) {
            sequencesFor2.addAll(nextSequence(sequence, dirKeypad));
        }
        // sequences to control the third robot
        Set<String> sequencesFor3 = new HashSet<>();
        for (String sequence : sequencesFor2) {
            sequencesFor3.addAll(nextSequence(sequence, dirKeypad));
        }
        // find the shortest of these sequences
        return findShortest(sequencesFor3);
    }

    public Set<String> nextSequence(String sequence, Map<Character, List<Integer>> keypad) {
        // stores sequences, that all lead to the same button presses
        // (these different sequences are caused by either moving with horizontal
        //  or vertical preference)
        Set<String> sequences = new HashSet<>(Set.of(""));
        List<Integer> start = keypad.get('A');
        int currX = start.get(0);
        int currY = start.get(1);

        for (int i = 0; i < sequence.length(); i++) {
            Set<String> newSequences = new HashSet<>();
            // finds the next position for the arm
            char button = sequence.charAt(i);
            List<Integer> nextPos = keypad.get(button);
            // moves arm of the robot
            for (String aSequence : sequences) {
                // tries with horizontal preference
                newSequences.add(aSequence + moveArm(currX, currY, nextPos, true, keypad) + 'A');
                // tries with vertical preference
                newSequences.add(aSequence + moveArm(currX, currY, nextPos, false, keypad) + 'A');
            }
            currX = nextPos.get(0);
            currY = nextPos.get(1);
            // updates the sequences
            sequences = newSequences;
        }
        return sequences;
    }

    public String moveArm(int x, int y, List<Integer> newPos, boolean horFirst,
            Map<Character, List<Integer>> keypad) {
        int dx = newPos.get(0) - x;
        int dy = newPos.get(1) - y;
        String sequence = "";

        // checks if the preference of horizontal/vertical would let the arm be above
        // the gap and switches the prefence if that is the case
        if (horFirst && isGap(x + dx, y, keypad)) {
            horFirst = false;
        } else if (!horFirst && isGap(x, y + dy, keypad)) {
            horFirst = true;
        }

        if (horFirst) {
            sequence += moveHorizontally(dx);
            sequence += moveVertically(dy);
        } else {
            sequence += moveVertically(dy);
            sequence += moveHorizontally(dx);
        }
        return sequence;
    }

    public boolean isGap(int x, int y, Map<Character, List<Integer>> keypad) {
        // when the position doesn't belong to any button of the keypad
        // it must be the gap
        if (keypad.values().contains(List.of(x, y))) {
            return false;
        }
        return true;
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

    public String findShortest(Set<String> list) {
        String shortest = null;
        for (String str : list) {
            if (shortest == null) {
                shortest = str;
            }
            if (str.length() < shortest.length()) {
                shortest = str;
            }
        }
        return shortest;
    }

    public static void main(String[] args) {
        Day21Part1 d21p1 = new Day21Part1();
        d21p1.processInput();
    }
}
