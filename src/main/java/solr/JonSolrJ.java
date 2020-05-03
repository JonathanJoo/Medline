package solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class JonSolrJ {
    //connect the solr
    // st - by SOlr URL
    // 2nd - by zookeeper.  (SolrCloud)
    public static void main(String[] args) throws IOException, SolrServerException {
        // connecting Solr -
        String zkHost="ec2-18-191-198-32.us-east-2.compute.amazonaws.com:2181";
        String collection="medline";
        CloudSolrClient  client = new CloudSolrClient.Builder().withZkHost(zkHost).build();
        client.setDefaultCollection(collection);
        String[] facetFields= new String[] {"country", "lang", "issntype"};

        // set the query
        SolrQuery query = new SolrQuery();
        query.set("q", "arttitle:dog");
        query.setParam("rows", "5");
        query.setParam("fl", "arttitle");
        query.setParam("facet", "true");
        query.setParam("facet.field", facetFields);
        query.setParam("facet.limit", "5");
        query.setParam("sort", "pubdate desc");


        //execute the query
        QueryResponse response = client.query(query);
        System.out.println(response.toString());

        //get the numfound and qtime.
        int qtime = response.getQTime();
        long numFound=response.getResults().getNumFound();
        System.out.println("qtime="+qtime);
        System.out.println("numFound="+numFound);
        System.out.println(response);

        //get the matched documents.
//        SolrDocumentList docList = response.getResults();
//        System.out.println("docList="+docList.toString());
//        for (int idx=0; idx<docList.size(); idx++) {
//            SolrDocument sd=docList.get(idx);
//            System.out.println("name="+sd.getFieldNames());
//            System.out.println("values="+sd.getFieldValue("arttitle"));
//        }

        // get facet
        FacetField fcountry = response.getFacetField("country");
        System.out.println(fcountry);
        System.out.println();
        FacetField flang = response.getFacetField("lang");
        System.out.println(flang);

        // get multiple facetfields.
        System.out.println(response.getFacetFields());
        System.out.println("get multiple facets");
        response.getFacetFields().forEach (facet -> {
            List<FacetField.Count> facetCountList = facet.getValues();
            facetCountList.forEach(count -> {
                System.out.println(count.getName()+":"+count.getCount());
            });
        });
//https://medium.com/@ahmetkapusuz/spring-boot-hello-world-application-with-intellij-idea-1524c68ddaae
//        ec2-18-222-184-35.us-east-2.compute.amazonaws.com:8983/solr/




















        client.close();

    }






}
