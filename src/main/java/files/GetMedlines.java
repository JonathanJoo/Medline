package files;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GetMedlines {

    public static void main(String[] args) throws IOException {
        // write your code here
        String file = "/Users/jooj/medline/index.lst";
        Path path = Paths.get(file);

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        List<String> xmlGzUrlsList = new ArrayList<>();

        for(String line : lines){

            if(!line.contains("xml.gz.md5")){
                addNonMD5Urls(xmlGzUrlsList, line);
            }
        }
        System.out.println(xmlGzUrlsList);
        writeToFile(xmlGzUrlsList);
    }

    public static List<String> addNonMD5Urls(List<String> list, String line) throws IOException {
        int firstIndex = line.indexOf('\"');
        int lastIndex = line.lastIndexOf('\"');
        String sub = line.substring(firstIndex+1, lastIndex);
        System.out.println(sub);
        list.add(sub);
        return list;
    }

    public static void writeToFile(List<String> list) throws IOException {
        File fout = new File("index.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for(int i=0; i<list.size(); i++){
            String line = list.get(i);
            if(!line.isEmpty()) {
                bw.write(line);
                bw.newLine();
            }
        }

        bw.close();
    }
}
