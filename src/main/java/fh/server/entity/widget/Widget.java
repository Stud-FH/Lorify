package fh.server.entity.widget;

import fh.server.constant.EntityType;

import javax.persistence.*;
import java.util.*;

@javax.persistence.Entity
public class Widget extends fh.server.entity.Entity {

    @Column
    private String name;

    @ManyToMany
    private final Map<String, WidgetComponent> components = new HashMap<>();




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, WidgetComponent> getComponents() {
        return components;
    }

    public WidgetComponent getComponent(String key) {
        return components.get(key);
    }

    public boolean hasComponent(String key) {
        return components.containsKey(key);
    }

    public WidgetComponent putComponent(String key, WidgetComponent component) {
        if (key == null || key.isEmpty()) return null;
        setLastModified(System.currentTimeMillis());
        if (component == null) return removeComponent(key);
        return components.put(key, component);
    }

    public WidgetComponent removeComponent(String key) {
        if (key == null) return null;
        setLastModified(System.currentTimeMillis());
        return components.remove(key);
    }

    public void removeComponent(WidgetComponent component) {
        if (component == null) return;
        setLastModified(System.currentTimeMillis());
        components.keySet().stream().filter(k -> components.get(k).equals(component)).forEach(components::remove);
    }

    public void putComponents(Map<String, WidgetComponent> components) {
        if (components == null) return;
        this.components.putAll(components);
        setLastModified(System.currentTimeMillis());
    }

    @Override
    public EntityType getType() {
        return EntityType.Widget;
    }
}
