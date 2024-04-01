package digit.validators;

import digit.TestConfiguration;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.repository.BirthRegistrationRepository;
import org.egov.tracer.model.CustomException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
public class BirthApplicationValidatorTest {
    @Mock
    private BirthRegistrationRepository repository;

    @MockBean
    private BirthApplicationValidator validator;

    @Test
    public void validateBirthApplication_WithValidRequest_Test() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenantId");
        request.getBirthRegistrationApplications().add(application);

        assertDoesNotThrow(() -> validator.validateBirthApplication(request));
    }

    @Test
    public void validateBirthApplication_WithMissingTenantId_Test() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        request.getBirthRegistrationApplications().add(application);

        CustomException exception = assertThrows(CustomException.class, () -> validator.validateBirthApplication(request));
        assertEquals("EG_BT_APP_ERR", exception.getCode());
        assertEquals("tenantId is mandatory for creating birth registration applications", exception.getMessage());
    }

    @Test
    public void validateApplicationExistence_WithExistingApplication_Test() {
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setApplicationNumber("testAppNumber");
        when(repository.getApplications(any())).thenReturn(Collections.singletonList(application));

        BirthRegistrationApplication result = validator.validateApplicationExistence(application);

        assertNotNull(result);
        assertEquals("testAppNumber", result.getApplicationNumber());
    }

    @Test
    public void validateApplicationExistence_WithNonExistingApplication_Test() {
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setApplicationNumber("testAppNumber");
        when(repository.getApplications(any())).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(application));
        assertEquals("EG_BT_APP_ERR", exception.getCode());
        assertEquals("Application not found with number: appNumber", exception.getMessage());
    }
}
