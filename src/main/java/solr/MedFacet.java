package solr;

public class MedFacet {
    private String name;
    private long count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "FacetValues{" +
                "name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
