package day17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.InputReader;

public class Day17Part2 {
    private long regA, regB, regC;
    private int instructionPointer;
    private List<Integer> instructions = new ArrayList<>();
    private List<Integer> output = new ArrayList<>();
    private List<Long> validInitializers = new ArrayList<>();

    Day17Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day17.txt");

        regA = regB = regC = 0;

        String instrs = input.getLast();
        instrs = instrs.substring(instrs.indexOf(":") + 2);
        instructions = Stream.of(instrs.split((",")))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public void processInput() {
        // builds possible values by going the last to first output
        findInitialA(instructions.size() - 1, 0);
        // takes the lowest of all initial values of register A
        validInitializers.sort(null);
        System.out.println(validInitializers.getFirst());
    }

    public void findInitialA(int outputIndex, long currentA) { // recursive
        // checks if all output numbers were already processed
        if (outputIndex < 0) {
            validInitializers.add(currentA);
            return;
        }
        int num = instructions.get(outputIndex);

        long originalA = currentA * 8;
        // testing the numbers 0 to 7 as the last three bits of regA,
        // which are stored in regB as the first step
        for (int j = 0; j <= 7; j++) {
            instructionPointer = 2;
            regA = originalA + j;
            regB = j;
            // performs the instructions until it would add a new output
            while (instructionPointer < instructions.size() &&
                    instructions.get(instructionPointer) != 5) {
                performInstruction(instructions.get(instructionPointer),
                        instructions.get(instructionPointer + 1));
            }
            // checks if the value in regB is the expected one
            if (num == regB % 8) {
                findInitialA(outputIndex - 1, originalA + j);
            }
        }
    }

    public void performInstruction(int opcode, int operand) {
        switch (opcode) {
            case 0:
                adv(operand);
                break;
            case 1:
                bxl(operand);
                break;
            case 2:
                bst(operand);
                break;
            case 3:
                jnz(operand);
                return;
            case 4:
                bxc(); // doesn't need operand
                break;
            case 5:
                out(operand);
                break;
            case 6:
                bdv(operand);
                break;
            case 7:
                cdv(operand);
                break;
            default:
                throw (new RuntimeException("unknown opcode"));
        }
        instructionPointer += 2;
    }

    public long combo(int operand) {
        switch (operand) {
            case 0:
            case 1:
            case 2:
            case 3:
                return operand;
            case 4:
                return regA;
            case 5:
                return regB;
            case 6:
                return regC;
            case 7:
                throw new RuntimeException("operand 7 shouldn't appear");
            default:
                throw new RuntimeException("unknown operand");
        }
    }

    public long specialDivision(long comboOp) {
        return (long) (regA / Math.pow(2, comboOp));
    }

    public void adv(int operand) {
        // divides regA by 2 to the power of the combo operand and
        // truncates the result to an int -> regA
        regA = specialDivision(combo(operand));
    }

    public void bxl(int operand) {
        // bitwise XOR of regB and literal operand -> regB
        regB = regB ^ operand;
    }

    public void bst(int operand) {
        // modulo 8 of the combo operand -> regB
        regB = combo(operand) % 8;
    }

    public void jnz(int operand) {
        // does nothing when regA is zero
        if (regA == 0) {
            instructionPointer += 2;
            return;
        }
        // sets the instructionPointer to the literal operand
        instructionPointer = operand;
    }

    public void bxc() {
        // bitwise XOR of regB and regC -> regB
        regB = regB ^ regC;
    }

    public void out(int operand) {
        // adds modulo 8 of the combo operand to the output
        output.add((int) (combo(operand) % 8));
    }

    public void bdv(int operand) {
        // same as adv but stores in regB
        regB = specialDivision(combo(operand));
    }

    public void cdv(int operand) {
        // same as adv but stores in regC
        regC = specialDivision(combo(operand));
    }

    public static void main(String[] args) {
        Day17Part2 d17p2 = new Day17Part2();
        d17p2.processInput();
    }
}
