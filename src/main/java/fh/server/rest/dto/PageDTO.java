package fh.server.rest.dto;


import fh.server.entity.widget.Widget;
import fh.server.rest.mapper.DTOMapper;

import java.util.HashMap;
import java.util.Map;

public class PageDTO extends ArtifactDTO {

    private String name;

    private String siteId;

    private String creatorGuardDescription;

    private Map<String, WidgetDTO> widgets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getCreatorGuardDescription() {
        return creatorGuardDescription;
    }

    public void setCreatorGuardDescription(String creatorGuardDescription) {
        this.creatorGuardDescription = creatorGuardDescription;
    }

    public Map<String, WidgetDTO> getWidgets() {
        return widgets;
    }

    public void setWidgets(Map<String, Widget> widgets) {
        this.widgets = new HashMap<>();
        widgets.keySet().forEach(k -> this.widgets.put(k, DTOMapper.INSTANCE.map(widgets.get(k))));
    }
}
