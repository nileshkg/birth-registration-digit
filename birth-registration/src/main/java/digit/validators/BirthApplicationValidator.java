package digit.validators;

import digit.repository.BirthRegistrationRepository;
import digit.models.BirthApplicationSearchCriteria;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class BirthApplicationValidator {

    @Autowired
    private BirthRegistrationRepository repository;

    public void validateBirthApplication(BirthRegistrationRequest birthRegistrationRequest) throws CustomException{
        birthRegistrationRequest.getBirthRegistrationApplications().forEach(application -> {
            if(ObjectUtils.isEmpty(application.getTenantId()))
                throw new CustomException("EG_BT_APP_ERR", "tenantId is mandatory for creating birth registration applications");
        });
    }

    public BirthRegistrationApplication validateApplicationExistence(BirthRegistrationApplication birthRegistrationApplication) {
        return repository.getApplications(BirthApplicationSearchCriteria.builder().applicationNumber(birthRegistrationApplication.getApplicationNumber()).build()).get(0);
    }
}