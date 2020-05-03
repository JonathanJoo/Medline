package solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.*;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.cloud.*;


public class SolrUtils {
    static CloudSolrClient cloudSolrClient;

    @SuppressWarnings("deprecation")

    public  void createCloudSolrClient(String zkHostString){
        this.cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHostString).build();

    }


    public  void createCloudSolrClient(String zkHostString, String collection){
        this.cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHostString).build();
        this.cloudSolrClient.setDefaultCollection(collection);
    }

    public SolrDocumentList getSolrResponse(SolrQuery solrQuery, String collection, CloudSolrClient solrClient) {
        System.out.println(solrClient.toString());
        QueryResponse response = null;
        SolrDocumentList list = null;
        try {
            QueryRequest req = new QueryRequest(solrQuery);
            solrClient.setDefaultCollection(collection);
            response = req.process(solrClient);
            list = response.getResults();
            System.out.println(list.toString());
        } catch (Exception e) {
            e.printStackTrace();//handle errors in this block
        }
        return list;
    }
    public  CloudSolrClient getCloudSolrClient() {
        return cloudSolrClient;
    }

}
