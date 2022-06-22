package fh.server.rest.dao;

import java.util.Map;
import java.util.Set;

public class WidgetDAO extends EntityDAO {

    private String name;

    private Map<String, ComponentDAO> components;

    private Map<String, String> componentIds;

    private Set<String> componentKeys;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, ComponentDAO> getComponents() {
        return components;
    }

    public void setComponents(Map<String, ComponentDAO> components) {
        this.components = components;
    }

    public Map<String, String> getComponentIds() {
        return componentIds;
    }

    public void setComponentIds(Map<String, String> componentIds) {
        this.componentIds = componentIds;
    }

    public Set<String> getComponentKeys() {
        return componentKeys;
    }

    public void setComponentKeys(Set<String> componentKeys) {
        this.componentKeys = componentKeys;
    }
}
