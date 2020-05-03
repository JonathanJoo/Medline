package parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONException;

import java.io.*;
import java.nio.file.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class parseMedlineFile {

    public static void main(String[] args) throws IOException {
        if(args.length != 1)
        {
            System.out.println("Usage: parser.parseMedlineFile <test.xml>");
            System.exit(0);
        }

        Map<String, String> fileMap = new HashMap<String, String>();
        String xmlFile = args[0];
        fileMap=getFileNames(xmlFile);
        //jsonFromXMLFile is for intermediate file contains converted XML to Json. The json will be used for parsing.
        String jsonFromXMLFile = fileMap.get("xmljson");
        //jsonToBeIndexed is final parsed json file to be indexed to Solr.
        String jsonToBeIndexed=fileMap.get("json");


        List<Article> articleList = null;

        try {
            Parser parser = new Parser(xmlFile, jsonFromXMLFile);
            articleList = parser.parsingMedlineXml();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            printArticleList(articleList, jsonToBeIndexed);
            System.out.println("The number of Parsed Documents is "+articleList.size()+" in "+fileMap.get("xml"));
        }

    }

    public static Map<String, String> getFileNames (String xmlFile) {
        Map<String, String> fileMap = new HashMap<String, String>();

        Path path=Paths.get(xmlFile);
        Path fileName=path.getFileName();
        String fileWithoutExt=fileName.toString().replace(".xml", "");


        Path directory = path.getParent();
        fileMap.put("xml", xmlFile);
        if (directory == null) {
            fileMap.put("json", fileWithoutExt+".json");
            fileMap.put("xmljson", "xml_"+fileWithoutExt+".json");
        }else {
            fileMap.put("json", directory+"/"+fileWithoutExt+".json");
            fileMap.put("xmljson", directory+"/xml_"+fileWithoutExt+".json");

        }


        return fileMap;

    }


    public static void printArticleList(List<Article> articleList, String jsonToBeIndexed) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(articleList);

        File fout = new File(jsonToBeIndexed);
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(json);
        bw.close();
    }


}