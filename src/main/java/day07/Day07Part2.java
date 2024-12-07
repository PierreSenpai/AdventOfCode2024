package day07;

import java.util.List;
import java.util.stream.Stream;

import util.InputReader;

public class Day07Part2 {
    List<String> input;

    Day07Part2() {
        input = InputReader.readInputByLine("src/main/resources/day07.txt");
    }

    public void processInput() {
        long sum = 0;
        for (String line : input) {
            String[] temp = line.split(": ");
            long res = Long.parseLong(temp[0]);
            long[] nums = Stream.of(temp[1].split(" "))
                .mapToLong(Long::parseLong).
                toArray();
            sum += validateEquation(res, nums);
        }
        System.out.println(sum);
    }

    public long validateEquation(long res, long[] nums) {
        // using a base 3 number to represent the operators ('0' for addition, '1' for multiplication,
        // '2' for concatenation)
        // amount of digits same as number of operators (array length - 1)
        int nDecimal = (int) Math.pow(3, nums.length - 1) - 1;
        while (nDecimal >= 0) { // iterates through all possibilities until nDecimal is 0 (= only addition)
            String nBase3 = toBase3(nDecimal);
            // adds leading zeros so base3 number always has same length
            String formatter = "%"+(nums.length - 1)+"s";
            nBase3 = String.format(formatter, nBase3)
                .replace(' ', '0');
            long sum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                if (nBase3.charAt(i - 1) == '1') sum *= nums[i];
                else if (nBase3.charAt(i - 1) == '0') sum += nums[i];
                else { // == '2' (concatenation)
                    sum = Long.parseLong(String.valueOf(sum) + String.valueOf(nums[i]));
                }
            }
            if (res == sum) { 
                System.out.println(res);
                return res;}
            nDecimal--; // preparing next combination
        }
        return 0;
    }

    public String toBase3(long decimal) {
        String str = "";
        long power = 1;
        // increases to highest power of three that is less than decimal
        while (power * 3 <= decimal) power *= 3;

        while (power >= 1) {
            str += decimal/power;
            decimal %= power;
            power /= 3;
        }
        return str;
    }

    public static void main(String[] args) {
        Day07Part2 d7p2 = new Day07Part2();
        d7p2.processInput();
    }
}
