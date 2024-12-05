package day05;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day05Part2 {
    private List<String> input;
    private List<String[]> rules, updates, relevantRules;
    private int indexFalse1, indexFalse2; // stores indices of the two pages which are in wrong order
    
    Day05Part2() {
        input = InputReader.readInputByLine("src/main/resources/day05.txt");
    }
    
    public void processInput() {
        getRules();
        getUpdates();
        // getIncorrect();

        int sum = 0;

        for (int i = 0; i < updates.size(); i++) {
            getRelevant(i);
            String[] update = updates.get(i);

            if (followsRules(update)) {continue;} // because correct updates are now irrelevant
            while (!followsRules(update)) {
                // swapping both pages often leads to correct updates, if not more swapping until correct
                String temp = update[indexFalse1];
                update[indexFalse1] = update[indexFalse2];
                update[indexFalse2] = temp; 
            }
            sum += Integer.parseInt(update[update.length / 2]); // array length odd but .5 gets truncated
        }

        System.out.println(sum);
    }

    // rest same as in part 1 
        
    public boolean followsRules(String[] update) {
        for (String[] rule : relevantRules) {
            // these two class variables will be reused when this method returns false (if not they aren't really "false indices")
            indexFalse1 = findIndex(update, rule[0]);
            indexFalse2 = findIndex(update, rule[1]); 
            if (indexFalse1 > indexFalse2)  { // bigger index means printed after second page -> incorrect
                return false;
            }
        }
        return true;
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

    public int findIndex(String[] update, String page) {
        for (int i = 0; i < update.length; i++) {
            if (update[i].equals(page)) {return i;}
        }
        return -1; // if no match found, returns -1 -> leads to exception
    }

    public static void main(String[] args) {
        Day05Part2 d5p2 = new Day05Part2();
        d5p2.processInput();
    }
}
