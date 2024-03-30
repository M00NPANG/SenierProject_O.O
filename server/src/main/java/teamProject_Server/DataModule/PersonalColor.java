package teamProject_Server.DataModule;
import java.io.*;
public class PersonalColor {
    public static String personal(String path) throws IOException {


        ProcessBuilder pb = new ProcessBuilder("python", "../personal_color/face.py",path);
        Process p = pb.start();

        // Python 스크립트의 표준 출력 읽기
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String outputLine;
        while ((outputLine = in.readLine()) != null) {
            System.out.println("Python Output: " + outputLine);
        }
        return outputLine;
    }
}