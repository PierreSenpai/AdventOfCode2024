package day24;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.InputReader;

public class Day24Part1 {
    private Map<String, Integer> wires = new HashMap<>();
    private List<String[]> gates = new ArrayList<>();

    Day24Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day24.txt");
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
        outputZWires();
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

    public void outputZWires() {
        String binary = "";
        int i = 0;
        while (true) {
            int value = valueOfZWire(i);
            // iterates until there is no further z wire
            if (value == -1) {
                break;
            }
            // adds value of the z wire to the start of the binary string
            binary = value + binary;
            i++;
        }
        System.out.println(Long.parseLong(binary, 2));
    }

    public int valueOfZWire(int number) {
        // returns -1 if the given z wire doesn't exist
        // else it returns the value of the wire
        String asString = Integer.toString(number);
        if (asString.length() == 1) {
            asString = "0" + asString;
        }
        if (wires.containsKey("z" + asString)) {
            return wires.get("z" + asString);
        }
        return -1;
    }

    public static void main(String[] args) {
        Day24Part1 d24p1 = new Day24Part1();
        d24p1.processInput();
    }
}
