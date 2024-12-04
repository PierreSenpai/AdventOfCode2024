package day04;

import java.util.List;

import util.InputReader;

public class Day04Part1 {
    private List<String> input;
    private int charsPerLine;
    private int numOfLines;

    Day04Part1() {
        input = InputReader.readInputByLine("src/main/resources/day04.txt");
        charsPerLine = input.getFirst().length(); // looks at length of first line
        numOfLines = input.size();
    }

    public void processInput() {
        int sum = 0;
        // goes through all lines and its chars; for each 'X', it checks for "XMAS" in any direction
        for (int iLine = 0; iLine < numOfLines; iLine++) {
            for (int iChar = 0; iChar < charsPerLine; iChar++) {
                if (input.get(iLine).charAt(iChar) == 'X') {
                    sum += checkForXMAS(iLine, iChar);
                }
            }
        }
        System.out.println(sum);
    }

    public int checkForXMAS(int iLine, int iChar) {
        int sum = 0;
        sum += checkHorizontal(iLine, iChar);
        sum += checkVertical(iLine, iChar);
        sum += checkDiagonal(iLine, iChar);
        return sum;
    }

    public int checkHorizontal(int iLine, int iChar) {
        int sum = 0;
        String line = input.get(iLine);
        String str;

        if (iChar <= charsPerLine - 4) { // check left -> right; has to be at least 4 (= (lastIndex+1) - 4) away from line end
            str = "";
            str += line.charAt(iChar);
            str += line.charAt(iChar + 1);
            str += line.charAt(iChar + 2);
            str += line.charAt(iChar + 3);
            if (str.equals("XMAS")) {sum++;}
        }
        
        if (iChar >= 3) { // check right -> left; has to be at least 4 (= index 3) away from line start
            str = "";
            str += line.charAt(iChar);
            str += line.charAt(iChar - 1);
            str += line.charAt(iChar - 2);
            str += line.charAt(iChar - 3);
            if (str.equals("XMAS")) {sum++;}
        }

        return sum;
    }

    public int checkVertical(int iLine, int iChar) {
        int sum = 0;
        String str;

        if (iLine >= 3) { // check bottom -> top; must be 4th line (= index 3) or further
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine - 1).charAt(iChar);
            str += input.get(iLine - 2).charAt(iChar);
            str += input.get(iLine - 3).charAt(iChar);
            if (str.equals("XMAS")) {sum++;}
        }
        
        if (iLine <= numOfLines - 4) { // check top -> bottom; must be 4th to last line (= (lastIndex+1) - 4) or earlier
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine + 1).charAt(iChar);
            str += input.get(iLine + 2).charAt(iChar);
            str += input.get(iLine + 3).charAt(iChar);
            if (str.equals("XMAS")) {sum++;}
        }

        return sum;
    }

    public int checkDiagonal(int iLine, int iChar) {
        int sum = 0;
        String str;

        if (iLine >= 3 && iChar <= charsPerLine - 4) { // check bottom left -> top right
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine - 1).charAt(iChar + 1);
            str += input.get(iLine - 2).charAt(iChar + 2);
            str += input.get(iLine - 3).charAt(iChar + 3);
            if (str.equals("XMAS")) {sum++;}
        }

        if (iLine >= 3 && iChar >= 3) { // check bottom right -> top left
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine - 1).charAt(iChar - 1);
            str += input.get(iLine - 2).charAt(iChar - 2);
            str += input.get(iLine - 3).charAt(iChar - 3);
            if (str.equals("XMAS")) {sum++;}
        }

        if (iLine <= numOfLines - 4 && iChar <= charsPerLine - 4) { // check top left -> bottom right
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine + 1).charAt(iChar + 1);
            str += input.get(iLine + 2).charAt(iChar + 2);
            str += input.get(iLine + 3).charAt(iChar + 3);
            if (str.equals("XMAS")) {sum++;}
        }

        if (iLine <= numOfLines - 4 && iChar >= 3) { // check top left -> bottom right
            str = "";
            str += input.get(iLine).charAt(iChar);
            str += input.get(iLine + 1).charAt(iChar - 1);
            str += input.get(iLine + 2).charAt(iChar - 2);
            str += input.get(iLine + 3).charAt(iChar - 3);
            if (str.equals("XMAS")) {sum++;}
        }

        return sum;
    }

    public static void main(String[] args) {
        Day04Part1 d4p1 = new Day04Part1();
        d4p1.processInput();
    }
}
