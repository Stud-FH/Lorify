package fh.server.rest.dao;

import fh.server.constant.SiteVisibility;

import java.util.Map;
import java.util.Set;

public class SiteDAO extends EntityDAO {

    private String name;

    private SiteVisibility visibility;

    private Map<String, String> pages;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SiteVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SiteVisibility visibility) {
        this.visibility = visibility;
    }

    public Map<String, String> getPages() {
        return pages;
    }

    public void setPages(Map<String, String> pages) {
        this.pages = pages;
    }
}
