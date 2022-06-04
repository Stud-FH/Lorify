package fh.server.rest.dao;

import fh.server.constant.ComponentType;

import java.util.Map;

public class WidgetComponentDAO extends EntityDAO {

    private ComponentType componentType;

    private String filename;

    private byte[] data;

    private String text;

    private String formulation;

    private String submissionGuardDescription;

    private String inspectorGuardDescription;

    private Map<String, Integer> quantification;

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

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

    public Map<String, Integer> getQuantification() {
        return quantification;
    }

    public void setQuantification(Map<String, Integer> quantification) {
        this.quantification = quantification;
    }


}
