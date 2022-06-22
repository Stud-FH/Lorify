package fh.server.controller;

import fh.server.context.Principal;
import fh.server.entity.Resource;
import fh.server.rest.dto.ResourceDTO;
import fh.server.service.AuthenticationService;
import fh.server.service.ResourceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResourceController {

    private final ResourceService resourceService;
    private final AuthenticationService authenticationService;

    public ResourceController(ResourceService resourceService, AuthenticationService authenticationService) {
        this.resourceService = resourceService;
        this.authenticationService = authenticationService;
    }




    @GetMapping("/check-path/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Boolean checkLogin(
            @PathVariable("path") String path
    ) {
        return resourceService.existsByPath(path);
    }

    @GetMapping("/resource/{path}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResourceDTO getResource(
            @RequestHeader("Authorization") String token,
            @PathVariable("path") String path
    ) {
        Principal principal = authenticationService.principal(token);
        Resource result = resourceService.fetchResource(path);
        return new ResourceDTO(result, principal);
    }

}
