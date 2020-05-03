package solr;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.common.util.NamedList;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class WorkingTools {
    public static void main(String[] args) throws IOException, SolrServerException {


        SolrUtils solrUtils = new SolrUtils();

        String zkHosts = "hddsc1db001dxc1.dev.oclc.org:2181/solr";
        solrUtils.createCloudSolrClient(zkHosts);

        // add functions if needed.
        getCollectionList(solrUtils.getCloudSolrClient());



        solrUtils.getCloudSolrClient().close();

    }

    public static void getCollectionList(CloudSolrClient solrClient) throws IOException, SolrServerException {
        CollectionAdminRequest collectionAdminRequest = new CollectionAdminRequest.ClusterStatus();

        NamedList<Object> collectionAdminResponse = solrClient.request(collectionAdminRequest);
        NamedList<Object> collections = (NamedList<Object>)((NamedList<Object>)collectionAdminResponse.get("cluster")).get("collections");
        Iterator<Map.Entry<String, Object>> iterator = collections.iterator();
        while (iterator.hasNext()){

            System.out.println(iterator.next().getKey());
        }
    }





}
