package fh.server.rest.dto;

import fh.server.constant.ComponentType;

public class WidgetComponentDTO extends ArtifactDTO {

    private ComponentType componentType;




    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }
}
