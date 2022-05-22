package fh.server.rest.dto;

public class ParagraphDTO extends WidgetComponentDTO {

    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
