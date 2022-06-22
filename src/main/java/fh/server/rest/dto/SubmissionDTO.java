package fh.server.rest.dto;

import fh.server.constant.Permission;
import fh.server.context.PermissionSetting;
import fh.server.entity.Submission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmissionDTO extends EntityDTO {

    private String aliasId;
    private String aliasName;
    private String pollId;
    private String string;


    public SubmissionDTO(Submission source, PermissionSetting ps) {
        super(source);

        if (ps.getResource().meets(Permission.AdminView)) {
            aliasId = source.getAlias().getId();
            aliasName = source.getAlias().getName();
            pollId = source.getPoll().getId();
            string = source.getString();
        } else if (ps.getResource().meets(Permission.UserView)) {
            pollId = source.getPoll().getId();

            switch (source.getPoll().getSubmissionVisibility()) {
                case Public: {
                    aliasName = source.getAlias().getName();
                    string = source.getString();
                } break;
                case Anonymous: {
                    string = source.getString();
                } break;
                case ContentHidden: {
                    aliasName = source.getAlias().getName();
                } break;
            }
        }
    }

    public String getAliasId() {
        return aliasId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public String getPollId() {
        return pollId;
    }

    public String getString() {
        return string;
    }
}
