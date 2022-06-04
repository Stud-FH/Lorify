package fh.server.rest.dto;

import fh.server.constant.ComponentType;

public abstract class WidgetComponentDTO extends EntityDTO {

    private ComponentType componentType;



    public abstract ComponentType getComponentType();

}
