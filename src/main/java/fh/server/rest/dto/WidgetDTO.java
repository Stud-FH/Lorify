package fh.server.rest.dto;

import fh.server.entity.widget.WidgetComponent;
import fh.server.rest.mapper.DTOMapper;

import java.util.HashMap;
import java.util.Map;

public class WidgetDTO extends ArtifactDTO {

    private Map<String, WidgetComponentDTO> components;


    public Map<String, WidgetComponentDTO> getComponents() {
        return components;
    }

    public void setComponents(Map<String, WidgetComponent> components) {
        this.components = new HashMap<>();
        components.keySet().forEach(k -> this.components.put(k, DTOMapper.INSTANCE.map(components.get(k))));
    }
}
