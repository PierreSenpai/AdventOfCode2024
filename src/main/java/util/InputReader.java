import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

public class InputReader {
    public static List<String> readInputByLine(String path) {
        try {
            List<String> list = new ArrayList<String>();
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            return list;
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static String readInputAsString(String path) {
        try {
            Path p = Path.of(path);
            String str = Files.readString(p);
            return str;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
