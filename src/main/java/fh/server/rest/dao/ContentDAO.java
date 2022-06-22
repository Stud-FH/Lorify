package fh.server.rest.dao;

public class ContentDAO extends ComponentDAO {

    private byte[] data;




    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
