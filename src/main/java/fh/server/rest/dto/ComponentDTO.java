package fh.server.rest.dto;


import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentDTO extends ResourceDTO {

    private String componentClass;
    private String position;

    public ComponentDTO(Component source, Principal principal) {
        super(source, principal);
        if (ps.getResource().meets(Permission.UserView)) {
            componentClass = source.getComponentClass();
            position = source.getPosition();
        }
    }

    public String getComponentClass() {
        return componentClass;
    }

    public String getPosition() {
        return position;
    }
}
