import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.lang.Math;

public class Day01Part01 {
    private List<String> content;
    private List<Integer> list1 = new ArrayList<>();
    private List<Integer> list2 = new ArrayList<>();

    Day01Part01(String path) {
        content = InputReader.readInputByLine(path);
    }

    public void fillLists() {
        for (String line : content) {
            String[] numbers = line.split("   ");
            list1.add(Integer.valueOf(numbers[0]));
            list2.add(Integer.valueOf(numbers[1]));
        }

        Collections.sort(list1);
        Collections.sort(list2);
    }

    public int calculateDistance() {
        int sum = 0;
        for (int i = 0; i < list1.size(); i++) {
            int distance = list1.get(i) - list2.get(i);
            sum += Math.abs(distance);
        }
        return sum;
    }


    public static void main(String[] args) {
        Day01Part01 d1p1 = new Day01Part01("src/main/resources/day01.txt");
        d1p1.fillLists();
        System.out.println(d1p1.calculateDistance());
    }
}
