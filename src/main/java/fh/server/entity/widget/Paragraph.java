package fh.server.entity.widget;

import fh.server.constant.ComponentType;
import fh.server.constant.EntityType;

import javax.persistence.Column;

@javax.persistence.Entity
public class Paragraph extends WidgetComponent {

    @Column(length = 2000)
    private String text;




    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.Paragraph;
    }

    @Override
    public EntityType getType() {
        return EntityType.Paragraph;
    }
}
