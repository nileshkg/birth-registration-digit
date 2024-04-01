package digit.enrichment;
import digit.service.UserService;
import digit.util.IdgenUtil;
import digit.util.UserUtil;
import digit.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;
import digit.models.coremodels.UserDetailResponse;
import org.egov.common.contract.request.User;

@Component
@Slf4j
public class BirthApplicationEnrichment {

    @Autowired
    private IdgenUtil idgenUtil;


    @Autowired
    private UserService userService;

    @Autowired
    private UserUtil userUtils;

    public void enrichBirthApplication(BirthRegistrationRequest birthRegistrationRequest) {
        try {
            List<String> birthRegistrationIdList = idgenUtil.getIdList(birthRegistrationRequest.getRequestInfo(), birthRegistrationRequest.getBirthRegistrationApplications().get(0).getTenantId(), "product.id", "P-[cy:yyyy-MM-dd]-[SEQ_PRODUCT_P]", birthRegistrationRequest.getBirthRegistrationApplications().size());
            Integer index = 0;
            for(BirthRegistrationApplication application : birthRegistrationRequest.getBirthRegistrationApplications()){
                // Enrich audit details
                AuditDetails auditDetails = AuditDetails.builder().createdBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).createdTime(System.currentTimeMillis()).lastModifiedBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid()).lastModifiedTime(System.currentTimeMillis()).build();
                application.setAuditDetails(auditDetails);

                // Enrich UUID
                application.setId(UUID.randomUUID().toString());

                // Enrich registration Id
                application.getAddress().setRegistrationId(application.getId());

                // Enrich address UUID
                application.getAddress().setId(UUID.randomUUID().toString());

                //Enrich application number from IDgen
                application.setApplicationNumber(birthRegistrationIdList.get(index++));
            }
        } catch (Exception e) {
            log.error("Error enriching birth application: {}", e.getMessage());
            // Handle the exception or throw a custom exception
        }
    }

    public void enrichBirthApplicationUponUpdate(BirthRegistrationRequest birthRegistrationRequest) {
        try {
            // Enrich lastModifiedTime and lastModifiedBy in case of update
            birthRegistrationRequest.getBirthRegistrationApplications().get(0).getAuditDetails().setLastModifiedTime(System.currentTimeMillis());
            birthRegistrationRequest.getBirthRegistrationApplications().get(0).getAuditDetails().setLastModifiedBy(birthRegistrationRequest.getRequestInfo().getUserInfo().getUuid());
        } catch (Exception e) {
            log.error("Error enriching birth application upon update: {}", e.getMessage());
            // Handle the exception or throw a custom exception
        }
    }

    private digit.models.User convertToNewUser(User oldUser) {
        digit.models.User newUser = new digit.models.User();

        newUser.setTenantId(oldUser.getTenantId());
       // newUser.setId(Math.toIntExact(Long.valueOf(oldUser.getId() != null ? Math.toIntExact(oldUser.getId()) : null)));
        newUser.setUuid(oldUser.getUuid());
        newUser.setUserName(oldUser.getUserName());
        newUser.setMobileNumber(oldUser.getMobileNumber());
        newUser.setEmailId(oldUser.getEmailId());

        List<Role> roles = new ArrayList<>();
        oldUser.getRoles().forEach(oldRole -> {
            Role newRole = new Role();
            newRole.setName(oldRole.getName());
            roles.add(newRole);
        });
        newUser.setRoles(roles);

        newUser.setName(oldUser.getName());
        newUser.setType(oldUser.getType());

        return newUser;
    }

    public void enrichFatherApplicantOnSearch(BirthRegistrationApplication application) {
        UserDetailResponse fatherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getFather().getUuid() ,null);
        User fatherUser = fatherUserResponse.getUser().get(0);
        log.info(fatherUser.toString());
        User fatherApplicant =
                User.builder()
                        .userName(fatherUser.getUserName())
                        .mobileNumber(fatherUser.getMobileNumber())
                        .emailId(fatherUser.getEmailId())
                        .uuid(fatherUser.getUuid())
                        .name(fatherUser.getName())
                        .type(fatherUser.getType())
                        .tenantId(fatherUser.getTenantId())
                        .roles(fatherUser.getRoles()).build();
        application.setFather(convertToNewUser(fatherApplicant));
    }

    public void enrichMotherApplicantOnSearch(BirthRegistrationApplication application) {
        UserDetailResponse motherUserResponse = userService.searchUser(userUtils.getStateLevelTenant(application.getTenantId()),application.getMother().getUuid(),null);
        User motherUser = motherUserResponse.getUser().get(0);
        log.info(motherUser.toString());
        User motherApplicant = User.builder()
                .userName(motherUser.getUserName())
                .mobileNumber(motherUser.getMobileNumber())
                .emailId(motherUser.getEmailId())
                .uuid(motherUser.getUuid())
                .name(motherUser.getName())
                .type(motherUser.getType())
                .tenantId(motherUser.getTenantId())
                .roles(motherUser.getRoles()).build();
        application.setMother(convertToNewUser(motherApplicant));
    }
}
