package fh.server.rest.dto;

import java.util.HashMap;
import java.util.Map;

public class PollDTO extends WidgetComponentDTO {

    private String formulation;

    private String submissionGuardDescription;

    private String inspectorGuardDescription;

    private final Map<String, String> submissions = new HashMap<>();

    private final Map<String, Integer> quantification = new HashMap<>();




    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public String getSubmissionGuardDescription() {
        return submissionGuardDescription;
    }

    public void setSubmissionGuardDescription(String submissionGuardDescription) {
        this.submissionGuardDescription = submissionGuardDescription;
    }

    public String getInspectorGuardDescription() {
        return inspectorGuardDescription;
    }

    public void setInspectorGuardDescription(String inspectorGuardDescription) {
        this.inspectorGuardDescription = inspectorGuardDescription;
    }

    public Map<String, String> getSubmissions() {
        return submissions;
    }

    public Map<String, Integer> getQuantification() {
        return quantification;
    }
}
