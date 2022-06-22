package fh.server.rest.dao;

import fh.server.constant.Visibility;
import fh.server.entity.Alias;
import fh.server.entity.Submission;

public class PollDAO extends ComponentDAO {

    private String formulation;
    private Visibility submissionVisibility;

    private boolean modifySubmission;
    private String submissionString;
    private Submission submission;
    private Alias principal;


    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    public Visibility getSubmissionVisibility() {
        return submissionVisibility;
    }

    public void setSubmissionVisibility(Visibility submissionVisibility) {
        this.submissionVisibility = submissionVisibility;
    }

    public boolean isModifySubmission() {
        return modifySubmission;
    }

    public void setModifySubmission(boolean modifySubmission) {
        this.modifySubmission = modifySubmission;
    }

    public String getSubmissionString() {
        return submissionString;
    }

    public void setSubmissionString(String submissionString) {
        this.submissionString = submissionString;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public Alias getPrincipal() {
        return principal;
    }

    public void setPrincipal(Alias principal) {
        this.principal = principal;
    }
}
