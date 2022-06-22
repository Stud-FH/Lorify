package fh.server.rest.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentDAO extends ComponentDAO {

    private byte[] data;




    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
