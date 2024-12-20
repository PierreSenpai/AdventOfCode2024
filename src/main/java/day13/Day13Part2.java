package day13;

import java.util.ArrayList;
import java.util.List;

import util.InputReader;

public class Day13Part2 {
    private List<Machine> machines = new ArrayList<>();

    Day13Part2() {
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
            long xPrize = Integer.parseInt(infoPrize.substring(2, infoPrize.indexOf(",")));
            xPrize += 10000000000000L;
            machine.setXPrize(xPrize);
            long yPrize = Integer.parseInt(infoPrize.substring(infoPrize.indexOf("Y") + 2));
            yPrize += 10000000000000L;
            machine.setYPrize(yPrize);
            machines.add(machine);
        }
    }

    public void processInput() {
        long sum = 0;
        for (Machine m : machines) {
            sum += processMachine(m);
        }
        System.out.println(sum);
    }

    public long processMachine(Machine m) {
        // solve with linear algebra
        // nA * xA + nB * xB = xPrize
        // nA * yA + nB * yB = yPrize

        // nA = (xPrize / xA) - (nB * xB / xA)
        // nA = n1 - nB * n2
        double n1 = (double) m.xPrize / m.xA;
        double n2 = (double) m.xB / m.xA;

        // (n1 - nB * n2) * yA + nB * yB = yPrize
        // n1 * yA - nB * n2 * yA + nB * yB = yPrize
        // nB * (yB - n2 * yA) = yPrize - n1 * yA
        // nB = (yPrize - n1 * yA) / (yB - n2 * yA)
        double nB = (m.yPrize - n1 * m.yA) / (m.yB - n2 * m.yA);

        // nA = n1 - nB * n2
        double nA = n1 - nB * n2;

        long a = Math.round(nA);
        long b = Math.round(nB);

        if (a * m.xA + b * m.xB == m.xPrize && a * m.yA + b * m.yB == m.yPrize)
            return 3 * a + b;
        return 0;
    }

    public static void main(String[] args) {
        Day13Part2 d13p2 = new Day13Part2();
        d13p2.processInput();
    }
}
