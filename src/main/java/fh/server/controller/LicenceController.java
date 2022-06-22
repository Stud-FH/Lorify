package fh.server.controller;

import fh.server.context.Principal;
import fh.server.entity.Account;
import fh.server.entity.Licence;
import fh.server.entity.Resource;
import fh.server.rest.dao.LicenceDAO;
import fh.server.rest.dao.ScopeDAO;
import fh.server.rest.dto.LicenceDTO;
import fh.server.rest.dto.ResourceDTO;
import fh.server.service.AuthenticationService;
import fh.server.service.LicenceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LicenceController {

    private final LicenceService licenceService;
    private final AuthenticationService authenticationService;

    public LicenceController(LicenceService licenceService, AuthenticationService authenticationService) {
        this.licenceService = licenceService;
        this.authenticationService = authenticationService;
    }




    @PostMapping("/create-licences")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Set<LicenceDTO> create(
            @RequestHeader("Authorization") String token,
            @RequestBody LicenceDAO data
    ) {
        Account principal = authenticationService.account(token);
        return licenceService.create(data, principal).stream().map(LicenceDTO::new).collect(Collectors.toSet());
    }

    @PostMapping("/activate-licence/{code}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LicenceDTO activate(
            @RequestHeader("Authorization") String token,
            @PathVariable("code") String code
    ) {
        Account principal = authenticationService.account(token);
        Licence licence = licenceService.fetchLicence(code);
        Licence activated = licenceService.activate(licence, principal);
        return new LicenceDTO(activated);
    }

}
