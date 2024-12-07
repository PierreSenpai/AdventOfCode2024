package day07;

import java.util.List;
import java.util.stream.Stream;

import util.InputReader;

public class Day07Part1 {
    List<String> input;

    Day07Part1() {
        input = InputReader.readInputByLine("src/main/resources/day07.txt");
    }

    public void processInput() {
        long sum = 0;
        for (String line : input) {
            String[] temp = line.split(": ");
            long res = Long.parseLong(temp[0]);
            int[] nums = Stream.of(temp[1].split(" "))
                .mapToInt(Integer::parseInt).
                toArray();
            sum += validateEquation(res, nums);
        }
        System.out.println(sum);
    }

    public long validateEquation(long res, int[] nums) {
        // using a binary number to represent the operators ('0' for addition, '1' for multiplication)
        // amount of digits same as number of operators (array length - 1)
        int nDecimal = (int) Math.pow(2, nums.length - 1) - 1;
        while (nDecimal >= 0) { // iterates through all possibilities until nDecimal = 0 (= only addition)
            String nBinary = Integer.toBinaryString(nDecimal);
            // adds leading zeros so binary number always has same length
            String formatter = "%"+(nums.length - 1)+"s";
            nBinary = String.format(formatter, nBinary)
                .replace(' ', '0');
            long sum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nBinary.charAt(i - 1) == '1') sum *= nums[i];
                else sum += nums[i];
            }
            if (res == sum) return res;
            nDecimal--; // preparing next combination
        }
        return 0;
    }

    public static void main(String[] args) {
        Day07Part1 d7p1 = new Day07Part1();
        d7p1.processInput();
    }
}
