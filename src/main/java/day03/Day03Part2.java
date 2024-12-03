package day03;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day03Part2 {
    private String input;
    Day03Part2() {
        input = InputReader.readInputAsString("src/main/resources/day03.txt");
    } 

    public int multiply(String instruction) {
        try {
            instruction = instruction.substring(4, instruction.length()-1); // removes "mul(" and ")", leaving the content within the parentheses
            String[] nums = instruction.split(","); // splits into the two numbers
            if (nums.length > 2) {return 0;} // if parantheses contain 3 or more numbers seperated by commas
            return Integer.parseInt(nums[0]) * Integer.parseInt(nums[1]);
        } catch (Exception e) { // triggers when instruction isn't right, e.g. "mul(...mul(...)" or "mul(...,)"
            return 0;
        }
    }

    public List<Integer> findAll(String wholeString, String pattern) {
        List<Integer> indices = new ArrayList<>();
        int charsRemoved = 0; // required, so indices match to original input after repeatedly removing parts at the start of string 
        while (true) {
            int index = wholeString.indexOf(pattern); // gets index for first instance of pattern occuring in wholeString
            if (index == -1) {break;} // -1 means no match found in the string
            indices.add(index + charsRemoved);
            wholeString = wholeString.substring(index+1); // removes part of string until match -> next iteration doesn't find same match
            charsRemoved += index + 1; // index starts at 0 -> index 1 less than amount of removed chars
        }
        return indices;
    }

    public void processInput() {
        List<Integer> indicesStart, indicesEnd, indicesCond;
        indicesStart = findAll(input, "mul("); // list with indices of all matches with "mul("
        indicesEnd = findAll(input, ")"); // list with indices of all matches with ")"
        indicesCond = findAll(input, "do()"); // list with indices of all matches with "do()"
        indicesCond.addAll(findAll(input, "don't()")); // all matches with "don't()" are added
        indicesCond.sort(null); // if not sorted, it would later iterate through all "do()"s before any "don't()"
        
        boolean doMul = true; // determines if current mul() should be evaluated

        int sum = 0; // result of adding all multiplications

        for (int i = 0; i < indicesStart.size(); i++) { // for each occurence of "mul("
            int iStart = indicesStart.get(i); // using this kind of for loop because of scope issues
            int iCond = 0; // index of current conditional instruction
            for (int j = 0; j < indicesCond.size(); j++) { // searching for closest conditional before current "mul()"
                if (indicesCond.get(j) > iStart) {break;}
                iCond = indicesCond.get(j);
            }
            final int iCurrentCond = iCond; // scope issues again
            indicesEnd.removeIf(indexE -> indexE < iCurrentCond); // removing unnecessary indices for ")"
            // gets conditional String and compares it to do and don't (else case happens when no conditional between two "mul()")
            String cond = input.substring(iCurrentCond, indicesEnd.getFirst() + 1);
            if (cond.equals("do()")) {doMul = true;} 
            else if (cond.equals("don't()")) {doMul = false;}
            
            if (doMul) {
            indicesEnd.removeIf(indexE -> indexE < iStart); // removing unnecessary (i.e. less than iStart) indices for ")"
            sum += multiply(input.substring(iStart, indicesEnd.getFirst() + 1)); // -> first int in indicesEnd = first ")" after current "mul("
            }
        }
        System.out.println(sum);
    }


    public static void main(String[] args) {
        Day03Part2 d3p2 = new Day03Part2();
        d3p2.processInput();
    }
}
