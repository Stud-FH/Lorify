package fh.server.rest.dto;

import java.util.Map;
import java.util.Set;

public class PageBlueprint extends ArtifactBlueprint {

    private String name;

    private String creatorGuardDescription;

    private Map<String, String> widgets;

    private Set<String> widgetKeys;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorGuardDescription() {
        return creatorGuardDescription;
    }

    public void setCreatorGuardDescription(String creatorGuardDescription) {
        this.creatorGuardDescription = creatorGuardDescription;
    }

    public Map<String, String> getWidgets() {
        return widgets;
    }

    public void setWidgets(Map<String, String> widgets) {
        this.widgets = widgets;
    }

    public Set<String> getWidgetKeys() {
        return widgetKeys;
    }

    public void setWidgetKeys(Set<String> widgetKeys) {
        this.widgetKeys = widgetKeys;
    }
}
