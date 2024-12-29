package day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import util.InputReader;

public class Day24Part2 {
    private Map<String, Integer> wires = new HashMap<>();
    private List<String[]> gates = new ArrayList<>();
    private long expectedResult;
    private long actualResult;
    // using a TreeSet, so that the wires are already in alphabetical order
    private Set<String> wrongZWires = new TreeSet<>();

    Day24Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day24.txt");
        // uncomment to test on corrected input
        // input = InputReader.readInputByLine("src/main/java/day24/corrected.txt");
        int split = input.indexOf("");
        List<String> inputWires = input.subList(0, split);
        for (String line : inputWires) {
            String[] wire = line.split(": ");
            wires.put(wire[0], Integer.parseInt(wire[1]));
        }
        List<String> inputGates = input.subList(split + 1, input.size());
        for (String line : inputGates) {
            // index 0: input1, index 1: operation, index 2: input2, index 4: output
            // (index 3 is unneccesary as it's just the arrow)
            gates.add(line.split(" "));
        }
    }

    public void processInput() {
        List<String[]> originalGates = gates;
        Map<String, Integer> originalWires = wires;
        // tests the device with different inputs
        // (i.e. the value of one of the 90 starting wires (45x 'x' and 45x 'y')
        //  is switched each time -> 9)
        for (String wire : originalWires.keySet()) {
            gates = new ArrayList<>(originalGates);
            wires = new HashMap<>(originalWires);
            switchWireValue(wire);
            testDevice();
        }
        // just to make sure, we also test it with the original input
        gates = new ArrayList<>(originalGates);
        wires = new HashMap<>(originalWires);
        testDevice();
        // prints out, which of the z wires were wrong in any of the tests
        System.out.println("wrong: " + wrongZWires);

        // based on this information, the correct swaps can be found quite easily
        // by using following scheme and using ctrl+f in the input file
        // (we start fixing the z wire with the lowest number, as an incorrect
        //  previous z wire often times leads to another wrong z wire right after)

        //                         "OR-wire"
        //                         of z(n-1)*              x(n-1) XOR y(n-1)
        //                             |                           |
        //        x(n-1) AND y(n-1)    +------------AND------------+
        //                |                          |
        // x(n) XOR y(n)  +------------OR------------+
        //       |                     |
        //       +---------XOR---------+ <-- *this wire is the
        //                  |                 "OR-wire" of z(n)
        //                 z(n)

        // (of course left/right can be swapped at each intersection)

        // additionally, it's helpful to keep track of the "OR-wires" of
        // previous z wires, e.g. z02: rpn, z03: qpp, z04: pvg, ...

        // when using this scheme, we start from the top and progress downwards
        // until we reach a mismatching spot, look for the correct alternative
        // and swap the two wires
        // afterwards we run the program again to find the remaining incorrect z wires

        // this leads to the following solution
        List<String> correctSwaps = new ArrayList<>(
                List.of("fhc", "z06", "qhj", "z11", "mwh", "ggt", "hqk", "z35"));
        correctSwaps.sort(null);
        System.out.println("solution:\n" + String.join(",", correctSwaps));
    }

    public void testDevice() {
        // finds the expected result
        long numberX = binaryNumberOf("x");
        long numberY = binaryNumberOf("y");
        expectedResult = numberX + numberY;
        // computes the actual result and compares it to the expected one
        processAllGates();
        findDifference();
    }

    public void switchWireValue(String wire) {
        // switches the value of the given starting wire
        // (i.e. one that starts with 'x' or 'y')
        if (wires.get(wire) == 1) {
            wires.put(wire, 0);
            return;
        }
        wires.put(wire, 1);
    }

    public long binaryNumberOf(String startChar) {
        String binary = "";
        int i = 0;
        while (true) {
            int value = valueOfWire(i, startChar);
            // iterates until there is no further wire of the given type
            if (value == -1) {
                break;
            }
            // adds value of the wire to the start of the binary string
            binary = value + binary;
            i++;
        }
        return Long.parseLong(binary, 2);
    }

    public int valueOfWire(int number, String startChar) {
        // returns -1 if the given wire doesn't exist
        // else it returns the value of the wire
        String asString = Integer.toString(number);
        if (asString.length() == 1) {
            asString = "0" + asString;
        }
        if (wires.containsKey(startChar + asString)) {
            return wires.get(startChar + asString);
        }
        return -1;
    }

    public void processAllGates() {
        while (!gates.isEmpty()) {
            List<String[]> unprocessedGates = new ArrayList<>();
            for (String[] gate : gates) {
                if (!wires.containsKey(gate[0]) || !wires.containsKey(gate[2])) {
                    unprocessedGates.add(gate);
                    continue;
                }
                processGate(gate);
            }
            gates = unprocessedGates;
        }
        actualResult = binaryNumberOf("z");
    }

    public void processGate(String[] gate) {
        int output;
        switch (gate[1]) {
            case "AND":
                output = wires.get(gate[0]) * wires.get(gate[2]);
                break;
            case "OR":
                output = Math.max(wires.get(gate[0]), wires.get(gate[2]));
                break;
            case "XOR":
                output = wires.get(gate[0]) ^ wires.get(gate[2]);
                break;
            default:
                throw new RuntimeException("unknown operation");
        }
        wires.put(gate[4], output);
    }

    public void findDifference() {
        // in the binary representation of diff, a 1 means different values on that bit
        // for the actual and the expected result. therefore a 0 means same value
        long diff = actualResult ^ expectedResult;
        String str = Long.toBinaryString(diff);
        for (int i = 0; i < str.length(); i++) {
            if ('1' == str.charAt(str.length() - 1 - i)) {
                addWrongZWire(i);
            }
        }
    }

    public void addWrongZWire(int num) {
        String numAsString = Integer.toString(num);
        // adds a leading zero if the number is just one digit
        if (numAsString.length() == 1) {
            numAsString = "0" + numAsString;
        }
        wrongZWires.add("z" + numAsString);
    }

    public static void main(String[] args) {
        Day24Part2 d24p2 = new Day24Part2();
        d24p2.processInput();
    }
}
