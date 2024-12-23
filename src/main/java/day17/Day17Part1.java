package day17;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import util.InputReader;

public class Day17Part1 {
    private int regA, regB, regC;
    private int instructionPointer = 0;
    private List<Integer> instructions = new ArrayList<>();
    private List<String> output = new ArrayList<>();

    Day17Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day17.txt");

        int regNumsStartAt = input.getFirst().indexOf(":") + 2;
        regA = Integer.parseInt(input.get(0).substring(regNumsStartAt));
        regB = Integer.parseInt(input.get(1).substring(regNumsStartAt));
        regC = Integer.parseInt(input.get(2).substring(regNumsStartAt));

        String instrs = input.getLast();
        instrs = instrs.substring(instrs.indexOf(":") + 2);
        instructions = Stream.of(instrs.split((",")))
            .map(Integer::parseInt)
            .collect(Collectors.toList());
    }

    public void processInput() {
        while (instructionPointer < instructions.size()) {
            performInstruction(instructions.get(instructionPointer),
                instructions.get(instructionPointer + 1));
        }
        System.out.println(String.join(",", output));
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

    public int combo(int operand) {
        switch(operand) {
            case 0: case 1: case 2: case 3:
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

    public int specialDivision(int comboOp) {
        return (int) (regA / Math.pow(2, comboOp));
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
        String anOutput = String.valueOf(combo(operand) % 8);
        output.add(anOutput);
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
        Day17Part1 d17p1 = new Day17Part1();
        d17p1.processInput();
    }
}
