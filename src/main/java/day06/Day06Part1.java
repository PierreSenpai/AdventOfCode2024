package day06;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.InputReader;

public class Day06Part1 {
    private List<String> map, markedMap;
    private int currentRow, currentCol;
    private char currentDir = '^'; // stores '^', '>', 'v' or '<' depending on direction
    private int numOfX = 0;
    private char[] directions = {'^', '>', 'v','<'};
    private Map<Character, int[]> instructions = Map.of( // stores x and y change for each direction
        '^', new int[] {-1, 0},
        '>', new int[] {0, 1},
        'v', new int[] {1, 0},
        '<', new int[] {0, -1}
    );
    
    Day06Part1() {
        map = InputReader.readInputByLine("src/main/resources/day06.txt");
        markedMap = new ArrayList<String>(map);
    }

    public void processInput(){
        findStartingPosition();

        while (true) {
            try {
                if (checkAhead()) {rotate();}
                else {move();}
            } catch (IndexOutOfBoundsException e) { // once guard reaches border, next step is out of bounds
                System.out.println(numOfX);
                break;
            }
        }
    }

    public void findStartingPosition() {
        for (int row = 0; row < map.size(); row++) {
            String line = map.get(row);
            for(int col = 0; col < line.length(); col++) {
                if (line.charAt(col) == currentDir) {
                    currentRow = row;
                    currentCol = col;
                    // replace '.' at starting position with 'X' in seperate map
                    setTo(markedMap, 'X');
                    break;
                }
            }
        }
    }

    public void setTo(List<String> aMap, char symbol) {
        if (symbol == 'X') { // only increases counter if symbol is 'X'
            if (aMap.get(currentRow).charAt(currentCol) == 'X') {return;} // ends method if position is already 'X'
            numOfX++;
        }
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
        setTo(markedMap, 'X');
    }

    public static void main(String[] args) {
        Day06Part1 d6p1 = new Day06Part1();
        d6p1.processInput();
    }
}
