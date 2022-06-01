package org.doxa.contract.serviceImpl.validators;

import org.doxa.auth.DoxaAuthenticationManager;
import org.doxa.contract.config.Message;
import org.doxa.contract.exceptions.AccessDeniedException;
import org.doxa.contract.exceptions.BadRequestException;
import org.doxa.contract.exceptions.ObjectDoesNotExistException;
import org.doxa.contract.microservices.DTO.ProjectDetailsApiDto;
import org.doxa.contract.microservices.DTO.ProjectUser;
import org.doxa.contract.microservices.DTO.entityService.IEntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This service provide method to validate data from entity microservice
 */
@Service
public class EntityServiceValidator {
    @Autowired
    private IEntitiesService entitiesService;

    @Autowired
    private DoxaAuthenticationManager authenticationManager;

    public ProjectDetailsApiDto validateProject(String companyUuid, String uuid) throws AccessDeniedException, ObjectDoesNotExistException {
        String requesterUuid = (String) authenticationManager.getUserByKey("sub");
        ProjectDetailsApiDto project = entitiesService.getProjectDetails(companyUuid, uuid);
        if (project == null) {
            throw new BadRequestException(Message.INVALID_PROJECT.getValue());
        }
        List<ProjectUser> projectUsers = project.getProjectUserDtoList();
        boolean userInProject = false;
        for (ProjectUser projectUser : projectUsers) {
            if (projectUser.getUserUuid().equals(requesterUuid)) {
                userInProject = true;
            }
        }
        if (!userInProject) {
            throw new AccessDeniedException(Message.USER_NOT_IN_PROJECT.getValue());
        }
        return project;
    }
}
