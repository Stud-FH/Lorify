package fh.server.rest.dto;

import fh.server.constant.ComponentType;

public class FileDTO extends WidgetComponentDTO {

    private String filename;

    private byte[] data;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.File;
    }
}
