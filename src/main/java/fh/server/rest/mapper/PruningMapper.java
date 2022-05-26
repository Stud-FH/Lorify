package fh.server.rest.mapper;

import fh.server.constant.TrustLevel;
import fh.server.entity.Alias;
import fh.server.entity.Entity;
import fh.server.entity.Page;
import fh.server.entity.Site;
import fh.server.entity.widget.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PruningMapper {

    PruningMapper INSTANCE = Mappers.getMapper(PruningMapper.class);

    default Entity prune(Entity source, Entity result, Entity principal) {
        TrustLevel principalClearance = source.getPruningClearance(principal);

        if (principalClearance.meets(source.getAccessRequirement("get-id:"))) result.setId(source.getId());
        if (principalClearance.meets(source.getAccessRequirement("get-parentId:"))) result.setParentId(source.getParentId());
        if (principalClearance.meets(source.getAccessRequirement("get-ownerId:"))) result.setOwnerId(source.getOwnerId());

        if (principalClearance.meets(source.getAccessRequirement("get-attributes"))) {
            for (String k : source.getAttributes().keySet()) {
                if (principalClearance.meets(source.getAccessRequirement("get-a:"+k))) result.putAttribute(k, source.getAttribute(k));
            }
        }

        if (principalClearance.meets(source.getAccessRequirement("get-guards"))) {
            for (String k : source.getGuards().keySet()) {
                if (principalClearance.meets(source.getAccessRequirement("get-g:"+k))) result.putGuard(k, source.getGuard(k));
            }
        }

        if (principalClearance.meets(source.getAccessRequirement("get-requirements"))) {
            for (String k : source.getAccessRequirements().keySet()) {
                if (principalClearance.meets(source.getAccessRequirement("get-r:"+k))) result.putAccessRequirements(k, source.getAccessRequirement(k));
            }
        }

        return result;
    }

    default Site prune(Site source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Site result = new Site();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-name:"))) result.setName(source.getName());
        if (principalClearance.meets(source.getAccessRequirement("get-visibility:"))) result.setVisibility(source.getVisibility());

        for (String k : source.getAliases().keySet()) {
            Alias alias = prune(source.getAlias(k), principal);
            if (alias != null) result.getAliases().put(alias.getName(), alias);
        }

        for (String k : source.getPages().keySet()) {
            Page page = prune(source.getPage(k), principal);
            if (page != null) result.getPages().put(k, page);
        }

        return result;
    }

    default Alias prune(Alias source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Alias result = new Alias();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-name:"))) result.setName(source.getName());
        if (principalClearance.meets(source.getAccessRequirement("get-accessor:"))) result.setAccessor(source.getAccessor());
        if (principalClearance.meets(source.getAccessRequirement("get-claimed:"))) result.setClaimed(source.getClaimed());

        return result;
    }

    default Page prune(Page source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Page result = new Page();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-name:"))) result.setName(source.getName());

        for (String k : source.getWidgets().keySet()) {
            Widget widget = prune(source.getWidget(k), principal);
            if (widget != null) result.getWidgets().put(k, widget);
        }

        return result;
    }

    default Widget prune(Widget source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Widget result = new Widget();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-name:"))) result.setName(source.getName());

        for (String k : source.getComponents().keySet()) {
            WidgetComponent component = prune(source.getComponent(k), principal);
            if (component != null) result.getComponents().put(k, component);
        }

        return result;
    }

    default WidgetComponent prune(WidgetComponent source, Entity principal) {
        switch (source.getComponentType()) {
            case Paragraph: return prune((Paragraph) source, principal);
            case File: return prune((File) source, principal);
            case Poll: return prune((Poll) source, principal);
            default: return null;
        }
    }

    default Paragraph prune(Paragraph source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Paragraph result = new Paragraph();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-text:"))) result.setText(source.getText());

        return result;
    }

    default File prune(File source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        File result = new File();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-filename:"))) result.setFilename(source.getFilename());
        if (principalClearance.meets(source.getAccessRequirement("get-data:"))) result.setData(source.getData());

        return result;
    }

    default Poll prune(Poll source, Entity principal) {

        TrustLevel principalClearance = source.getPruningClearance(principal);
        if (!principalClearance.meets(source.getAccessRequirement("get"))) return null;
        Poll result = new Poll();
        prune(source, result, principal);

        if (principalClearance.meets(source.getAccessRequirement("get-formulation:"))) result.setFormulation(source.getFormulation());
        if (principalClearance.meets(source.getAccessRequirement("get-submissions:"))) result.getSubmissions().putAll(source.getSubmissions());
        if (principalClearance.meets(source.getAccessRequirement("get-quantification:"))) result.getQuantification().putAll(source.getQuantification());

        return result;
    }
}
