package fh.server.rest.dto;

import fh.server.constant.AliasManagementPolicy;
import fh.server.constant.SiteVisibility;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SiteBlueprint extends ArtifactBlueprint {

    private String name;

    private String creatorGuardDescription;

    private Map<String, String> pages;

    private Set<String> pageKeys;

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

    public Map<String, String> getPages() {
        return pages;
    }

    public void setPages(Map<String, String> pages) {
        this.pages = pages;
    }

    public Set<String> getPageKeys() {
        return pageKeys;
    }

    public void setPageKeys(Set<String> pageKeys) {
        this.pageKeys = pageKeys;
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
