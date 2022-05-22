package fh.server.rest.dto;

import fh.server.constant.AliasManagementPolicy;
import fh.server.constant.SiteVisibility;
import fh.server.entity.Alias;
import fh.server.entity.Page;
import fh.server.rest.mapper.DTOMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SiteDTO extends ArtifactDTO {

    private String name;

    private String creatorGuardDescription;

    private Map<String, AliasDTO> aliases;

    private Map<String, PageDTO> pages;

    private SiteVisibility visibility;

    private AliasManagementPolicy nameManagementPolicy;

    private AliasManagementPolicy tagManagementPolicy;

    private AliasManagementPolicy attributeManagementPolicy;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatorGuardDescription() {
        return creatorGuardDescription;
    }

    public void setCreatorGuardDescription(String creatorGuardDescription) {
        this.creatorGuardDescription = creatorGuardDescription;
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

    public SiteVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SiteVisibility visibility) {
        this.visibility = visibility;
    }

    public AliasManagementPolicy getNameManagementPolicy() {
        return nameManagementPolicy;
    }

    public void setNameManagementPolicy(AliasManagementPolicy nameManagementPolicy) {
        this.nameManagementPolicy = nameManagementPolicy;
    }

    public AliasManagementPolicy getTagManagementPolicy() {
        return tagManagementPolicy;
    }

    public void setTagManagementPolicy(AliasManagementPolicy tagManagementPolicy) {
        this.tagManagementPolicy = tagManagementPolicy;
    }

    public AliasManagementPolicy getAttributeManagementPolicy() {
        return attributeManagementPolicy;
    }

    public void setAttributeManagementPolicy(AliasManagementPolicy attributeManagementPolicy) {
        this.attributeManagementPolicy = attributeManagementPolicy;
    }
}
