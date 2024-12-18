package day12;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day12Part1 {
    private char[][] map;
    private int nRows;
    private int nCols;
    // stores visited plots so they aren't processed multiple times
    private List<List<Integer>> visitedPlots = new ArrayList<>();
    // stores the positions of the plots that belong to the current region
    private List<List<Integer>> plotsOfRegion;

    private int currentArea;
    private int currentPerimeter;
    private int totalPrice = 0;

    Day12Part1() {
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
        currentPerimeter = 0;
        findPerimeter();
        totalPrice += currentArea * currentPerimeter;
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

    public void findPerimeter() {
        for (List<Integer> plot : plotsOfRegion) {
            int row = plot.get(0);
            int col = plot.get(1);
            currentPerimeter += perimeterOfPlot(row, col);
        }
    }

    public int perimeterOfPlot(int row, int col) {
        int perimeter = 4;
        // check up
        if (plotsOfRegion.contains(List.of(row - 1, col)))
            perimeter--;
        // check down
        if (plotsOfRegion.contains(List.of(row + 1, col)))
            perimeter--;
        // check left
        if (plotsOfRegion.contains(List.of(row, col - 1)))
            perimeter--;
        // check right
        if (plotsOfRegion.contains(List.of(row, col + 1)))
            perimeter--;
        return perimeter;
    }

    public static void main(String[] args) {
        Day12Part1 d12p1 = new Day12Part1();
        d12p1.processInput();
    }
}
