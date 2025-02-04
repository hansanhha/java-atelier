package hansanhha;

import java.io.File;
import java.io.FileReader;

public class InvalidErrorHandler {

    public void readFile(String filePath) {
        try {
            File file = new File(filePath);
            FileReader reader = new FileReader(file);
            reader.read();
            reader.close();
        } catch (Exception e) {
            // 빈 catch 블록 (예외를 무시하는 안티 패턴)
        }
    }
}
