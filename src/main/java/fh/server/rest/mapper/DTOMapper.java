package fh.server.rest.mapper;

import fh.server.entity.*;
import fh.server.entity.login.*;
import fh.server.entity.widget.*;
import fh.server.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "attributes", target = "attributes")
    @Mapping(source = "lastModified", target = "lastModified")
    EntityDTO map(Entity source);

    @Mapping(source = "logins", target = "logins")
    AccountDTO map(Account source);

    @Mapping(source = "identifier", target = "identifier")
    PasswordLoginDTO map(PasswordLogin source);

    @Mapping(source = "token", target = "token")
    TokenLoginDTO map(TokenLogin source);

    @Mapping(source = "ownerIds", target = "ownerIds")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "visibilityGuardDescription", target = "visibilityGuardDescription")
    @Mapping(source = "tags", target = "tags")
    ArtifactDTO map(Artifact source);

    @Mapping(source = "siteId", target = "siteId")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "name", target = "name")
    AliasDTO map(Alias source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "creatorGuardDescription", target = "creatorGuardDescription")
    @Mapping(source = "aliases", target = "aliases")
    @Mapping(source = "pages", target = "pages")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "nameManagementPolicy", target = "nameManagementPolicy")
    @Mapping(source = "tagManagementPolicy", target = "tagManagementPolicy")
    @Mapping(source = "attributeManagementPolicy", target = "attributeManagementPolicy")
    SiteDTO map(Site source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "siteId", target = "siteId")
    @Mapping(source = "creatorGuardDescription", target = "creatorGuardDescription")
    @Mapping(source = "widgets", target = "widgets")
    PageDTO map(Page source);

    @Mapping(source = "components", target = "components")
    WidgetDTO map(Widget source);

    @Mapping(source = "componentType", target = "componentType")
    WidgetComponentDTO map(WidgetComponent source);

    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "data", target = "data")
    FileDTO map(File source);

    @Mapping(source = "text", target = "text")
    ParagraphDTO map(Paragraph source);

    @Mapping(source = "formulation", target = "formulation")
    @Mapping(source = "submissionGuardDescription", target = "submissionGuardDescription")
    @Mapping(source = "inspectorGuardDescription", target = "inspectorGuardDescription")
    @Mapping(source = "submissions", target = "submissions")
    @Mapping(source = "quantification", target = "quantification")
    PollDTO map(Poll source);





    @Mapping(source = "accountId", target = "accountId")
    default LoginDTO map(Login source) {
        switch (source.getLoginMethod()) {
            case Token: return new TokenLoginDTO() {{
                setId(source.getId());
                setAttributes(source.getAttributes());
                setLastModified(source.getLastModified());
                setAccountId(source.getAccountId());
                setToken(((TokenLogin) source).getToken());
            }};
            case Password: return new PasswordLoginDTO() {{
                setId(source.getId());
                setAttributes(source.getAttributes());
                setLastModified(source.getLastModified());
                setAccountId(source.getAccountId());
                setIdentifier(((PasswordLogin) source).getIdentifier());
            }};
            case OAuth2: //TODO
            default: throw new IllegalStateException();
        }
    }
}
