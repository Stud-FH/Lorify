package fh.server.entity.widget;

import fh.server.constant.ComponentType;

import javax.persistence.Basic;
import javax.persistence.Column;

@javax.persistence.Entity
public class File extends WidgetComponent {

    @Column(nullable = false)
    private String filename;

    @Basic
    private byte[] data;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
        setLastModified(System.currentTimeMillis());
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.File;
    }
}
