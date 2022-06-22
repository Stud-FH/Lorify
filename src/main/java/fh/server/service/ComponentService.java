package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Component;
import fh.server.entity.Resource;
import fh.server.entity.Scope;
import fh.server.repository.*;
import fh.server.rest.dao.ComponentDAO;
import fh.server.rest.dao.ResourceDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComponentService extends ResourceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentService.class);



    protected final ComponentRepository componentRepository;

    public ComponentService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository,
            @Qualifier("componentRepository") ComponentRepository componentRepository
    ) {
        super(entityRepository, resourceRepository, dataRepository);
        this.componentRepository = componentRepository;
    }

    public Component fetchComponent(String path) {
        return componentRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "component not found: "+path));
    }


    @Override
    public Resource create(ResourceDAO dao, Principal principal, Permission permissionRequired, Resource template) {
        Scope parent = dao.getScope();
        if (parent != null) {
            try {
                String position = ((ComponentDAO) dao).getPosition();
                if (position == null || position.isEmpty()) position = "default_0";
                String[] split = position.split("_");
                String prefix = split[0] + "_";
                int suffix = Integer.parseInt(split[1]);
                for (Component c : parent.getComponents()) {
                    String p = c.getPosition();
                    if (p.startsWith(prefix)) {
                        int s2 = Integer.parseInt(p.split("_")[1]);
                        if (s2 >= suffix) suffix = s2 + 1;
                    }
                }
                ((ComponentDAO) dao).setPosition(prefix + suffix);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "position syntax error");
            }
        } else ((ComponentDAO) dao).setPosition("default_0");
        return super.create(dao, principal, permissionRequired, template);
    }

    public Component update(Component victim, ComponentDAO dao, Principal principal) {
        victim = (Component) super.update(victim, dao, principal);
        flush();
        return victim;
    }



    protected Component saveAndFlush(Component created) {
        super.saveAndFlush(created);
        return componentRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        componentRepository.flush();
    }

    protected String typeLabel() {
        return "component";
    }

}
