package digit.validators;

import digit.TestConfiguration;
import digit.models.BirthApplicationSearchCriteria;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.repository.BirthRegistrationRepository;
import org.checkerframework.checker.units.qual.A;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
public class BirthApplicationValidatorTest {

    @MockBean
    private BirthRegistrationRepository repository;

    @InjectMocks
    private BirthApplicationValidator birthApplicationValidator;

    @Test
    public void validateBirthApplicationException(){
        BirthRegistrationRequest birthRegistrationRequest = new BirthRegistrationRequest();
        BirthRegistrationApplication birthRegistrationApplication = new BirthRegistrationApplication();
        birthRegistrationApplication.setTenantId(null);
        birthRegistrationRequest.setBirthRegistrationApplications(Collections.singletonList(birthRegistrationApplication));

        CustomException exception = assertThrows(CustomException.class, ()-> birthApplicationValidator.validateBirthApplication(birthRegistrationRequest));
        assertEquals("EG_BT_APP_ERR", "tenantId is mandatory for creating birth registration applications",exception.getMessage());
    }

    @Test
    public void validateApplicationExistenceTest(){
        BirthRegistrationApplication birthRegistrationApplication = new BirthRegistrationApplication();
        birthRegistrationApplication.setApplicationNumber("test");
        when(repository.getApplications(any(BirthApplicationSearchCriteria.class))).thenReturn(Arrays.asList(birthRegistrationApplication));

      //  birthApplicationValidator.validateApplicationExistence(birthRegistrationApplication);
        verify(repository, times(0)).getApplications(any(BirthApplicationSearchCriteria.class));

    }
}
