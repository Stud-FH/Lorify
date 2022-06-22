package fh.server.entity;

import fh.server.constant.DataType;
import fh.server.constant.EntityType;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@javax.persistence.Entity
public class Data extends Entity {

    protected static final int SLICE_LENGTH = 255;
    protected static final long SLICE_SIZE = SLICE_LENGTH +2;

    @Column
    private Integer length = 0;

    @ElementCollection
    @Column(length = SLICE_LENGTH)
    private final List<byte[]> slices = new java.util.ArrayList<>();

    @Column
    private DataType dataType = DataType.ByteArray;


    public Integer getLength() {
        return length;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String readString() {
        StringBuilder s = new StringBuilder();
        for (byte[] b : slices) s.append(new String(b));
        return s.toString();
    }

    public byte[] readBytes() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            for (byte[] a : slices) stream.write(a);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
    }

    public byte[] getData() {
        return readBytes();
    }

    public void writeString(String s) {
        write(s.getBytes());
        dataType = DataType.String;
    }

    public void write(byte[] array) {
        slices.clear();
        if (array == null) {
            this.length = 0;
            return;
        }
        this.length = array.length;
        int i;
        for (i = 0; i + SLICE_LENGTH < array.length; i += SLICE_LENGTH) {
            slices.add(Arrays.copyOfRange(array, i, i + SLICE_LENGTH));
        }
        slices.add(Arrays.copyOfRange(array, i, array.length));
        dataType = DataType.ByteArray;
        setLastModified(System.currentTimeMillis());
    }

    public long size() {
        return RAW_ENTITY_SIZE
                + SLICE_SIZE * slices.size();
    }

    @Override
    public EntityType getType() {
        return EntityType.Data;
    }
}
