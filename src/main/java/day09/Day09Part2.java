package day09;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day09Part2 {
    private List<Integer> filesystem = new ArrayList<>();
    private int maxIndex;

    Day09Part2() {
        String diskmap = InputReader.readInputAsString("src/main/resources/day09.txt").trim();
        // constructs filesystem based on diskmap
        int index = 0;
        boolean indicatesFile = true; // true if file, false if space

        for (int i = 0; i < diskmap.length(); i++) {
            int digit = Character.getNumericValue(diskmap.charAt(i));
            for (int j = 0; j < digit; j++) {
                if (indicatesFile)
                    filesystem.add(index);
                else
                    filesystem.add(-1); // using -1 instead of . so all values are integers
            }
            if (indicatesFile)
                index++;
            indicatesFile = !indicatesFile; // switches boolean value
        }
        maxIndex = index - 1; // index was incremented for next file, therefore index - 1
    }

    public void processInput() {
        reorderFiles();
        checksum();
    }

    public void reorderFiles() {
        for (int i = maxIndex; i >= 0; i--) {
            // finds block belonging to current file
            int start = filesystem.indexOf(Integer.valueOf(i));
            // breaks as soon as no free space on the left of the current file is available
            if (start < filesystem.indexOf(Integer.valueOf(-1)))
                break;
            // if start and end are the same position length is still 1 (-> + 1)
            int length = filesystem.lastIndexOf(Integer.valueOf(i)) - start + 1;
            // finds starting index of matching free space
            int startFreeSpace = findSpace(length);
            // moves to next file when not enough space was found before the file
            if (startFreeSpace == -1 || startFreeSpace > start)
                continue;
            moveFile(start, startFreeSpace, length);
        }
    }

    public int findSpace(int length) {
        int index = -1; // starting index of a group of free space (-1 for currently non-free space)
        int counter = 0; // counts free space in a row
        for (int i = 0; i < filesystem.size(); i++) {
            // if a free space is found, counter is incremented; if it's a new group of free
            // space the starting
            // index is also stores; if a file is found, the counter and index is reset
            if (filesystem.get(i) == -1) {
                if (index == -1)
                    index = i;
                counter++;
            } else {
                counter = 0;
                index = -1;
            }
            if (counter == length)
                return index; // returns if free space is big enough
        }
        return -1;
    }

    public void moveFile(int startFile, int startSpace, int length) {
        int index = filesystem.get(startFile);
        for (int i = 0; i < length; i++) {
            filesystem.set(startSpace + i, index);
            filesystem.set(startFile + i, -1);
        }
    }

    public void checksum() {
        long checksum = 0;

        for (int i = 0; i < filesystem.size(); i++) {
            if (filesystem.get(i) == -1)
                continue;
            checksum += i * filesystem.get(i);
        }
        System.out.println(checksum);
    }

    public static void main(String[] args) {
        Day09Part2 d9p2 = new Day09Part2();
        d9p2.processInput();
    }
}
