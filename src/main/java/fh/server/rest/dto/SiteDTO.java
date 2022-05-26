package fh.server.rest.dto;

import fh.server.constant.SiteVisibility;
import fh.server.constant.TrustLevel;
import fh.server.entity.Alias;
import fh.server.entity.Page;
import fh.server.rest.mapper.DTOMapper;

import java.util.HashMap;
import java.util.Map;

public class SiteDTO extends EntityDTO {

    private String name;

    private SiteVisibility visibility;

    private Map<String, AliasDTO> aliases;

    private Map<String, PageDTO> pages;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SiteVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SiteVisibility visibility) {
        this.visibility = visibility;
    }

    public Map<String, AliasDTO> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, Alias> aliases) {
        this.aliases = new HashMap<>();
        aliases.keySet().forEach(key -> this.aliases.put(key, DTOMapper.INSTANCE.map(aliases.get(key))));
    }

    public Map<String, PageDTO> getPages() {
        return pages;
    }

    public void setPages(Map<String, Page> pages) {
        this.pages = new HashMap<>();
        pages.keySet().forEach(k -> this.pages.put(k, DTOMapper.INSTANCE.map(pages.get(k))));
    }

    @Override
    public SiteDTO prune(TrustLevel principalClearance) {
        if (!principalClearance.meets(accessRequirements.get("get-name"))) name = null;
        if (!principalClearance.meets(accessRequirements.get("get-visibility"))) visibility = null;
        if (!principalClearance.meets(accessRequirements.get("get-aliases"))) aliases = null;
//        else for(String k : aliases.keySet()) if (!principalClearance.meets(accessRequirements.get("get-alias:"+k))) aliases.remove(k); // not desirable
        if (!principalClearance.meets(accessRequirements.get("get-pages"))) pages = null;
        else for(String k : pages.keySet()) if (!principalClearance.meets(accessRequirements.get("get-p:"+k))) pages.remove(k);
        super.prune(principalClearance);
        return this;
    }
}
