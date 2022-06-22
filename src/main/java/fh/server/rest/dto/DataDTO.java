package fh.server.rest.dto;


import fh.server.constant.DataType;
import fh.server.entity.Data;

public class DataDTO extends EntityDTO {

    private Integer length;
    private byte[] data;
    private DataType dataType;

    public DataDTO(Data source) {
        super(source);
        length = source.getLength();
        data = source.getData();
        dataType = source.getDataType();
    }

    public Integer getLength() {
        return length;
    }

    public byte[] getData() {
        return data;
    }

    public DataType getDataType() {
        return dataType;
    }
}
