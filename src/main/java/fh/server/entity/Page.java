package fh.server.entity;

import fh.server.entity.widget.Widget;
import fh.server.helpers.interpreter.B;
import fh.server.helpers.interpreter.DescriptionInterpreter;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Page extends Artifact {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String siteId;

    @Column(length = 2000)
    private String creatorGuardDescription;

    @Transient
    private transient B creatorGuard;

    @ManyToMany
    private final Map<String, Widget> widgets = new HashMap<>(); // key ~ position




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setLastModified(System.currentTimeMillis());
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
        setLastModified(System.currentTimeMillis());
    }

    public String getCreatorGuardDescription() {
        return creatorGuardDescription;
    }

    public void setCreatorGuardDescription(String s) {
        this.creatorGuardDescription = s;
        setLastModified(System.currentTimeMillis());
    }

    public B getCreatorGuard() {
        if (creatorGuard == null) {
            creatorGuard = DescriptionInterpreter.resilientB(creatorGuardDescription);
        }
        return creatorGuard;
    }

    public Map<String, Widget> getWidgets() {
        return widgets;
    }

    public Widget getWidget(String key) {
        return widgets.get(key);
    }

    public boolean hasWidget(String key) {
        return widgets.containsKey(key);
    }

    public Widget putWidget(String key, Widget widget) {
        if (key == null || key.isEmpty()) return null;
        setLastModified(System.currentTimeMillis());
        if (widget == null) return removeWidget(key);
        return widgets.put(key, widget);
    }

    public Widget removeWidget(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return widgets.remove(key);
    }

    public void removeWidget(Widget widget) {
        if (widget == null) return;
        setLastModified(System.currentTimeMillis());
        widgets.keySet().stream().filter(k -> widgets.get(k).equals(widget)).forEach(widgets::remove);
    }

    public void putWidgets(Map<String, Widget> widgets) {
        if (widgets == null) return;
        this.widgets.putAll(widgets);
        setLastModified(System.currentTimeMillis());
    }
}
