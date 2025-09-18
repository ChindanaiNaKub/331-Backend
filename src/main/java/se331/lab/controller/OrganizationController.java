package se331.lab.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import se331.lab.Organization;
import se331.lab.service.OrganizationService;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrganizationController {
    final OrganizationService organizationService;

    @GetMapping({"organization", "organizations"})
    @ResponseBody
    public ResponseEntity<?> getOrganizationLists(
            @RequestParam(value = "_limit", required = false) Integer perPage,
            @RequestParam(value = "_page", required = false) Integer page) {
        Page<Organization> pageOutput = organizationService.getOrganizations(perPage, page);
        HttpHeaders responseHeader = new HttpHeaders();
        responseHeader.set("x-total-count", String.valueOf(pageOutput.getTotalElements()));
        responseHeader.set("X-Total-Count", String.valueOf(pageOutput.getTotalElements()));
        responseHeader.setAccessControlExposeHeaders(java.util.List.of("X-Total-Count", "x-total-count"));
        return new ResponseEntity<>(pageOutput.getContent(), responseHeader, HttpStatus.OK);
    }

    @GetMapping({"organization/{id}", "organizations/{id}"})
    @ResponseBody
    public ResponseEntity<?> getOrganization(@PathVariable("id") Long id) {
        Organization output = organizationService.getOrganization(id);
        if (output != null) {
            return ResponseEntity.ok(output);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The given id is not found");
        }
    }

    @PostMapping("/organizations")
    @ResponseBody
    public ResponseEntity<?> addOrganization(@RequestBody Organization organization){
        Organization output = organizationService.save(organization);
        return ResponseEntity.ok(output);
    }
}
