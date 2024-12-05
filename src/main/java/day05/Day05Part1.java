package day05;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day05Part1 {
    private List<String> input;
    private List<String[]> rules, updates, relevantRules;
    
    Day05Part1() {
        input = InputReader.readInputByLine("src/main/resources/day05.txt");
    }
    
    public void processInput() {
        getRules();
        getUpdates();

        int sum = 0;

        for (int i = 0; i < updates.size(); i++) {
            getRelevant(i);
            String[] update = updates.get(i);
            if (followsRules(update)) {
                sum += Integer.parseInt(update[update.length / 2]); // array length odd but .5 gets truncated
            }
        }

        System.out.println(sum);
    }



    public void getRules() {
        rules = new ArrayList<>();
        for (String line : input) {
            if (line.equals("")) { // stops when first section ends
                break;
            }
            rules.add(line.split("\\|")); // double backslash since '|' without backslashes means OR
        }
    }

    public void getUpdates() {
        updates = new ArrayList<>();
        int startOfUpdates = input.indexOf("") + 1; // index of line where updates begin
        for (int i = startOfUpdates; i < input.size(); i++) {
            updates.add(input.get(i).split(","));
        }
    }

    public void getRelevant(int indexUpdate) { 
        String[] update = updates.get(indexUpdate);
        relevantRules = new ArrayList<>();
        for (String[] rule : rules) {
            if (isIn(rule[0], update) && isIn(rule[1], update)){ // rules only revelant if both pages are included in update
                relevantRules.add(rule);
            }
        }
    }

    public boolean isIn(String page, String[] update) {
        for (String includedPage : update) {
            if (page.equals(includedPage)) {return true;}
        }
        return false;
    }

    public boolean followsRules(String[] update) {
        for (String[] rule : relevantRules) {
            if (findIndex(update, rule[0]) > findIndex(update, rule[1]))  { // bigger index means printed after second page -> incorrect
                return false;
            }
        }
        return true;
    }

    public int findIndex(String[] update, String page) {
        for (int i = 0; i < update.length; i++) {
            if (update[i].equals(page)) {return i;}
        }
        return -1; // if no match found, returns -1 -> leads to exception
    }

    public static void main(String[] args) {
        Day05Part1 d5p1 = new Day05Part1();
        d5p1.processInput();
    }
}
