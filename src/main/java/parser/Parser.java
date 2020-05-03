package parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import utils.MedlineUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Parser {

    private String xmlFile;
    private String jsonFile;
    private JSONObject convertedJson = null;


    public Parser(String xmlFile, String jsonFile) {
        this.xmlFile = xmlFile;
        this.jsonFile = jsonFile;
    }

    public Parser(String xmlFile) {
        this.xmlFile = xmlFile;
    }


    public void writeToJsonFile() throws IOException {
        int INDENTATION = 4;
        // create the json file first. This is for reference.
        System.out.println("Finished Parsing all the data in the json file");
        Path pathJson = Paths.get(this.jsonFile);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.jsonFile));
        writer.write(this.convertedJson.toString(INDENTATION));
        writer.close();

    }

    public List<Article> parsingMedlineXml() throws IOException {
        List<Article> articleList = new ArrayList<>();
        StringBuilder contentBuilder = new StringBuilder();
        String xmlStr = null;
        // Input xml file.
        try (Stream<String> stream = Files.lines(Paths.get(xmlFile), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // let's convert xml to json first. Because json is much better to parse!

        this.convertedJson = XML.toJSONObject(contentBuilder.toString());
        writeToJsonFile();
            // parsing start.
        JSONArray pubmedArticleList = (JSONArray) ((JSONObject) (convertedJson.get("PubmedArticleSet")))
                .get("PubmedArticle");

        //hold all Article objects in List. about 1000 per xml file.

        for (int idx = 0; idx < pubmedArticleList.length(); idx++) {
            String pmid=null;
            try {
                JSONObject pubmedArticle = (JSONObject) pubmedArticleList.get(idx);
                JSONObject medlineCitationForPMID = (JSONObject) pubmedArticle.get("MedlineCitation");
                JSONObject PMID = (JSONObject) medlineCitationForPMID.get("PMID");
                pmid = (String.valueOf(PMID.get("content")));
//                System.out.println("PMID is: " + pmid);

                Article article = null;
                article = parsePubmedArticle(pubmedArticle);
                article.setPmid(pmid);
                article.setId(pmid);
                // get the xml file name from xmlFile
                Path path = Paths.get(xmlFile);
                Path fileName=path.getFileName();
                article.setXmlfile(fileName.toString());

                if (article != null) {
                    articleList.add(article);
                }
            } catch (JSONException | ClassCastException ex) {
                ex.printStackTrace();
                System.out.println("Exception in PMID " + pmid + " in "+xmlFile);
            }
        }
        contentBuilder.delete(0,contentBuilder.length());

//            System.out.println("Finished Parsing all the data in json object");

        return articleList;
    }

    // parse pubmedArticle which is actual single article in JSON Object (or in XML file).
    private Article parsePubmedArticle(JSONObject pubmedArticle)   {
        // 2 big sub json objects
        Article article = new Article();
        JSONObject medlineCitation = (JSONObject) pubmedArticle.get("MedlineCitation");
        JSONObject pubmedData = (JSONObject) pubmedArticle.get("PubmedData");

        article = parseMedlineCitation(medlineCitation);

        return article;
    }

    private void parsePubmedData(Article article, JSONObject pubmedData) {
        //to do later if needed.
    }

    private Article parseMedlineCitation(JSONObject medlineCitation) {
        Article article = new Article();


        if (medlineCitation.has("NumberOfReferences")) {
            article.setRefcnt((Integer) medlineCitation.get("NumberOfReferences"));
        }
        article.setStatus((String) medlineCitation.get("Status"));

        JSONObject medlineJournalInfo = (JSONObject) medlineCitation.get("MedlineJournalInfo");
        article.setNlmid(String.valueOf(medlineJournalInfo.get("NlmUniqueID")));
        article.setCountry((String) medlineJournalInfo.get("Country"));

        if (medlineJournalInfo.has("ISSNLinking")) {
            article.setIssnl((String) medlineJournalInfo.get("ISSNLinking"));
        }
        article.setMedta((String) medlineJournalInfo.get("MedlineTA"));

        article.setOwner((String) medlineCitation.get("Owner"));

        List<String> citationSubsetList = new ArrayList<String>();
        if (medlineCitation.has("CitationSubset")) {
            switch (medlineCitation.get("CitationSubset").getClass().getSimpleName()) {
                case "String":
                    citationSubsetList.add((String) medlineCitation.get("CitationSubset"));
                    break;
                case "JSONArray":
                    JSONArray citationArray = (JSONArray) medlineCitation.get("CitationSubset");
                    for (int idx = 0; idx < citationArray.length(); idx++) {
                        String p = (String) citationArray.get(idx);
                        citationSubsetList.add(p);
                    }
                    break;
            }
        }
        article.setCitsubset(citationSubsetList);


        // section of "Article"
        JSONObject articleJson = (JSONObject) medlineCitation.get("Article");

        // PublicationTypeList
        if(articleJson.has("PublicationTypeList")){
            switch(articleJson.get("PublicationTypeList").getClass().getSimpleName()){
                case "String":
                    String publicationTypeString = (String) articleJson.get("PublicationTypeList");
                    break;
                case "JSONObject":
                    JSONObject publicationTypeListJson = (JSONObject) articleJson.get("PublicationTypeList");
                    List<String> publicationTypeList = new ArrayList<String>();
                    switch (publicationTypeListJson.get("PublicationType").getClass().getSimpleName()) {
                        case "JSONObject":
                            JSONObject pubType = (JSONObject) publicationTypeListJson.get("PublicationType");
                            publicationTypeList.add((String) pubType.get("content"));
                            break;
                        case "JSONArray":
                            JSONArray pubTypeArray = (JSONArray) publicationTypeListJson.get("PublicationType");
                            for (int idx = 0; idx < pubTypeArray.length(); idx++) {
                                JSONObject p = (JSONObject) pubTypeArray.get(idx);
                                publicationTypeList.add((String) p.get("content"));
                            }
                            break;
                    }
                    article.setPubtype(publicationTypeList);
                    break;

            }



        }



        List<String> language = new ArrayList<String>();
        if (articleJson.has("Language")) {
            if (articleJson.get("Language").getClass() == String.class) {
                language.add((String)articleJson.get("Language"));
            } else if (articleJson.get("Language").getClass() == JSONArray.class) {
                JSONArray langArray = (JSONArray)articleJson.get("Language");
                for (int idx=0; idx<langArray.length(); idx++) {
                    language.add((String)langArray.get(idx));
                }
            }

            article.setLang(language);
        }
        if (articleJson.has("Abstract")) {

            article.setAbs(getAbstractText((JSONObject)articleJson.get("Abstract")));
        }

        // get Author info.
        getAuthorInfo(article, articleJson);

        if ( articleJson.has("ArticleTitle") && articleJson.get("ArticleTitle").getClass() == String.class) {
            article.setArttitle((String) articleJson.get("ArticleTitle"));
        }

        // journal section
        JSONObject journalJson = (JSONObject) articleJson.get("Journal");
        JSONObject journalIssue = (JSONObject) journalJson.get("JournalIssue");
        if (journalIssue.has("Issue")){
            article.setIssue(String.valueOf( journalIssue.get("Issue")));
        }
        if (journalIssue.has("Volume")){
            article.setVol(String.valueOf(journalIssue.get("Volume")));
        }
        JSONObject pubDateJson = (JSONObject) journalIssue.get("PubDate");
        if (pubDateJson.has("Month")) {
            article.setPubmm(MedlineUtils.normalizeMonth(String.valueOf(pubDateJson.get("Month"))));
        }

        if (pubDateJson.has("Year")) {
            article.setPubyy(String.valueOf(pubDateJson.get("Year")));
            if (pubDateJson.has("Month")) {
                article.setPubdate(String.valueOf(pubDateJson.get("Year"))+ MedlineUtils.normalizeMonth(pubDateJson.get("Month")));
//                System.out.println(pubDateJson.get("Month")+" "+ article.getPubmm()+" "+article.getPubdate() );
            }
        }



        if (journalJson.has("ISOAbbreviation")) {
            article.setIsoabb((String) journalJson.get("ISOAbbreviation"));
        }

        if (journalJson.has("ISSN")) {
            JSONObject issnJson = (JSONObject) journalJson.get("ISSN");
            article.setIssn((String) issnJson.get("content"));
            article.setIssntype((String) issnJson.get("IssnType"));
        }

        article.setJournaltitle((String) journalJson.get("Title"));

        if (articleJson.has("VernacularTitle") && articleJson.get("VernacularTitle").getClass() == String.class)  {
            article.setVertitle((String) articleJson.get("VernacularTitle"));
        }
        if(medlineCitation.has("MeshHeadingList")) {
            getMESH(article, (JSONObject)medlineCitation.get("MeshHeadingList"));
        }

        return article;
    }

    // get abstract text
    private String getAbstractText(JSONObject absObj) {
        StringBuilder absStrBuilder = new StringBuilder();

        if (absObj.get("AbstractText").getClass() == String.class) {
            if (absObj.get("AbstractText").getClass() == String.class) {
                absStrBuilder.append((String) absObj.get("AbstractText"));
            }
        } else if (absObj.get("AbstractText").getClass() == JSONArray.class) {
            JSONArray absTextArray = (JSONArray)absObj.get("AbstractText");
            for (int idx=0; idx<absTextArray.length(); idx++) {
                if (absTextArray.get(idx).getClass() == String.class) {
                    absStrBuilder.append(((String) absTextArray.get(idx)));
                } else {
                    JSONObject abstextEach = (JSONObject) absTextArray.get(idx);
                    if (abstextEach.has("content") && abstextEach.get("content") == String.class) {
                        absStrBuilder.append(((String) abstextEach.get("content")));
                    }
                }
            }
        }
        return absStrBuilder.toString();
    }

    // for MESH
    private void getMESH(Article article, JSONObject meshHeadingJson) {

        List<String> mesh = new ArrayList<String>();

        switch (meshHeadingJson.get("MeshHeading").getClass().getSimpleName()) {
            case "JSONObject":
                JSONObject subMeshHeading = (JSONObject) meshHeadingJson.get("MeshHeading");
                if (subMeshHeading.has("QualifierName")) {
                    switch (subMeshHeading.get("QualifierName").getClass().getSimpleName()) {
                        case "JSONObject":
                            JSONObject descriptor = (JSONObject) subMeshHeading.get("QualifierName");
                            mesh.add((String) descriptor.get("content"));
                            break;
                        case "JSONArray":
                            JSONArray descriptorArray = (JSONArray) subMeshHeading.get("QualifierName");
                            for (int jdx = 0; jdx < descriptorArray.length(); jdx++) {
                                JSONObject descriptJson = (JSONObject) descriptorArray.get(jdx);
                                mesh.add((String) descriptJson.get("content"));
                            }
                            break;
                    }
                }

                if (subMeshHeading.has("DescriptorName")) {
                    switch (subMeshHeading.get("DescriptorName").getClass().getSimpleName()) {
                        case "JSONObject":
                            JSONObject descriptor = (JSONObject) subMeshHeading.get("DescriptorName");
                            mesh.add((String) descriptor.get("content"));
                            break;
                        case "JSONArray":
                            JSONArray descriptorArray = (JSONArray) subMeshHeading.get("DescriptorName");
                            for (int jdx = 0; jdx < descriptorArray.length(); jdx++) {
                                JSONObject descriptJson = (JSONObject) descriptorArray.get(jdx);
                                mesh.add((String) descriptJson.get("content"));
                            }
                            break;
                    }
                }
                break;

            case "JSONArray":
                JSONArray meshHeading = (JSONArray) (meshHeadingJson.get("MeshHeading"));
                for (int idx = 0; idx < meshHeading.length(); idx++) {
                    JSONObject subHeading = (JSONObject) meshHeading.get(idx);
                    if (subHeading.has("DescriptorName")) {
                        switch (subHeading.get("DescriptorName").getClass().getSimpleName()) {
                            case "JSONObject":
                                JSONObject descriptor = (JSONObject) subHeading.get("DescriptorName");
                                mesh.add((String) descriptor.get("content"));
                                break;
                            case "JSONArray":
                                JSONArray descriptorArray = (JSONArray) subHeading.get("DescriptorName");
                                for (int jdx = 0; jdx < descriptorArray.length(); jdx++) {
                                    JSONObject descriptJson = (JSONObject) descriptorArray.get(jdx);
                                    mesh.add((String) descriptJson.get("content"));
                                }
                                break;
                        }
                    }
                    if (subHeading.has("QualifierName")) {
                        switch (subHeading.get("QualifierName").getClass().getSimpleName()) {
                            case "JSONObject":
                                JSONObject qualifier = (JSONObject) subHeading.get("QualifierName");
                                mesh.add((String) qualifier.get("content"));
                                break;
                            case "JSONArray":
                                JSONArray qualifierArray = (JSONArray) subHeading.get("QualifierName");
                                for (int jdx = 0; jdx < qualifierArray.length(); jdx++) {
                                    JSONObject descriptJson = (JSONObject) qualifierArray.get(jdx);
                                    mesh.add((String) descriptJson.get("content"));
                                }
                                break;
                        }
                    }
                }
                break;
        }
        article.setMesh(mesh);
    }

    // for Author info.
    private void getAuthorInfo(Article article, JSONObject articleJson) {
                // authorList
                List<String> lname = new ArrayList<String>();
                List<String> fullname = new ArrayList<String>();
                if (articleJson.has("AuthorList")) {
                    JSONObject author = null;
                    JSONObject authorList = (JSONObject) articleJson.get("AuthorList");
                    switch (authorList.get("Author").getClass().getSimpleName()) {
                        case "JSONObject":
                            author = (JSONObject) authorList.get("Author");
                            if (author.has("LastName")) {
                                if (author.get("LastName").getClass() == String.class) {
                                    lname.add((String) author.get("LastName"));
                                    //combined. lastname , forename
                                    String fullnameStr = (String) author.get("LastName");
                                    if (author.has("ForeName")) {
                                        switch(author.get("ForeName").getClass().getSimpleName()){
                                            case "String":
                                                fullnameStr += ", " + (String) author.get("ForeName");
                                                break;
                                        }

                                    }
                                    fullname.add(fullnameStr);
                                }
                            }
                            break;
                        case "JSONArray":
                            JSONArray authorArray = (JSONArray) authorList.get("Author");
                            for (int idx = 0; idx < authorArray.length(); idx++) {
                                author = (JSONObject) authorArray.get(idx);
                                if (author.has("LastName")) {
                                    if (author.get("LastName").getClass() == String.class) {
                                        lname.add((String) author.get("LastName"));
                                        String fullnameStr = (String) author.get("LastName");
                                        if (author.has("ForeName") && author.get("ForeName").getClass() == String.class) {
                                            fullnameStr += ", " + (String) author.get("ForeName");
                                        }
                                        fullname.add(fullnameStr);
                                    }
                                }
                            }
                            break;
                    }
            article.setLname(lname);
            article.setFullname(fullname);
        }
    }
}