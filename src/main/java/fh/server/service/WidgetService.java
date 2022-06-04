package fh.server.service;

import fh.server.entity.*;
import fh.server.entity.widget.*;
import fh.server.helpers.Context;
import fh.server.helpers.Operation;
import fh.server.repository.*;
import fh.server.rest.dao.EntityDAO;
import fh.server.rest.dao.SiteDAO;
import fh.server.rest.dao.WidgetDAO;
import fh.server.rest.dao.WidgetComponentDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WidgetService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetService.class);



    protected final WidgetRepository widgetRepository;
    protected final WidgetComponentRepository widgetComponentRepository;
    protected final ParagraphRepository paragraphRepository;
    protected final FileRepository fileRepository;
    protected final PollRepository pollRepository;
    protected final PageRepository pageRepository;

    public WidgetService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("widgetRepository") WidgetRepository widgetRepository,
            @Qualifier("postElementRepository") WidgetComponentRepository widgetComponentRepository,
            @Qualifier("paragraphRepository") ParagraphRepository paragraphRepository,
            @Qualifier("fileRepository") FileRepository fileRepository,
            @Qualifier("pollRepository") PollRepository pollRepository,
            @Qualifier("pageRepository") PageRepository pageRepository
    ) {
        super(entityRepository);
        this.widgetRepository = widgetRepository;
        this.widgetComponentRepository = widgetComponentRepository;
        this.paragraphRepository = paragraphRepository;
        this.fileRepository = fileRepository;
        this.pollRepository = pollRepository;
        this.pageRepository = pageRepository;
    }




    public Widget fetchWidgetById(String id) {
        return widgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "widget not found"));
    }

    public WidgetComponent fetchComponentById(String id) {
        return widgetComponentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "component not found"));
    }

    public Widget createWidget(WidgetDAO dao, Account principal) {

        Widget widget = new Widget();
        widget.adapt(dao);
        widget.setName(dao.getName());
        widget.setOwner(principal);
        for (String key : dao.getComponentKeys()) widget.putComponent(key, produce(dao.getComponents().get(key), principal));

        Context context = new Context(principal, null);
        checkNotEmpty(dao.getComponentKeys(), "componentKeys");
        checkNotNull(dao.getComponents());
        for (String key : dao.getComponentKeys()) checkNotNull(dao.getComponents().get(key));

        widget.getComponents().values().forEach(this::saveAndFlush);
        widget = saveAndFlush(widget);
        LOGGER.info(String.format("widget created. [%s=%s, principal=%s]", typeLabel(), widget, principal));
        return widget;
    }

    protected WidgetComponent produce(WidgetComponentDAO dao, Account principal) {
        checkNotNull(dao.getComponentType());
        switch (dao.getComponentType()) {
            case File:
                File file = new File();
                file.adapt(dao);
                file.setOwner(principal);
                file.setFilename(dao.getFilename());
                file.setData(dao.getData());
                return file;
            case Paragraph:
                Paragraph paragraph = new Paragraph();
                paragraph.adapt(dao);
                paragraph.setOwner(principal);
                paragraph.setText(dao.getText());
                return paragraph;
            case Poll:
                Poll poll = new Poll();
                poll.adapt(dao);
                poll.setOwner(principal);
                poll.setFormulation(dao.getFormulation());
                poll.putQuantification(dao.getQuantification());
                return poll;
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected component type");
        }
    }

    public void putWidget(Widget widget, String pageId, String position, Entity principal) {
        //TODO verify, make method more elegant
        Page page = pageRepository.findById(pageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "page not found"));
        if (position == null) {
            int index = 0;
            while (page.hasWidget((position = ""+index))) index++;
        }
        page.putWidget(position, widget);
        pageRepository.flush();
    }

    public Widget operate(Widget widget, WidgetDAO dao, Entity principal) {
        super.operate(dao, new Context(principal, widget), this);
        return widget;
    }

    protected Operation setup(String opKey, EntityDAO dao, Context context) {
        WidgetDAO widgetDao = (WidgetDAO) dao;

        if ("name".equals(opKey)) {
            checkNotEmpty(widgetDao.getName(), "name");
            return context.operation(opKey, widgetDao.getName(), context.victimAsSite().getName());
        }

        if (opKey.startsWith("Widget.c:")) {
            String key = opKey.substring( "Widget.c:".length());
            String componentId = widgetDao.getComponentIds().get(key);
            return context.operation(opKey, fetchComponentById(componentId), context.victimAsSite().getPage(key));
        }
        return super.setup(opKey, dao, context);
    }

    protected void execute(Operation operation, EntityDAO dao) {
        WidgetDAO widgetDao = (WidgetDAO) dao;
        String opKey = operation.getOperation();

        switch (opKey) {
            case "get": return;
            case "name": operation.victimAsWidget().setName(widgetDao.getName()); return;
        }
        if (opKey.startsWith("Widget.c:")) {
            String key = opKey.substring( "Widget.c:".length());
            operation.valueAsWidget().putComponent(key, operation.valueAsComponent());
            return;
        }
        super.execute(operation, dao);
    }

    protected WidgetComponent saveAndFlush(WidgetComponent created) {
        super.saveAndFlush(created);
        widgetComponentRepository.saveAndFlush(created);
        switch (created.getComponentType()) {
            case Paragraph: return paragraphRepository.saveAndFlush((Paragraph) created);
            case File: return fileRepository.saveAndFlush((File) created);
            case Poll: return pollRepository.saveAndFlush((Poll) created);
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected component type");
        }
    }

    protected Widget saveAndFlush(Widget created) {
        super.saveAndFlush(created);
        return widgetRepository.saveAndFlush(created);
    }

    protected String typeLabel() {
        return "widget";
    }

}
