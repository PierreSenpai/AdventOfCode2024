package day12;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day12Part2 {
    private char[][] map;
    private int nRows;
    private int nCols;
    // stores visited plots so they aren't processed multiple times
    private List<List<Integer>> visitedPlots = new ArrayList<>();
    // stores the positions of the plots that belong to the current region
    private List<List<Integer>> plotsOfRegion;
    // stores sides as { rowIn, colIn, rowOut, colOut }
    private List<List<Integer>> sidesOfRegion;

    List<List<Integer>> directions = List.of(
        List.of(0, 1), List.of(1, 0), List.of(0, -1), List.of(-1, 0));
        // right, down, left, up

    private int currentArea;
    private int currentSides;
    private int totalPrice = 0;

    Day12Part2() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day12.txt");

        nRows = input.size();
        nCols = input.getFirst().length();
        map = new char[nRows][nCols];

        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col ++) {
                map[row][col] = input.get(row).charAt(col);
            }
        }
    }

    public void processInput() {
        // iterates through every plot
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                processPosition(row, col);
            }
        }
        System.out.println(totalPrice);
    }

    public void processPosition(int row, int col) {
        // skips position if it was already processed
        if (!checkPositionValid(row, col))
            return;
        // finds whole region that the plot belongs to
        findRegion(row, col);
        currentArea = plotsOfRegion.size();
        currentSides = 0;
        findSides();
        totalPrice += currentArea * currentSides;
    }

    public boolean checkPositionValid(int row, int col) {
        // checks for positions outside of the map
        if (row < 0 || row >= nRows || col < 0 || col >= nCols)
            return false;
        // checks for already visited positions
        if (visitedPlots.contains(List.of(row, col)))
            return false;
        return true;
    }

    public void findRegion(int row, int col) {
        plotsOfRegion = new ArrayList<>();
        visitedPlots.add(List.of(row, col));
        plotsOfRegion.add(List.of(row, col));
        char type = map[row][col];
        findRegion(row, col, type);
    }

    public void findRegion(int row, int col, char type) { // recursive
        // check up
        if (checkPlotPartOfRegion(row - 1, col, type))
            findRegion(row - 1, col, type);
        // check down
        if (checkPlotPartOfRegion(row + 1, col, type))
            findRegion(row + 1, col, type);
        // check left
        if (checkPlotPartOfRegion(row, col - 1, type))
            findRegion(row, col - 1, type);
        // check right
        if (checkPlotPartOfRegion(row, col + 1, type))
            findRegion(row, col + 1, type);
    }

    public boolean checkPlotPartOfRegion(int row, int col, char type) {
        if (!checkPositionValid(row, col))
            return false;
        if (map[row][col] == type) {
            visitedPlots.add(List.of(row, col));
            plotsOfRegion.add(List.of(row, col));
            return true;
        }
        return false;
    }

    public void findSides() {
        sidesOfRegion = new ArrayList<>();
        List<Integer> startPos = plotsOfRegion.getFirst();
        findSides(startPos.get(0), startPos.get(1), 0);

        // the algorithm above only traces the outer sides, so we need an additional one
        // for the inside: it iterates through each plot of the region and looks for edges
        // to plots of other regions. if these aren't included in the list sidesOfRegion
        // (-> don't belong to the outer edges), it performs a similar algorithm to the first
        // on that edge
        findInsideSides();
    }

    public void findSides(int startRow, int startCol, int startDir) {
        int row = startRow;
        int col = startCol;
        int dir = startDir;

        do {
            // check left of current direction
            // -> if in region, turn left, increment sides, move; else add side
            int leftDir = (4 + dir - 1) % 4; // "4 +" prevents the parantheses to evaluate to a negative number
            if (checkDirection(row, col, leftDir)) {
                // turn left
                dir = leftDir;
                currentSides++;
                // move to the next position
                row += directions.get(dir).get(0);
                col += directions.get(dir).get(1);
                continue;
            } else {
                sidesOfRegion.add(List.of(row, col, row + directions.get(leftDir).get(0),
                        col + directions.get(leftDir).get(1)));
            }
            // check in current direction
            // -> if not in region, turn right, increment sides; else move
            if (!checkDirection(row, col, dir)) {
                // turn right
                dir = (dir + 1) % 4;
                currentSides++;
            } else {
                // move to the next position
                row += directions.get(dir).get(0);
                col += directions.get(dir).get(1);
            }
        } while (row != startRow || col != startCol || dir != startDir);
    }

    public boolean checkDirection(int row, int col, int dir) {
        int newRow = row + directions.get(dir).get(0);
        int newCol = col + directions.get(dir).get(1);
        if (newRow < 0 || newRow >= nRows || newCol < 0 || newCol >= nCols)
            return false;
        return map[row][col] == map[newRow][newCol];
    }

    public void findInsideSides() {
        for (List<Integer> plot : plotsOfRegion) {
            int row = plot.get(0);
            int col = plot.get(1);
            for (int dir = 0; dir < 4; dir++) {
                int newRow = row + directions.get(dir).get(0);
                int newCol = col + directions.get(dir).get(1);
                if (plotsOfRegion.contains(List.of(newRow, newCol)))
                    continue;
                if (!sidesOfRegion.contains(List.of(row, col, newRow, newCol)))
                    findSides(row, col, dir + 1);
            }
        }
    }

    public static void main(String[] args) {
        Day12Part2 d12p2 = new Day12Part2();
        d12p2.processInput();
    }
}
