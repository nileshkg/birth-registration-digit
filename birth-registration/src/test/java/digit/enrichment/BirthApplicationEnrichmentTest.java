package digit.enrichment;

import digit.models.*;
import digit.service.UserService;
import digit.util.IdgenUtil;
import digit.util.UserUtil;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BirthApplicationEnrichmentTest {
    @InjectMocks
    private BirthApplicationEnrichment birthApplicationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private UserService userService;

    @Mock
    private UserUtil userUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void enrichBirthApplication_Test() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt()))
                .thenReturn(Arrays.asList("BTR123456", "BTR123457"));

        birthApplicationEnrichment.enrichBirthApplication(birthRegistrationRequest);

        List<BirthRegistrationApplication> applications = birthRegistrationRequest.getBirthRegistrationApplications();
        assertNotNull(applications);
        assertEquals(2, applications.size());
        for (BirthRegistrationApplication application : applications) {
            assertNotNull(application.getAuditDetails());
            assertNotNull(application.getId());
            assertNotNull(application.getApplicationNumber());
            assertNotNull(application.getAddress());
            assertNotNull(application.getFather());
            assertNotNull(application.getMother());
            assertNotNull(application.getAddress().getId());
            assertEquals(application.getId(), application.getFather().getUuid());
            assertEquals(application.getId(), application.getMother().getUuid());
        }
    }

    @Test
    public void enrichBirthApplicationUponUpdate_Test() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        BirthRegistrationApplication application = birthRegistrationRequest.getBirthRegistrationApplications().get(0);
        AuditDetails auditDetails = new AuditDetails("userId1", "123456L", 143535L, 123457L);
        application.setAuditDetails(auditDetails);

        birthApplicationEnrichment.enrichBirthApplicationUponUpdate(birthRegistrationRequest);

        assertEquals("userId1", application.getAuditDetails().getCreatedBy());
        assertEquals(Optional.of(123456L), application.getAuditDetails().getCreatedTime());
        assertEquals("userId2", application.getAuditDetails().getLastModifiedBy());
        assertNotEquals(Optional.of(123457L), application.getAuditDetails().getLastModifiedTime()); // Last modified time should be updated
    }

    private BirthRegistrationRequest createBirthRegistrationRequest() {
        RequestInfo requestInfo = new RequestInfo();
        List<BirthRegistrationApplication> applications = new ArrayList<>();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        application.setTenantId("tenantId");
        application.setId("testID");
        application.setAddress(new BirthApplicationAddress());
        application.setFather(new User());
        application.setMother(new User());
        applications.add(application);
        return new BirthRegistrationRequest(requestInfo, applications);
    }
}
