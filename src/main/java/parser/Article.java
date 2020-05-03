package parser;

import org.apache.solr.client.solrj.beans.Field;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Article {


    @Field private String pmid;
    @Field private String id;
    @Field private int refcnt;
    @Field private String status;
    @Field private String nlmid;
    @Field private String country;
    @Field private String issnl;
    @Field private String medta;
    @Field private String owner;
    @Field private List<String> citsubset = new ArrayList<String>();
    @Field private List<String> pubtype = new ArrayList<String>();
    @Field private List<String> lname = new ArrayList<String>();
    @Field private List<String> fullname = new ArrayList<String>();
    @Field private List<String> mesh = new ArrayList<String>();
    @Field private String abs;
    @Field private List<String> lang = new ArrayList<String>();
    @Field private String arttitle;
    @Field private String issue;
    @Field private String vol;
    @Field private String pubmm;
    @Field private String pubyy;
    @Field private String pubdate;
    @Field private String isoabb;
    @Field private String issn;
    @Field private String issntype;
    @Field private String journaltitle;
    @Field private String vertitle;

    public Article() {
    }

    public Article(String pmid, String id, int refcnt, String status, String nlmid, String country, String issnl, String medta, String owner, List<String> citsubset,
                   List<String> pubtype, List<String> lname, List<String> fullname, List<String> mesh, String abs,
                   List<String> lang, String arttitle, String issue, String vol, String pubmm, String pubyy, String pubdate,
                   String isoabb, String issn, String issntype, String journaltitle, String vertitle, String xmlfile) {
        this.pmid = pmid;
        this.id = id;
        this.refcnt = refcnt;
        this.status = status;
        this.nlmid = nlmid;
        this.country = country;
        this.issnl = issnl;
        this.medta = medta;
        this.owner = owner;
        this.citsubset = citsubset;
        this.pubtype = pubtype;
        this.lname = lname;
        this.fullname = fullname;
        this.mesh = mesh;
        this.abs = abs;
        this.lang = lang;
        this.arttitle = arttitle;
        this.issue = issue;
        this.vol = vol;
        this.pubmm = pubmm;
        this.pubyy = pubyy;
        this.pubdate = pubdate;
        this.isoabb = isoabb;
        this.issn = issn;
        this.issntype = issntype;
        this.journaltitle = journaltitle;
        this.vertitle = vertitle;
        this.xmlfile = xmlfile;
    }

    public String getXmlfile() {
        return xmlfile;
    }

    public void setXmlfile(String xmlfile) {
        this.xmlfile = xmlfile;
    }

    private String xmlfile;


    public String getPmid() {
        return pmid;
    }

    public void setPmid(String pmid) {
        this.pmid = pmid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRefcnt() {
        return refcnt;
    }

    public void setRefcnt(int refcnt) {
        this.refcnt = refcnt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNlmid() {
        return nlmid;
    }

    public void setNlmid(String nlmid) {
        this.nlmid = nlmid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIssnl() {
        return issnl;
    }

    public void setIssnl(String issnl) {
        this.issnl = issnl;
    }

    public String getMedta() {
        return medta;
    }

    public void setMedta(String medta) {
        this.medta = medta;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getCitsubset() {
        return citsubset;
    }

    public void setCitsubset(List<String> citsubset) {
        this.citsubset = citsubset;
    }

    public List<String> getPubtype() {
        return pubtype;
    }

    public void setPubtype(List<String> pubtype) {
        this.pubtype = pubtype;
    }

    public List<String> getLname() {
        return lname;
    }

    public void setLname(List<String> lname) {
        this.lname = lname;
    }

    public List<String> getFullname() {
        return fullname;
    }

    public void setFullname(List<String> fullname) {
        this.fullname = fullname;
    }

    public List<String> getMesh() {
        return mesh;
    }

    public void setMesh(List<String> mesh) {
        this.mesh = mesh;
    }

    public String getAbs() {
        return abs;
    }

    public void setAbs(String abs) {
        this.abs = abs;
    }

    public List<String> getLang() {
        return lang;
    }

    public void setLang(List<String> lang) {
        this.lang = lang;
    }

    public String getArttitle() {
        return arttitle;
    }

    public void setArttitle(String arttitle) {
        this.arttitle = arttitle;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getPubmm() {
        return pubmm;
    }

    public void setPubmm(String pubmm) {
        this.pubmm = pubmm;
    }

    public String getPubyy() {
        return pubyy;
    }

    public void setPubyy(String pubyy) {
        this.pubyy = pubyy;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getIsoabb() {
        return isoabb;
    }

    public void setIsoabb(String isoabb) {
        this.isoabb = isoabb;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getIssntype() {
        return issntype;
    }

    public void setIssntype(String issntype) {
        this.issntype = issntype;
    }

    public String getJournaltitle() {
        return journaltitle;
    }

    public void setJournaltitle(String journaltitle) {
        this.journaltitle = journaltitle;
    }

    public String getVertitle() {
        return vertitle;
    }

    public void setVertitle(String vertitle) {
        this.vertitle = vertitle;
    }


    @Override
    public String toString() {
        return "Article{" +
                "pmid='" + pmid + '\'' +
                ", id='" + id + '\'' +
                ", refcnt=" + refcnt +
                ", status='" + status + '\'' +
                ", nlmid='" + nlmid + '\'' +
                ", country='" + country + '\'' +
                ", issnl='" + issnl + '\'' +
                ", medta='" + medta + '\'' +
                ", owner='" + owner + '\'' +
                ", citsubset=" + citsubset +
                ", pubtype=" + pubtype +
                ", lname=" + lname +
                ", fullname=" + fullname +
                ", mesh=" + mesh +
                ", abs='" + abs + '\'' +
                ", lang=" + lang +
                ", arttitle='" + arttitle + '\'' +
                ", issue='" + issue + '\'' +
                ", vol='" + vol + '\'' +
                ", pubmm='" + pubmm + '\'' +
                ", pubyy='" + pubyy + '\'' +
                ", pubdate='" + pubdate + '\'' +
                ", isoabb='" + isoabb + '\'' +
                ", issn='" + issn + '\'' +
                ", issntype='" + issntype + '\'' +
                ", journaltitle='" + journaltitle + '\'' +
                ", vertitle='" + vertitle + '\'' +
                ", xmlfile='" + xmlfile + '\'' +
                '}';
    }


}