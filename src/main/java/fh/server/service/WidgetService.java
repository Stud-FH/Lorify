package fh.server.service;

import fh.server.entity.*;
import fh.server.entity.widget.*;
import fh.server.helpers.Context;
import fh.server.repository.*;
import fh.server.rest.dto.EntityBlueprint;
import fh.server.rest.dto.PageBlueprint;
import fh.server.rest.dto.WidgetBlueprint;
import fh.server.rest.dto.WidgetComponentBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WidgetService extends ArtifactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetService.class);



    protected final WidgetRepository widgetRepository;
    protected final WidgetComponentRepository widgetComponentRepository;
    protected final ParagraphRepository paragraphRepository;
    protected final FileRepository fileRepository;
    protected final PollRepository pollRepository;
    protected final PageRepository pageRepository;

    public WidgetService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository,
            @Qualifier("widgetRepository") WidgetRepository widgetRepository,
            @Qualifier("postElementRepository") WidgetComponentRepository widgetComponentRepository,
            @Qualifier("paragraphRepository") ParagraphRepository paragraphRepository,
            @Qualifier("fileRepository") FileRepository fileRepository,
            @Qualifier("pollRepository") PollRepository pollRepository,
            @Qualifier("pageRepository") PageRepository pageRepository
    ) {
        super(entityRepository, artifactRepository);
        this.widgetRepository = widgetRepository;
        this.widgetComponentRepository = widgetComponentRepository;
        this.paragraphRepository = paragraphRepository;
        this.fileRepository = fileRepository;
        this.pollRepository = pollRepository;
        this.pageRepository = pageRepository;
    }




    public Widget fetchWidget(String id) {
        return widgetRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "widget not found"));
    }

    /**
     * @return  widget, principal, artifact
     */
    public Context buildContext(Widget widget, Account principal) {
        return Context.build()
                .principal(principal)
                .artifact(widget)
                .widget(widget)
                .dispatch();
    }

    public Widget createWidget(WidgetBlueprint blueprint, Account principal) {
        Context context = buildContext(principal);
        checkNotEmpty(blueprint.getComponentKeys(), "componentKeys");
        checkNotNull(blueprint.getComponents());
        for (String key : blueprint.getComponentKeys()) checkNotNull(blueprint.getComponents().get(key));

        Widget widget = new Widget();
        widget.addOwner(principal);
        for (String key : blueprint.getComponentKeys()) widget.putComponent(key, produce(blueprint.getComponents().get(key)));

        widget.getComponents().values().forEach(this::saveAndFlush);
        widget = saveAndFlush(widget);
        LOGGER.info(String.format("widget created. [%s=%s, principal=%s]", typeLabel(), widget, principal));
        return widget;
    }

    protected WidgetComponent produce(WidgetComponentBlueprint blueprint) {
        switch (blueprint.getComponentType()) {
            case File: return new File() {{
                setFilename(blueprint.getFilename());
                setData(blueprint.getData());
                setComments(blueprint.getComments());
                setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());
                addTags(blueprint.getTags());
                putAttributes(blueprint.getAttributes());
            }};
            case Paragraph: return new Paragraph() {{
                setText(blueprint.getText());
                setComments(blueprint.getComments());
                setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());
                addTags(blueprint.getTags());
                putAttributes(blueprint.getAttributes());
            }};
            case Poll: return new Poll() {{
                setFormulation(blueprint.getFormulation());
                setSubmissionGuardDescription(blueprint.getSubmissionGuardDescription());
                setInspectorGuardDescription(blueprint.getInspectorGuardDescription());
                putQuantification(blueprint.getQuantification());
                setComments(blueprint.getComments());
                setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());
                addTags(blueprint.getTags());
                putAttributes(blueprint.getAttributes());
            }};
            default: throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "unexpected component type");
        }
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.verifyUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((WidgetService) service).verifyNameUpdate(((WidgetBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("component")) {
            ((WidgetService) service).verifyComponentUpdate(((WidgetBlueprint) blueprint), context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) { //todo update individual components as well
        super.performUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("name")) {
            ((WidgetService) service).performNameUpdate(((WidgetBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("component")) {
            ((WidgetService) service).performComponentUpdate(((WidgetBlueprint) blueprint), context);
        }
    }

    public void verifyNameUpdate(WidgetBlueprint blueprint, Context context) {
        checkNotNull(context.getWidget());
        checkOwner(context);
        checkNotEmpty(blueprint.getName(), "name");
    }

    public void performNameUpdate(WidgetBlueprint blueprint, Context context) {
        String previous = context.getWidget().getName();
        context.getWidget().setName(blueprint.getName());
        pageRepository.flush();
        LOGGER.info(String.format("name updated: %s -> %s in %s", previous, blueprint.getName(), context));
    }

    protected void verifyComponentUpdate(WidgetBlueprint blueprint, Context context) {
        checkNotNull(context.getWidget());
        checkOwner(context);
        checkNotNull(blueprint.getComponentKeys());
        checkNotNull(blueprint.getComponentIds());
        for (String key : blueprint.getComponentKeys()) {
            checkComponentExistence(blueprint.getComponentIds().get(key));
        }
    }

    protected void performComponentUpdate(WidgetBlueprint blueprint, Context context) {
        StringBuilder update = new StringBuilder();
        for (String key : blueprint.getComponentKeys()) {
            if (key == null ||key.isEmpty()) {
                WidgetComponent previous = context.getWidget().removeComponent(key);
                update.append(String.format("%s\"%s\" removed (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, previous));
            } else {
                WidgetComponent value = widgetComponentRepository.findById(blueprint.getComponentIds().get(key)).orElse(null);
                WidgetComponent previous = context.getWidget().putComponent(key, value);
                update.append(String.format("%s\"%s\" -> \"%s\" (prev:\"%s\")", update.length() == 0 ? "{" : ", ", key, value, previous));
            }
        }
        update.append("}");
        widgetRepository.flush();
        LOGGER.info(String.format("components updated: %s in %s", update, context));
    }

    private void checkComponentExistence(String id) {
        if (id == null || id.isEmpty()) return;
        if (!widgetComponentRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "component not found: "+id);
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
