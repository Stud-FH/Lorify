package fh.server.rest.dto;


import fh.server.constant.Permission;
import fh.server.constant.Visibility;
import fh.server.context.Principal;
import fh.server.entity.Poll;

import java.util.Set;
import java.util.stream.Collectors;

public class PollDTO extends ComponentDTO {

    private String formulation;
    private Set<SubmissionDTO> submissions;
    private Visibility submissionVisibility;

    public PollDTO(Poll source, Principal principal) {
        super(source, principal);

        if (ps.getResource().meets(Permission.UserView)) {
            formulation = source.getFormulation();
            submissionVisibility = source.getSubmissionVisibility();
            if (submissionVisibility != Visibility.Invisible || ps.getResource().meets(Permission.AdminView)) {
                submissions = source.getSubmissions().stream().map(c -> new SubmissionDTO(c, ps)).collect(Collectors.toSet());
            }
        }
    }

    public String getFormulation() {
        return formulation;
    }

    public Set<SubmissionDTO> getSubmissions() {
        return submissions;
    }

    public Visibility getSubmissionVisibility() {
        return submissionVisibility;
    }
}
