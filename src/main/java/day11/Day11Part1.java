package day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.InputReader;

public class Day11Part1 {
    private List<String> stones;

    Day11Part1() {
        String input = InputReader.readInputAsString("src/main/resources/day11.txt").trim();
        stones = Arrays.asList(input.split(" "));
    }

    public void processInput() {
        for (int i = 0; i < 25; i++) {
            blink();
            System.out.println(i);
        }
        System.out.println(stones.size());
    }

    public void blink() {
        List<String> temp = new ArrayList<>();
        for (String stone : stones) {
            if (stone.equals("0"))
                temp.add("1");
            else if (stone.length() % 2 == 0) {
                temp.add(stone.substring(0, stone.length() / 2));
                // remove leading zeros
                int n = Integer.parseInt(stone.substring(stone.length() / 2));
                temp.add(Integer.toString(n));
            }
            else {
                long n = Long.parseLong(stone) * 2024;
                temp.add(Long.toString(n));
            }
        }
        stones = temp;
    }

    public static void main(String[] args) {
        Day11Part1 d11p1 = new Day11Part1();
        d11p1.processInput();
    }
}
