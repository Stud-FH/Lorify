package fh.server.entity;

import fh.server.constant.Permission;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.rest.dao.ComponentDAO;
import fh.server.rest.dao.ResourceDAO;

import javax.persistence.*;

@javax.persistence.Entity
public abstract class Component extends Resource {

    @Column(nullable = false, length = 50)
    private String componentClass;

    @Column(nullable = false, length = 50)
    private String position;



    public String getComponentClass() {
        return componentClass;
    }

    public void setComponentClass(String componentClass) {
        this.componentClass = componentClass;
        setLastModified(System.currentTimeMillis());
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public PermissionSetting verifyAccess(ResourceDAO resourceDAO, Principal principal) {
        PermissionSetting ps = super.verifyAccess(resourceDAO, principal);
        ComponentDAO dao = (ComponentDAO) resourceDAO;
        if (dao.getComponentClass() != null) {
            ps.getResource().require(Permission.ExclusiveAccess);
        }
        if (dao.getPosition() != null) {
            ps.getResource().require(Permission.AuthorAccess);
        }
        return ps;
    }

    @Override
    public ComponentDAO adapt(ResourceDAO resourceDAO, ResourceDAO logObject) {
        if (logObject == null) logObject = new ComponentDAO();
        super.adapt(resourceDAO, logObject);
        ComponentDAO dao = (ComponentDAO) resourceDAO;
        ComponentDAO lo = (ComponentDAO) logObject;

        if (dao.getComponentClass() != null) {
            lo.setComponentClass(componentClass);
            setComponentClass(dao.getComponentClass());
        }
        if (dao.getPosition() != null) {
            lo.setPosition(position);
            setPosition(dao.getPosition());
        }

        return lo;
    }
}
