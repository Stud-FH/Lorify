package fh.server.entity.widget;

import fh.server.constant.ComponentType;
@javax.persistence.Entity
public abstract class WidgetComponent extends fh.server.entity.Entity {

    public abstract ComponentType getComponentType();
}
