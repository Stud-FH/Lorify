package fh.server.rest.dto;


import fh.server.constant.TrustLevel;
import fh.server.entity.widget.Widget;
import fh.server.rest.mapper.DTOMapper;

import java.util.HashMap;
import java.util.Map;

public class PageDTO extends EntityDTO {

    private String name;

    private Map<String, WidgetDTO> widgets;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, WidgetDTO> getWidgets() {
        return widgets;
    }

    public void setWidgets(Map<String, Widget> widgets) {
        this.widgets = new HashMap<>();
        widgets.keySet().forEach(k -> this.widgets.put(k, DTOMapper.INSTANCE.map(widgets.get(k))));
    }

    @Override
    public PageDTO prune(TrustLevel principalClearance) {
        if (!principalClearance.meets(accessRequirements.get("get-name"))) name = null;
        if (!principalClearance.meets(accessRequirements.get("get-widgets"))) widgets = null;
        else {
            for(String k : widgets.keySet()) if (!principalClearance.meets(accessRequirements.get("get-w:"+k))) widgets.remove(k);
            for (WidgetDTO widgetDTO : widgets.values()) widgetDTO.prune(principalClearance); // TODO re-evaluate clearance
        }
        super.prune(principalClearance);
        return this;
    }
}
