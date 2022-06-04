package fh.server.rest.dto;

import fh.server.constant.TrustLevel;

public class AliasDTO extends EntityDTO {

    private String name;

    private String accessor;

    private Boolean claimed;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessor() {
        return accessor;
    }

    public void setAccessor(String accessor) {
        this.accessor = accessor;
    }

    public Boolean getClaimed() {
        return claimed;
    }

    public void setClaimed(Boolean claimed) {
        this.claimed = claimed;
    }

    @Override
    public AliasDTO prune(TrustLevel principalClearance) {
        if (!principalClearance.meets(accessRequirements.get("get-name"))) name = null;
        if (!principalClearance.meets(accessRequirements.get("get-accessor"))) accessor = null;
        if (!principalClearance.meets(accessRequirements.get("get-claimed"))) claimed = null;
        super.prune(principalClearance);
        return this;
    }
}
