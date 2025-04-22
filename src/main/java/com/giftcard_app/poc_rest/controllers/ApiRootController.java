package com.giftcard_app.poc_rest.controllers;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class ApiRootController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public RepresentationModel<?> root() {
        var model = new RepresentationModel<>();
        model.add(linkTo(ApiRootController.class).slash("users").withRel("users"));
        model.add(linkTo(ApiRootController.class).slash("orders").withRel("orders"));
        model.add(linkTo(apiDocsUri()).withRel("api-docs"));
        model.add(linkTo(swaggerUiUri()).withRel("swagger-ui"));
        return model;
    }

    private URI apiDocsUri() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/v3/api-docs").build().toUri();
    }

    private URI swaggerUiUri() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/swagger-ui/index.html").build().toUri();
    }
}
