package day23;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.InputReader;

public class Day23Part2 {
    private boolean[][] adjaMatrix;
    private List<String> vertices = new ArrayList<>();
    private Set<Integer> biggestSet;
    private int sizeBiggestSet = 0;
    private Map<Integer, Set<Integer>> adjacentIndices = new HashMap<>();
    private Set<Set<Integer>> cache = new HashSet<>();

    Day23Part2() {
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
        function1();
        findPassword();
    }

    public void function1() {
        for (int i = 0; i < vertices.size(); i++) {
            // prints progress
            System.out.print("doing " + vertices.get(i) + " (index " + i + ")\r");
            findLanPartyMembers(i, new HashSet<>());
        }
    }

    public void findLanPartyMembers(int current, Set<Integer> previous) {
        Set<Integer> connected = findConnectedTo(current);
        // if the current computer isn't connected to all previous one of the party
        // he can't be part of that party
        if (!connected.containsAll(previous)) {
            return;
        }
        previous.add(current);
        // if the same set of lan party members is reached multiple times,
        // the resulting computation will be redundant since the same members
        // will be found in the end, no matter which of them is the current computer
        if (cache.contains(previous)) {
            return;
        } else {
            cache.add(new HashSet<>(previous));
        }
        checkIfBetter(previous);
        // checks for the rest of the connected computers, which aren't already part
        // of the lan party, if they are in fact part of it
        connected.removeAll(previous);
        for (int computer : connected) {
            findLanPartyMembers(computer, new HashSet<>(previous));
        }
    }

    public void checkIfBetter(Set<Integer> set) {
        // if this lan party is bigger than the previously biggest one
        // it replaces that one
        if (set.size() <= sizeBiggestSet) {
            return;
        }
        sizeBiggestSet = set.size();
        biggestSet = new HashSet<>(set);
    }

    public Set<Integer> findConnectedTo(int indexComputer) {
        // returns a set with the indices of all computers that are connected
        // to the given one
        if (adjacentIndices.containsKey(indexComputer)) {
            return new HashSet<>(adjacentIndices.get(indexComputer));
        }
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < vertices.size(); i++) {
            if (adjaMatrix[indexComputer][i]) {
                set.add(i);
            }
        }
        // saves the result when findConnectedTo() is called with the same index again
        adjacentIndices.put(indexComputer, set);
        return new HashSet<>(set);
    }

    public void findPassword() {
        List<String> computers = new ArrayList<>();
        // translates the indices to the corresponding computer
        for (int index : biggestSet) {
            computers.add(vertices.get(index));
        }
        computers.sort(null);
        System.out.println(String.join(",", computers));
    }

    public static void main(String[] args) {
        Day23Part2 d23p2 = new Day23Part2();
        d23p2.processInput();
    }
}
