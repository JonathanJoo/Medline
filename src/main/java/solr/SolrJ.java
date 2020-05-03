package solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolrJ {

    public static void main (String[] agrs) throws IOException, SolrServerException {
        final String solrUrl = "http://ec2-18-191-198-32.us-east-2.compute.amazonaws.com:8983/solr/medline";
//        HttpSolrClient client = new HttpSolrClient.Builder(solrUrl).build();
//
//
//        final SolrQuery query = new SolrQuery();
//        query.set("q", "*:*");
//        query.addField("id");
//        QueryResponse response = client.query(query);
//
//        SolrDocumentList docList = response.getResults();
//
//        for (SolrDocument doc: docList){
//            System.out.println(doc.get("id"));
//       }





//        final QueryResponse response = client.query( query);
//        //final List<Nested> products = response.getBeans(Nested.class);
//        SolrDocumentList docList = response.getResults();
//        for (SolrDocument doc: docList){
//            System.out.println(doc.get("id"));
//        }



    }




}

class Nested {
    @Field
    public String id;
    @Field public String title;

    public Nested(String id, String title) {
        this.id = id;  this.title = title;
    }

    public Nested() {}
}

