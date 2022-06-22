package fh.server.rest.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComponentDAO extends ResourceDAO {

    private String componentClass;
    private String position;




    public String getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
