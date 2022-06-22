package fh.server.entity;

import fh.server.constant.EntityType;
import fh.server.constant.Permission;
import fh.server.constant.Visibility;
import fh.server.context.PermissionSetting;
import fh.server.context.Principal;
import fh.server.rest.dao.PollDAO;
import fh.server.rest.dao.ResourceDAO;
import lombok.Getter;

import javax.persistence.*;
import java.util.Optional;
import java.util.Set;

@Getter
@javax.persistence.Entity
public class Poll extends Component {

    public static final String KEY_LIMIT = "_limit";
    public static final String MC_SEPARATOR = ", ";

    @Column
    private String formulation;

    @Enumerated
    @Column(nullable = false)
    private Visibility submissionVisibility = Visibility.Public;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    private final Set<Submission> submissions = new java.util.LinkedHashSet<>();


    public String getFormulation() {
        return formulation;
    }

    public void setFormulation(String formulation) {
        this.formulation = formulation;
        setLastModified(System.currentTimeMillis());
    }

    public Visibility getSubmissionVisibility() {
        return submissionVisibility;
    }

    public void setSubmissionVisibility(Visibility submissionVisibility) {
        this.submissionVisibility = submissionVisibility;
        setLastModified(System.currentTimeMillis());
    }

    public Set<Submission> getSubmissions() {
        return submissions;
    }

    public boolean existsSubmission(Alias alias) {
        return submissions.stream().anyMatch(s -> s.getAlias().equals(alias));
    }


    public Optional<Submission> getSubmission(Alias alias) {
        return submissions.stream().filter(s -> s.getAlias().equals(alias)).findAny();
    }

//    public boolean submissionEquals(Alias alias, String reference) {
//        if (alias == null) return false;
//        String submission = submissions.get(alias.getId());
//        return submission != null && submission.equals(reference);
//    }

//    public boolean submissionNotEquals(Alias alias, String reference) {
//        if (alias == null) return false;
//        String submission = submissions.get(alias.getId());
//        return submission != null && !submission.equals(reference);
//    }

    public Submission revokeSubmission(Alias alias) {
        Submission submission = getSubmission(alias).orElse(null);
        if (submission != null) {
            submission.remove();
            return submission;
        }
        return null;
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
        setLastModified(System.currentTimeMillis());
    }

    public void removeSubmission(Submission submission) {
        submissions.remove(submission);
        setLastModified(System.currentTimeMillis());
    }

//    public Boolean hasQuantification(String key) {
//        return quantification.containsKey(key);
//    }
//
//    public Integer getQuantification(String key) {
//        return quantification.get(key);
//    }
//
//    public Integer putQuantification(String key, Integer value) {
//        if (key == null || key.isEmpty()) throw new NullPointerException();
//        setLastModified(System.currentTimeMillis());
//        if (value == null) return quantification.remove(key);
//        return quantification.put(key, value);
//    }
//
//    public Integer removeQuantification(String key) {
//        if (key == null) throw new NullPointerException();
//        setLastModified(System.currentTimeMillis());
//        return quantification.remove(key);
//    }
//
//    public void putQuantification(Map<String, Integer> quantification) {
//        this.quantification.putAll(quantification);
//    }

    public PermissionSetting verifyAccess(ResourceDAO resourceDAO, Principal principal) {
        PermissionSetting ps = super.verifyAccess(resourceDAO, principal);
        PollDAO dao = (PollDAO) resourceDAO;

        if (dao.getFormulation() != null) {
            ps.getResource().require(Permission.AuthorAccess);
        }
        if (dao.isModifySubmission()) {
            ps.getResource().require(Permission.UserAccess);
            if (!principal.equals(dao.getPrincipal())) throw new IllegalStateException("dao init gone wrong");
        }
        if (dao.getSubmissionVisibility() != null) {
            ps.getResource().require(Permission.AdminAccess);
        }

        return ps;
    }

    public PollDAO adapt(ResourceDAO resourceDAO, ResourceDAO logObject) {
        if (logObject == null) logObject = new PollDAO();
        super.adapt(resourceDAO, logObject);
        PollDAO dao = (PollDAO) resourceDAO;
        PollDAO lo = (PollDAO) logObject;

        if (dao.getFormulation() != null) {
            lo.setFormulation(formulation);
            setFormulation(dao.getFormulation());
        }
        if (dao.isModifySubmission()) {
            lo.setSubmission(revokeSubmission(dao.getPrincipal()));
            if (dao.getSubmission() != null) {
                dao.getSubmission().deploy();
            }
        }
        if (dao.getSubmissionVisibility() != null) {
            lo.setSubmissionVisibility(submissionVisibility);
            setSubmissionVisibility(dao.getSubmissionVisibility());
        }
        return lo;
    }

    @Override
    public EntityType getType() {
        return EntityType.Poll;
    }
}
