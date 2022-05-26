package fh.server.helpers;

import fh.server.constant.EntityType;
import fh.server.entity.*;
import fh.server.entity.widget.Poll;
import fh.server.entity.widget.Widget;

import java.util.function.Supplier;

public class Context {

    private final Supplier<Entity> principalSupplier;
    private final Supplier<Entity> victimSupplier;

    private Entity principal;
    private Entity victim;

    protected Context(Context parent) {
        this.principalSupplier = parent.principalSupplier;
        this.victimSupplier = parent.victimSupplier;
        this.principal = parent.principal;
        this.victim = parent.victim;
    }

    public Context(
            Supplier<Entity> principalSupplier,
            Supplier<Entity> victimSupplier) {
        this.principalSupplier = principalSupplier;
        this.victimSupplier = victimSupplier;
    }

    public Context(Entity principal, Entity victim) {
        this.principalSupplier = () -> principal;
        this.victimSupplier = () -> victim;
    }

    public Operation operation(String operation, Object value, Object previous) {
        return new Operation(this, operation, value, previous);
    }


    public Entity getPrincipal() {
        return principal == null? (principal = principalSupplier.get()) : principal;
    }

    public Alias principalAsAlias() {
        return (Alias) getPrincipal();
    }

    public Account principalAsAccount() {
        return (Account) getPrincipal();
    }

    public Entity getVictim() {
        return victim == null? (victim = victimSupplier.get()) : victim;
    }

    public Alias victimAsAlias() {
        return (Alias) getVictim();
    }

    public Poll victimAsPoll() {
        return (Poll) getVictim();
    }

    public Site victimAsSite() {
        return (Site) getVictim();
    }

    public Page victimAsPage() {
        return (Page) getVictim();
    }

    public Widget victimAsWidget() {
        return (Widget) getVictim();
    }

    public EntityType victimType() {
        return getVictim().getType();
    }

    public EntityType principalType() {
        return getPrincipal().getType();
    }

    public boolean isOwnerAccess() {
        return getVictim().getOwnerId().equals(getPrincipal().getId());
    }

}
