package day03;

import util.InputReader;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.List;

public class Day03Part1 {
    private String input;
    Day03Part1() {
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
        List<Integer> indicesStart, indicesEnd;
        indicesStart = findAll(input, "mul("); // list with indices of all matches with "mul("
        indicesEnd = findAll(input, ")"); // list with indices of all matches with ")"

        int sum = 0; // result of adding all multiplications

        for (int iStart : indicesStart) { // for each occurence of "mul("
            indicesEnd.removeIf(indexE -> indexE < iStart); // removing unnecessary (i.e. less than iStart) indices for ")"
            sum += multiply(input.substring(iStart, indicesEnd.getFirst() + 1)); // -> first int in indicesEnd = first ")" after current "mul("
        }
        System.out.println(sum);
    }


    public static void main(String[] args) {
        Day03Part1 d3p1 = new Day03Part1();
        d3p1.processInput();
    }
}
