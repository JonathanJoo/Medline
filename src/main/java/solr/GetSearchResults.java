package solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import parser.Article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetSearchResults {

    public static String  zkHosts="ec2-18-191-198-32.us-east-2.compute.amazonaws.com:2181";
    public static String collection="medline";
    public static String flList="pmid,id,refcnt,status,nlmid,country,issnl,medta,owner,citsubse"+
            ",pubtype,lnam,fullname,mesh,abs,lang,arttitle,issue,vol,pubmm,pubyy,pubdate,isoabb,issn,issntype,journaltitle,vertitle";
    public static String[] facetFields=new String[] {"country", "pubyy", "lang", "lname_str", "journaltitle_str", "issn", "pubtype", "issntype"};
            
            
            
    public static void main(String[] args) throws IOException, SolrServerException {
        System.out.println(flList);


        SolrUtils solrUtils = new SolrUtils();

        solrUtils.createCloudSolrClient(zkHosts, collection);
        CloudSolrClient client=solrUtils.getCloudSolrClient();

        final SolrQuery query = new SolrQuery();
        query.set("q", "all:water");
        query.setParam("fl", flList);
        query.setParam("facet.limit", "5");
        query.addFacetField(facetFields);
        QueryResponse response = client.query(query);

        System.out.println("Header : "+response.getHeader().toString());
        System.out.println("Number found : "+response.getResults().getNumFound());
        System.out.println("QTime : "+response.getQTime()+"msec");

        // matched documents
        System.out.println("Matched Documents");
        SolrDocumentList docList = response.getResults();
        final List<Article> matchedArticles = response.getBeans(Article.class);

        matchedArticles.forEach (article -> System.out.println(article));

        // facet handling
        System.out.println("Number of Facet Fields : " +response.getFacetFields().size());
        FacetField fcountry = response.getFacetField("country");

        Map<String, List<MedFacet>> facetMap = new HashMap<String, List<MedFacet>>();
        response.getFacetFields().forEach (facet -> {
            List<MedFacet> medFacetValues = new ArrayList<MedFacet>();
            for (int idx=0; idx < facet.getValueCount(); idx++) {
                MedFacet fv = new MedFacet();
                fv.setName(facet.getValues().get(idx).getName());
                fv.setCount(facet.getValues().get(idx).getCount());
                medFacetValues.add(fv);

            }
            facetMap.put(facet.getName(), medFacetValues);
        });
        System.out.println("----FACETS--------");
        facetMap.forEach((facetField, facetValues) -> {
            System.out.println("facet field :"+facetField);
            facetValues.forEach(value -> {
                System.out.println(value.getName()+":"+value.getCount());
            });
            System.out.println("------------");

        });





//        List<FacetValues> countryFacet = new ArrayList<FacetValues>();
//        for (int idx=0; idx< fcountry.getValueCount(); idx++) {
//            FacetValues fv = new FacetValues();
//            fv.setCount(fcountry.getValues().get(idx).getCount());
//            fv.setName(fcountry.getValues().get(idx).getName());
//            countryFacet.add(fv);
//        }
//        System.out.println(countryFacet);

//        System.out.println(response.getResults());
//        System.out.println();
//
//        SolrDocumentList docList = response.getResults();
//        final List<Article> articles = response.getBeans(Article.class);

//        for (SolrDocument doc: docList){
//            System.out.println(doc);
//        }

//        final Map<String, String> queryParamMap = new HashMap<String, String>();
//        queryParamMap.put("q", "arttitle:water");
//        queryParamMap.put("fl", flList);
//        queryParamMap.put("rows", "5");
//        queryParamMap.put("facet", "true");
//        queryParamMap.put("sort", "pubdate desc");
//        queryParamMap.put("facet.field", "country");
//        queryParamMap.put("facet.field", "lang");
//        queryParamMap.put("facet.field", "issn");
//        queryParamMap.put("facet.field", "lname");
//        queryParamMap.put("facet.field", "journaltitle");
//        queryParamMap.put("facet.field", "pubyy");
//
//        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
//        QueryResponse response = client.query(queryParams);
//        System.out.println("Header");
//        System.out.println(response.getHeader());
//        System.out.println("Number found : "+response.getResults().getNumFound());
//        System.out.println(response.getFacetFields());
//        System.out.println(response.getResults());



//        final SolrDocumentList documentsts = response.getResults();

//        assertEquals(NUM_INDEXED_DOCUMENTS, documents.getNumFound());
//        for(SolrDocument document : documents) {
//            System.out.println((document.getFieldNames().contains("id")));
//            System.out.println((document.getFieldNames().contains("name")));
//        }
//        final List<Article> articles = response.getBeans(Article.class);
//
//        System.out.println(articles);





        // add functions if needed.
//        getCollectionList(solrUtils.getCloudSolrClient());



        solrUtils.getCloudSolrClient().close();


    }

}
