package fh.server.rest.mapper;

import fh.server.entity.*;
import fh.server.entity.login.*;
import fh.server.entity.widget.*;
import fh.server.rest.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "parentId", target = "parentId")
    @Mapping(source = "ownerId", target = "ownerId")
    @Mapping(source = "attributes", target = "attributes")
    @Mapping(source = "accessRequirements", target = "accessRequirements")
    @Mapping(source = "guards", target = "guards")
    @Mapping(source = "lastModified", target = "lastModified")
    EntityDTO map(Entity source);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "logins", target = "logins")
    AccountDTO map(Account source);

    @Mapping(source = "identifier", target = "identifier")
    PasswordLoginDTO map(PasswordLogin source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "accessor", target = "accessor")
    @Mapping(source = "claimed", target = "claimed")
    AliasDTO map(Alias source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "visibility", target = "visibility")
    @Mapping(source = "aliases", target = "aliases")
    @Mapping(source = "pages", target = "pages")
    SiteDTO map(Site source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "widgets", target = "widgets")
    PageDTO map(Page source);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "components", target = "components")
    WidgetDTO map(Widget source);

    @Mapping(source = "filename", target = "filename")
    @Mapping(source = "data", target = "data")
    FileDTO map(File source);

    @Mapping(source = "text", target = "text")
    ParagraphDTO map(Paragraph source);

    @Mapping(source = "formulation", target = "formulation")
    @Mapping(source = "submissions", target = "submissions")
    @Mapping(source = "quantification", target = "quantification")
    PollDTO map(Poll source);

    @Mapping(source = "accountId", target = "accountId")
    default LoginDTO map(Login source) {
        switch (source.getLoginMethod()) {
            case Password: return new PasswordLoginDTO() {{
                setId(source.getId());
                setOwnerId(source.getOwnerId());
                setAttributes(source.getAttributes());
                setLastModified(source.getLastModified());
                setIdentifier(((PasswordLogin) source).getIdentifier());
            }};
            case OAuth2: //TODO
            default: throw new IllegalStateException();
        }
    }


    default WidgetComponentDTO map(WidgetComponent source) {
        switch (source.getComponentType()) {
            case Paragraph: return new ParagraphDTO() {{
                adapt(source);
                setText(((Paragraph) source).getText());
            }};
            case File: return new FileDTO() {{
                adapt(source);
                setFilename(((File) source).getFilename());
                setData(((File) source).getData());
            }};
            case Poll: return new PollDTO() {{
                adapt(source);
                setFormulation(((Poll) source).getFormulation());
                setSubmissions(((Poll) source).getSubmissions());
                setQuantification(((Poll) source).getQuantification());
            }};
            default: throw new IllegalStateException();
        }
    }

}
