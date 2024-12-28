package day23;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.InputReader;

public class Day23Part1 {
    private boolean[][] adjaMatrix;
    private List<String> vertices = new ArrayList<>();
    private Set<Set<String>> computerSets = new HashSet<>();

    Day23Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day23.txt");
        // constructs the graph
        Set<String> vertexSet = new HashSet<>();
        List<String[]> edges = new ArrayList<>();
        for (String line : input) {
            String[] edge = line.split("-");
            vertexSet.add(edge[0]);
            vertexSet.add(edge[1]);
            edges.add(edge);
        }
        vertices.addAll(vertexSet);
        adjaMatrix = new boolean[vertices.size()][vertices.size()];
        for (String[] edge : edges) {
            adjaMatrix[vertices.indexOf(edge[0])][vertices.indexOf(edge[1])] = true;
            adjaMatrix[vertices.indexOf(edge[1])][vertices.indexOf(edge[0])] = true;
        }
    }

    public void processInput() {
        findComputerSets();
        computerSets.removeIf(s -> noStartWithT(s));
        System.out.println(computerSets.size());
    }

    public void findComputerSets() {
        for (int first = 0; first < vertices.size(); first++) {
            List<Integer> listForSecond = findConnectedTo(first);
            for (int second : listForSecond) {
                List<Integer> listForThird = findConnectedTo(second);
                listForThird.retainAll(listForSecond);
                for (int third : listForThird) {
                    computerSets.add(Set.of(
                            vertices.get(first),
                            vertices.get(second),
                            vertices.get(third)));
                }
            }
        }
    }

    public List<Integer> findConnectedTo(int indexComputer) {
        List<Integer> list = new ArrayList<>();
        // "indexComputer + 1" instead of 0 to reduce the amount of redundant computation
        for (int i = indexComputer + 1; i < vertices.size(); i++) {
            if (adjaMatrix[indexComputer][i]) {
                list.add(i);
            }
        }
        return list;
    }

    public boolean noStartWithT(Set<String> set) {
        for (String computer : set) {
            if (computer.startsWith("t")) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Day23Part1 d23p1 = new Day23Part1();
        d23p1.processInput();
    }
}
