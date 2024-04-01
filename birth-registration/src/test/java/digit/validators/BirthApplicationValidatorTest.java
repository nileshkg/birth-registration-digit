package digit.validators;

import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.repository.BirthRegistrationRepository;
import org.egov.tracer.model.CustomException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BirthApplicationValidatorTest {
    @Mock
    private BirthRegistrationRepository repository;

    @InjectMocks
    private BirthApplicationValidator validator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testValidateBirthApplication_WithValidRequest() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenantId");
        request.getBirthRegistrationApplications().add(application);

        assertDoesNotThrow(() -> validator.validateBirthApplication(request));
    }

    @Test
    public void testValidateBirthApplication_WithMissingTenantId() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        request.getBirthRegistrationApplications().add(application);

        CustomException exception = assertThrows(CustomException.class, () -> validator.validateBirthApplication(request));
        assertEquals("EG_BT_APP_ERR", exception.getCode());
        assertEquals("tenantId is mandatory for creating birth registration applications", exception.getMessage());
    }

    @Test
    public void testValidateApplicationExistence_WithExistingApplication() {
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setApplicationNumber("appNumber");
        when(repository.getApplications(any())).thenReturn(Collections.singletonList(application));

        BirthRegistrationApplication result = validator.validateApplicationExistence(application);

        assertNotNull(result);
        assertEquals("appNumber", result.getApplicationNumber());
    }

    @Test
    public void testValidateApplicationExistence_WithNonExistingApplication() {
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setApplicationNumber("appNumber");
        when(repository.getApplications(any())).thenReturn(Collections.emptyList());

        CustomException exception = assertThrows(CustomException.class, () -> validator.validateApplicationExistence(application));
        assertEquals("EG_BT_APP_ERR", exception.getCode());
        assertEquals("Application not found with number: appNumber", exception.getMessage());
    }
}
