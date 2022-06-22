package fh.server.rest.dto;


import fh.server.constant.Permission;
import fh.server.context.Principal;
import fh.server.entity.Scope;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class ScopeDTO extends ComponentDTO {

    private Set<AliasDTO> aliases;
    private Set<ComponentDTO> components;
    private List<AccessRuleDTO> accessRules;


    public ScopeDTO(Scope source, Principal principal) {
        super(source, principal);
        if (ps.getResource().meets(Permission.UserView)) {
            aliases = source.getAliases().values().stream().map(c -> new AliasDTO(c, principal)).collect(Collectors.toSet());
            components = source.getComponents().stream().map(c -> new ComponentDTO(c, principal)).collect(Collectors.toSet());
        }
        if (ps.getResource().meets(Permission.AdminView)) {
            accessRules = source.getAccessRules().stream().map(AccessRuleDTO::new).collect(Collectors.toList());
        }
    }

    public Set<AliasDTO> getAliases() {
        return aliases;
    }

    public Set<ComponentDTO> getComponents() {
        return components;
    }

    public List<AccessRuleDTO> getAccessRules() {
        return accessRules;
    }
}
