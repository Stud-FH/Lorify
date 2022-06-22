package fh.server.service;

import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Alias;
import fh.server.entity.Content;
import fh.server.entity.Scope;
import fh.server.repository.*;
import fh.server.rest.dao.ComponentDAO;
import fh.server.rest.dao.ContentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ContentService extends ComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentService.class);



    protected final ContentRepository contentRepository;

    public ContentService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("resourceRepository") ResourceRepository resourceRepository,
            @Qualifier("dataRepository") DataRepository dataRepository,
            @Qualifier("componentRepository") ComponentRepository componentRepository,
            @Qualifier("contentRepository") ContentRepository contentRepository


    ) {
        super(entityRepository, resourceRepository, dataRepository, componentRepository);
        this.contentRepository = contentRepository;
    }

    public Content fetchContent(String path) {
        return contentRepository.findByPath(path)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "content not found"));
    }

    public Content create(Scope scope, ContentDAO dao, Account principal) {
        dao.setScope(scope);
        Content created = (Content) super.create(dao, principal, Permission.AuthorAccess, new Content());
        return saveAndFlush(created);
    }

    public Content update(Content victim, ContentDAO dao, Principal principal) {
        victim = (Content) super.update(victim, dao, principal);
        flush();
        return victim;
    }



    protected Content saveAndFlush(Content created) {
        super.saveAndFlush(created);
        return contentRepository.saveAndFlush(created);
    }

    protected void flush() {
        super.flush();
        contentRepository.flush();
    }

    protected String typeLabel() {
        return "content";
    }

}
