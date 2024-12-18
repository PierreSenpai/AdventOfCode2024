package day11;

import java.util.stream.Stream;
import java.util.HashMap;

import util.InputReader;

public class Day11Part2 {
    // since the same number results in the same stones, we can avoid enormous lists with duplicate stones by
    // using a hashmap that contains stoneNumber/amount pairs
    private HashMap<Long, Long> stones = new HashMap<>();

    Day11Part2() {
        String input = InputReader.readInputAsString("src/main/resources/day11.txt").trim();

        long[] temp = Stream.of(input.split(" ")).mapToLong(Long::parseLong).toArray();
        for (int i = 0; i < temp.length; i++) {
            long stone = temp[i];
            // merge: if the key value pair doesn't exist in the hashmap, it adds stone:1
            //        else it adds 1 (b) to the previous value (a)
            stones.merge(stone, 1L, (a, b) -> a + b);
        }
    }

    public void processInput() {
        for (int i = 0; i < 75; i++) {
            blink();
        }
        long nStones = 0;
        for (long stone : stones.keySet()) {
            nStones += stones.get(stone);
        }
        System.out.println(nStones);
    }

    public void blink() {
        HashMap<Long, Long> newStones = new HashMap<>();
        for (long stone : stones.keySet()) {
            if (stone == 0) {
                newStones.merge(1L, stones.get(stone), (a, b) -> a + b);
                continue;
            }
            String str = Long.toString(stone);
            if (str.length() % 2 == 0) {
                long n1 = Long.parseLong(str.substring(0, str.length() / 2));
                newStones.merge(n1, stones.get(stone), (a, b) -> a + b);
                long n2 = Long.parseLong(str.substring(str.length() / 2));
                newStones.merge(n2, stones.get(stone), (a, b) -> a + b);
                continue;
            }
            newStones.merge(stone * 2024, stones.get(stone), (a, b) -> a + b);
        }
        stones = newStones;
    }

    public static void main(String[] args) {
        Day11Part2 d11p2 = new Day11Part2();
        d11p2.processInput();
    }
}
