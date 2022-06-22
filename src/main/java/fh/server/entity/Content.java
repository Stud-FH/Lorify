package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.Permission;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.rest.dao.ContentDAO;
import fh.server.rest.dao.ResourceDAO;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@javax.persistence.Entity
public class Content extends Component {

    @OneToOne(orphanRemoval = true)
    @JoinColumn
    private final Data data = new Data();




    public Data getData() {
        return data;
    }

    public PermissionSetting verifyAccess(ResourceDAO resourceDAO, Principal principal) {
        PermissionSetting ps = super.verifyAccess(resourceDAO, principal);
        ContentDAO dao = (ContentDAO) resourceDAO;

        if (dao.getData() != null) {
            ps.getResource().require(Permission.AuthorAccess);
        }

        return ps;
    }

    public ContentDAO adapt(ResourceDAO resourceDAO, ResourceDAO logObject) {
        if (logObject == null) logObject = new ContentDAO();
        super.adapt(resourceDAO, logObject);
        ContentDAO dao = (ContentDAO) resourceDAO;
        ContentDAO lo = (ContentDAO) logObject;

        if (dao.getData() != null) {
//            lo.setData(data.readBytes());
            data.write(dao.getData());
        }

        return lo;
    }

    // todo size

    @Override
    public EntityType getType() {
        return EntityType.Paragraph;
    }
    
}
