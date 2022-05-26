package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.SiteVisibility;
import fh.server.helpers.Tokens;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Site extends Entity {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private SiteVisibility visibility;

    @ManyToMany
    private final Map<String, Alias> aliases = new HashMap<>();

    @ManyToMany
    private final Map<String, Page> pages = new HashMap<>();



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastModified(System.currentTimeMillis());
    }

    public SiteVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(SiteVisibility visibility) {
        this.visibility = visibility;
        setLastModified(System.currentTimeMillis());
    }

    public Map<String, Alias> getAliases() {
        return aliases;
    }

    public Alias getAlias(String token) {
        return aliases.get(token);
    }

    public Alias getAlias(Account account) {
        return aliases.get(account.getId());
    }

    public boolean hasAlias(String token) {
        return aliases.containsKey(token);
    }

    public boolean hasAlias(Account account) {
        return aliases.containsKey(account.getId());
    }

    public Alias createAlias(String name) {
        setLastModified(System.currentTimeMillis());
        String token;
        do {
            token = Tokens.randomAliasToken();
        } while (aliases.containsKey(token));
        Alias alias = new Alias();
        alias.setParentId(getId());
        alias.setAccessor(token);
        alias.setName(name);
        aliases.put(token, alias);
        return alias;
    }

    public Alias claimAlias(String token, Account account) {
        if (account == null) throw new NullPointerException();
        if (aliases.containsKey(account.getId())) throw new IllegalStateException();
        Alias alias = aliases.remove(token);
        if (alias == null) throw new NoSuchElementException();
        alias.claim(account);
        aliases.put(account.getId(), alias);
        setLastModified(System.currentTimeMillis());
        return alias;
    }

    public Alias removeAlias(String token) {
        setLastModified(System.currentTimeMillis());
        return aliases.remove(token);
    }

    public Map<String, Page> getPages() {
        return pages;
    }

    public Page getPage(String key) {
        return pages.get(key);
    }

    public boolean hasPage(String key) {
        return pages.containsKey(key);
    }

    public Page putPage(String key, Page page) {
        if (key == null || key.isEmpty()) return null;
        if (page == null) return removePage(key);
        setLastModified(System.currentTimeMillis());
        return pages.put(key, page);
    }

    public Page removePage(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return pages.remove(key);
    }

    public void removePage(Page page) {
        if (page == null) return;
        setLastModified(System.currentTimeMillis());
        pages.keySet().stream().filter(k -> pages.get(k).equals(page)).forEach(pages::remove);
    }

    public void putPages(Map<String, Page> pages) {
        if (pages == null) return;
        this.pages.putAll(pages);
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public EntityType getType() {
        return EntityType.Site;
    }
}
