package fh.server.rest.dao;

import java.util.Map;
import java.util.Set;

public class PageDAO extends EntityDAO {

    private String name;

    private Map<String, String> widgets;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getWidgets() {
        return widgets;
    }

    public void setWidgets(Map<String, String> widgets) {
        this.widgets = widgets;
    }
}
