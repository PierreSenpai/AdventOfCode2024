package day04;

import java.util.List;

import util.InputReader;

public class Day04Part2 {
    private List<String> input;
    private int charsPerLine;
    private int numOfLines;

    Day04Part2() {
        input = InputReader.readInputByLine("src/main/resources/day04.txt");
        charsPerLine = input.getFirst().length(); // looks at length of first line
        numOfLines = input.size();
    }

    public void processInput() {
        int sum = 0;
        // goes through all lines and its chars; for each 'A', it checks for "MAS" in the two diagonals
        for (int iLine = 0; iLine < numOfLines; iLine++) {
            for (int iChar = 0; iChar < charsPerLine; iChar++) {
                if (input.get(iLine).charAt(iChar) == 'A') {
                    sum += checkForXMAS(iLine, iChar);
                }
            }
        }
        System.out.println(sum);
    }

    public int checkForXMAS(int iLine, int iChar) {
        String str1 = "";
        String str2 = "";

        if (iLine >= 1 && iLine <= numOfLines - 2 && // check if not on top/bottom/left/right edge
            iChar >= 1 && iChar <= charsPerLine - 2) { 
            // get bottom left <-> top right
            str1 += input.get(iLine + 1).charAt(iChar - 1);
            str1 += input.get(iLine).charAt(iChar);
            str1 += input.get(iLine - 1).charAt(iChar + 1);
            // get top left <-> bottom right
            str2 += input.get(iLine - 1).charAt(iChar - 1);
            str2 += input.get(iLine).charAt(iChar);
            str2 += input.get(iLine + 1).charAt(iChar + 1);
        }

        if ((str1.equals("MAS") || str1.equals("SAM")) &&
            (str2.equals("MAS") || str2.equals("SAM")))
            {return 1;}

        return 0;
    }

    public static void main(String[] args) {
        Day04Part2 d4p2 = new Day04Part2();
        d4p2.processInput();
    }
}
