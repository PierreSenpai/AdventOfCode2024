package day06;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import util.InputReader;

public class Day06Part2 {
    private List<String> originalMap, map, markedMap;
    private int currentRow, currentCol;
    private char currentDir; // stores '^', '>', 'v' or '<' depending on direction
    private char[] directions = {'^', '>', 'v','<'};
    private Map<Character, int[]> instructions = Map.of( // stores x and y change for each direction
        '^', new int[] {-1, 0},
        '>', new int[] {0, 1},
        'v', new int[] {1, 0},
        '<', new int[] {0, -1}
    );
    
    Day06Part2() {
        originalMap = InputReader.readInputByLine("src/main/resources/day06.txt");
        // markedMap = new ArrayList<String>(map);
    }

    public void processInput(){
        int nPossibilities = 0; // counts num of possibilities with loops

        // iterates through all possible locations for the new obstruction
        for (int row = 0; row < originalMap.size(); row++) {
            for (int col = 0; col < originalMap.get(row).length(); col++) {
                // prohibits placing obstruction on or in front of starting position
                try {
                    if (originalMap.get(row).charAt(col) == '^' ||
                        originalMap.get(row + 1).charAt(col) == '^') {
                        continue;
                    }
                } catch (IndexOutOfBoundsException e) {}
                // prohibits placing obstruction on already existing obstruction
                if (originalMap.get(row).charAt(col) == '#') {continue;}
                // seperate map and markedMap for each possibility
                currentDir = '^'; // overwrite direction of last iteration with starting direction
                map = new ArrayList<String>(originalMap);
                setObstruction(map, row, col);
                markedMap = new ArrayList<String>(map);

                nPossibilities += checkPossibility();
            }
        }
        System.out.println(nPossibilities);
    }

    public int checkPossibility(){
        findStartingPosition();

        while (true) {
            try {
                if (checkAhead()) {rotate();}
                else { 
                    move();
                    if (setMarked()) {return 1;} // same marking results in same path
                }
            } catch (IndexOutOfBoundsException e) { // once guard reaches border, next step is out of bounds
                break;
            }
        }   
        return 0;
    }

    public void setObstruction(List<String> aMap, int row, int col) {
        String temp = aMap.get(row);
        temp = temp.substring(0, col)+'#'+temp.substring(col+1); // putting # instead of 0 so checkAhead() can stay the same
        aMap.set(row, temp);
    }

    public boolean setMarked() { // returns true if current position already marked with same direction
        char currentSymbol = markedMap.get(currentRow).charAt(currentCol);
        if (currentSymbol == currentDir) {return true;}

        setTo(markedMap, currentDir);
        return false;
    }

    public void findStartingPosition() {
        for (int row = 0; row < map.size(); row++) {
            String line = map.get(row);
            for(int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == currentDir) {
                    currentRow = row;
                    currentCol = col;
                    break;
                }
            }
        }
    }

    public void setTo(List<String> aMap, char symbol) {
        String temp = aMap.get(currentRow);
        temp = temp.substring(0, currentCol)+symbol+temp.substring(currentCol+1);
        aMap.set(currentRow, temp);
    }

    public boolean checkAhead() { // returns true if obstacle is ahead, else false
        int[] instruction = instructions.get(currentDir);
        if (map.get(currentRow + instruction[0]).charAt(currentCol + instruction[1]) == '#'){
            return true;
        }
        return false;
    }

    public void rotate() throws IndexOutOfBoundsException{
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == currentDir) {
                currentDir = directions[(i + 1) % directions.length]; // changes to next direction; modulo leads to loop
                break;
            }
        }
    }

    public void move() throws IndexOutOfBoundsException {
        int[] instruction = instructions.get(currentDir);
        setTo(map, '.');
        currentRow += instruction[0];
        currentCol += instruction[1];
        setTo(map, currentDir);
    }

    public static void main(String[] args) {
        Day06Part2 d6p2 = new Day06Part2();
        d6p2.processInput();
    }
}
