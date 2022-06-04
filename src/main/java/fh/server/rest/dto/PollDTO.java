package fh.server.rest.dto;

import fh.server.constant.ComponentType;

import java.util.HashMap;
import java.util.Map;

public class PollDTO extends WidgetComponentDTO {

    private String formulation;
    private Map<String, String> submissions;

    private Map<String, Integer> quantification;




    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public Map<String, String> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(Map<String, String> submissions) {
        this.submissions = submissions;
    }

    public Map<String, Integer> getQuantification() {
        return quantification;
    }

    public void setQuantification(Map<String, Integer> quantification) {
        this.quantification = quantification;
    }

    @Override
    public ComponentType getComponentType() {
        return ComponentType.Poll;
    }
}
