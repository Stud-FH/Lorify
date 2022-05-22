package fh.server.service;

import fh.server.entity.Account;
import fh.server.entity.Artifact;
import fh.server.helpers.Context;
import fh.server.repository.ArtifactRepository;
import fh.server.repository.EntityRepository;
import fh.server.rest.dto.ArtifactBlueprint;
import fh.server.rest.dto.EntityBlueprint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ArtifactService extends EntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtifactService.class);


    protected final ArtifactRepository artifactRepository;

    public ArtifactService(
            @Qualifier("entityRepository") EntityRepository entityRepository,
            @Qualifier("artifactRepository") ArtifactRepository artifactRepository

    ) {
        super(entityRepository);
        this.artifactRepository = artifactRepository;
    }


    public Context buildContext(Artifact artifact, Account principal) {
        return Context.build()
                .principal(principal)
                .artifact(artifact)
                .dispatch();
    }

    protected void verifyUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.verifyUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("comment")) {
            ((ArtifactService) service).verifyCommentUpdate(((ArtifactBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("visibilityGuardDescription")) {
            ((ArtifactService) service).verifyVisGuardUpdate(((ArtifactBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("tag")) {
            ((ArtifactService) service).verifyTagUpdate(((ArtifactBlueprint) blueprint), context);
        }
    }

    protected void performUpdate(EntityBlueprint blueprint, Context context, EntityService service) {
        super.performUpdate(blueprint, context, service);

        if (blueprint.getChangelist().contains("comment")) {
            ((ArtifactService) service).performCommentUpdate(((ArtifactBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("visibilityGuardDescription")) {
            ((ArtifactService) service).performVisGuardUpdate(((ArtifactBlueprint) blueprint), context);
        }
        if (blueprint.getChangelist().contains("tag")) {
            ((ArtifactService) service).performTagUpdate(((ArtifactBlueprint) blueprint), context);
        }
    }

    public void verifyCommentUpdate(ArtifactBlueprint blueprint, Context context) {
        checkNotNull(context.getArtifact());
        checkOwner(context);
    }

    public void performCommentUpdate(ArtifactBlueprint blueprint, Context context) {
        String previous = context.getArtifact().getComments();
        context.getArtifact().setComments(blueprint.getComments());
        artifactRepository.flush();
        LOGGER.info(String.format("comments updated: %s -> %s in %s", previous, blueprint.getComments(), context));
    }

    public void verifyVisGuardUpdate(ArtifactBlueprint blueprint, Context context) {
        checkNotNull(context.getArtifact());
        checkOwner(context);
    }

    public void performVisGuardUpdate(ArtifactBlueprint blueprint, Context context) {
        String previous = context.getArtifact().getVisibilityGuardDescription();
        context.getArtifact().setVisibilityGuardDescription(blueprint.getVisibilityGuardDescription());
        artifactRepository.flush();
        LOGGER.info(String.format("visibilityGuardDescription updated: %s -> %s in %s", previous, blueprint.getVisibilityGuardDescription(), context));
    }

    protected void verifyTagUpdate(ArtifactBlueprint blueprint, Context context) {
        checkNotNull(context.getArtifact());
        checkOwner(context);
        for (String tag : blueprint.getTags()) checkNotEmpty(tag, "tag");
        for (String tag : blueprint.getAntiTags()) checkNotEmpty(tag, "tag");
    }

    protected void performTagUpdate(ArtifactBlueprint blueprint, Context context) {
        for (String tag : blueprint.getTags()) context.getArtifact().addTag(tag);
        for (String tag : blueprint.getAntiTags()) context.getArtifact().removeTag(tag);
        artifactRepository.flush();
        LOGGER.info(String.format("tags updated: +%s ¬%s in %s", blueprint.getTags(), blueprint.getAntiTags(), context));
    }

    protected Artifact saveAndFlush(Artifact created) {
        super.saveAndFlush(created);
        return artifactRepository.saveAndFlush(created);
    }

    protected String typeLabel() {
        return "artifact";
    }

}
