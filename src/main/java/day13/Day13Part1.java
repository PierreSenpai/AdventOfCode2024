package day13;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day13Part1 {
    private List<Machine> machines = new ArrayList<>();

    Day13Part1() {
        List<String> input = InputReader.readInputByLine("src/main/resources/day13.txt");
        for (int i = 0; i < input.size(); i += 4) {
            String infoA = input.get(i).substring(10);
            String infoB = input.get(i + 1).substring(10);
            String infoPrize = input.get(i + 2).substring(7);

            Machine machine = new Machine();
            // extract instructions of button A
            int xA = Integer.parseInt(infoA.substring(2, infoA.indexOf(",")));
            machine.setXA(xA);
            int yA = Integer.parseInt(infoA.substring(infoA.indexOf("Y") + 2));
            machine.setYA(yA);
            // extract instructions of button B
            int xB = Integer.parseInt(infoB.substring(2, infoB.indexOf(",")));
            machine.setXB(xB);
            int yB = Integer.parseInt(infoB.substring(infoB.indexOf("Y") + 2));
            machine.setYB(yB);
            // extract instructions of prize location
            int xPrize = Integer.parseInt(infoPrize.substring(2, infoPrize.indexOf(",")));
            machine.setXPrize(xPrize);
            int yPrize = Integer.parseInt(infoPrize.substring(infoPrize.indexOf("Y") + 2));
            machine.setYPrize(yPrize);
            machines.add(machine);
        }
    }

    public void processInput() {
        int sum = 0;
        for (Machine m : machines) {
            sum += processMachine(m);
        }
        System.out.println(sum);
    }

    public int processMachine(Machine machine) {
        int leastTokens = 0;
        for (int nA = 1; nA <= 100; nA++) {
            for (int nB = 1; nB <= 100; nB++) {
                int x = nA * machine.xA + nB * machine.xB;
                int y = nA * machine.yA + nB * machine.yB;
                int tokens = 3 * nA + nB;
                if (x == machine.xPrize && y == machine.yPrize &&
                    (leastTokens == 0 || tokens < leastTokens)) {
                    leastTokens = tokens;
                }
            }
        }
        return leastTokens;
    }

    public static void main(String[] args) {
        Day13Part1 d13p1 = new Day13Part1();
        d13p1.processInput();
    }
}
