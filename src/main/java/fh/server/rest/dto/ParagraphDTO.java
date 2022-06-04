package fh.server.rest.dto;

import fh.server.constant.ComponentType;

public class ParagraphDTO extends WidgetComponentDTO {

    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.Paragraph;
    }
}
