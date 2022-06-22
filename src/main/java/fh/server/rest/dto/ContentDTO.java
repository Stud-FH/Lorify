package fh.server.rest.dto;


import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Content;

public class ContentDTO extends ComponentDTO {

    private DataDTO data;

    public ContentDTO(Content source, Principal principal) {
        super(source, principal);

        if (ps.getResource().meets(Permission.UserView)) {
            data = new DataDTO(source.getData());
        }
    }

    public DataDTO getData() {
        return data;
    }
}
