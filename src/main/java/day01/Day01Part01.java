import java.io.BufferedReader;
import java.io.FileReader;

public class Day01Part01 {
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader("src/main/resources/day01.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }
}
