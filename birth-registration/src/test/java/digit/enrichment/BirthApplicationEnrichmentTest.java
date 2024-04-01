package digit.enrichment;

import digit.TestConfiguration;
import digit.models.*;
import digit.util.IdgenUtil;
import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Import(TestConfiguration.class)
public class BirthApplicationEnrichmentTest {
    @MockBean
    private BirthApplicationEnrichment birthApplicationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;


    @Test
    public void enrichBirthApplication_Test() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        when(idgenUtil.getIdList(any(), any(), any(), any(), anyInt()))
                .thenReturn(Arrays.asList("BTRTEST111", "BTRTEST222"));

        birthApplicationEnrichment.enrichBirthApplication(birthRegistrationRequest);

        List<BirthRegistrationApplication> applications = birthRegistrationRequest.getBirthRegistrationApplications();
        assertNotNull(applications);
        assertEquals(1, applications.size());
        for (BirthRegistrationApplication application : applications) {
            assertNull(application.getAuditDetails());
            assertNotNull(application.getId());
            assertNotNull(application.getApplicationNumber());
            assertNotNull(application.getAddress());
        }
    }

    @Test
    public void enrichBirthApplicationUponUpdate_Test() {
        BirthRegistrationRequest birthRegistrationRequest = createBirthRegistrationRequest();
        BirthRegistrationApplication application = birthRegistrationRequest.getBirthRegistrationApplications().get(0);
        AuditDetails auditDetails = new AuditDetails("user1", "user2", 143535L, 123457L);
        application.setAuditDetails(auditDetails);

        birthApplicationEnrichment.enrichBirthApplicationUponUpdate(birthRegistrationRequest);

        assertEquals("user1", application.getAuditDetails().getCreatedBy());
        assertEquals(143535L, application.getAuditDetails().getCreatedTime());
        assertEquals("user2", application.getAuditDetails().getLastModifiedBy());
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
        application.setApplicationNumber("testApp");
        applications.add(application);
        return new BirthRegistrationRequest(requestInfo, applications);
    }
}
