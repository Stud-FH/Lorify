package fh.server.rest.dao;

import fh.server.entity.Alias;
import fh.server.entity.Component;
import fh.server.entity.access.AccessRule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ScopeDAO extends ComponentDAO {

    private Map<String, Alias> aliases;
    private Set<String> antiAliases;

    private List<String> accessRuleIds;
    private Map<String, AccessRuleDAO> accessRuleDAOs;
    private List<AccessRule> accessRules;

    private Set<Component> components;
    private Set<String> antiComponents;




    public Map<String, Alias> getAliases() {
        return aliases;
    }

    public void setAliases(Map<String, Alias> aliases) {
        this.aliases = aliases;
    }

    public Set<String> getAntiAliases() {
        return antiAliases;
    }

    public void setAntiAliases(Set<String> antiAliases) {
        this.antiAliases = antiAliases;
    }

    public List<String> getAccessRuleIds() {
        return accessRuleIds;
    }

    public void setAccessRuleIds(List<String> accessRuleIds) {
        this.accessRuleIds = accessRuleIds;
    }

    public Map<String, AccessRuleDAO> getAccessRuleDAOs() {
        return accessRuleDAOs;
    }

    public void setAccessRuleDAOs(Map<String, AccessRuleDAO> accessRuleDAOs) {
        this.accessRuleDAOs = accessRuleDAOs;
    }

    public List<AccessRule> getAccessRules() {
        return accessRules;
    }

    public void setAccessRules(List<AccessRule> accessRules) {
        this.accessRules = accessRules;
    }

    public Set<Component> getComponents() {
        return components;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    public Set<String> getAntiComponents() {
        return antiComponents;
    }

    public void setAntiComponents(Set<String> antiComponents) {
        this.antiComponents = antiComponents;
    }
}
