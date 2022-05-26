package fh.server.entity.widget;

import fh.server.constant.ComponentType;
import fh.server.constant.EntityType;
import fh.server.entity.Alias;
import fh.server.helpers.interpreter.B;
import fh.server.helpers.interpreter.Interpreter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@javax.persistence.Entity
public class Poll extends WidgetComponent {

    public static final String KEY_LIMIT = "_limit";
    public static final String MC_SEPARATOR = ", ";

    @Column
    private String formulation;

    @ElementCollection
    private final Map<String, String> submissions = new HashMap<>(); // key is alias id

    @ElementCollection
    private final Map<String, Integer> quantification = new HashMap<>();




    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public Map<String, String> getSubmissions() {
        return submissions;
    }

    public boolean existsSubmission(Alias alias) {
        return submissions.containsKey(alias);
    }

    public String getSubmission(Alias alias) {
        return submissions.get(alias);
    }

    public boolean submissionEquals(Alias alias, String reference) {
        if (alias == null) return false;
        String submission = submissions.get(alias);
        return submission != null && submission.equals(reference);
    }

    public boolean submissionNotEquals(Alias alias, String reference) {
        if (alias == null) return false;
        String submission = submissions.get(alias);
        return submission != null && !submission.equals(reference);
    }

    public String revokeSubmission(Alias alias) {
        setLastModified(System.currentTimeMillis());
        return submissions.remove(alias.getId());
    }

    public String submit(Alias alias, String input) {
        setLastModified(System.currentTimeMillis());
        if (input == null) return revokeSubmission(alias);
        return submissions.put(alias.getId(), input);
    }

    public Map<String, Integer> getQuantification() {
        return quantification;
    }

    public Boolean hasQuantification(String key) {
        return quantification.containsKey(key);
    }

    public Integer getQuantification(String key) {
        return quantification.get(key);
    }

    public Integer putQuantification(String key, Integer value) {
        if (key == null || key.isEmpty()) throw new NullPointerException();
        setLastModified(System.currentTimeMillis());
        if (value == null) return quantification.remove(key);
        return quantification.put(key, value);
    }

    public Integer removeQuantification(String key) {
        if (key == null) throw new NullPointerException();
        setLastModified(System.currentTimeMillis());
        return quantification.remove(key);
    }

    public void putQuantification(Map<String, Integer> quantification) {
        this.quantification.putAll(quantification);
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.Poll;
    }

    @Override
    public EntityType getType() {
        return EntityType.Poll;
    }
}
