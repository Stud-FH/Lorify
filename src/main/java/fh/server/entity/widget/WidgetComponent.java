package fh.server.entity.widget;

import fh.server.constant.ComponentType;
import fh.server.entity.Artifact;

import javax.persistence.*;

@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class WidgetComponent extends fh.server.entity.Entity {

    public abstract ComponentType getComponentType();
}
