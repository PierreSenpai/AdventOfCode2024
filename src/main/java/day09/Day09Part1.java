package day09;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day09Part1 {
    private List<Integer> filesystem = new ArrayList<>();

    Day09Part1() {
        String diskmap = InputReader.readInputAsString("src/main/resources/day09.txt").trim();
        // constructs filesystem based on diskmap
        int index = 0;
        boolean indicatesFile = true; // true if file, false if space

        for (int i = 0; i < diskmap.length(); i++) {
            int digit = Character.getNumericValue(diskmap.charAt(i));
            for (int j = 0; j < digit; j++) {
                if (indicatesFile) filesystem.add(index);
                else filesystem.add(-1); // using -1 instead of . so all values are integers
            }
            if (indicatesFile) index++;
            indicatesFile = !indicatesFile; // switches boolean value
        }
    }

    public void processInput() {
        reorderFiles();
        checksum();
    }

    public void reorderFiles() {
        // runs as long as there is free space and re-orders files
        while (filesystem.contains(-1)) {
            filesystem.set(
                    // first occurrence of free space
                    filesystem.indexOf(-1),
                    // removes last item (not needed anymore) and returns it for inserting in new
                    // position
                    filesystem.removeLast());
            // trims free space at the end of the filesystem
            // (this happens when a file was completely moved and now the space before it is
            // at the end
            // and in if this happens after the final move of a file, exceptions can occur)
            while (filesystem.getLast() == -1) {
                filesystem.removeLast();
            }
        }
    }

    public void checksum() {
        long checksum = 0;

        for (int i = 0; i < filesystem.size(); i++) {
            checksum += i * filesystem.get(i);
        }

        System.out.println(checksum);
    }

    public static void main(String[] args) {
        Day09Part1 d9p1 = new Day09Part1();
        d9p1.processInput();
    }
}
