package digit.service;

import digit.TestConfiguration;
import digit.enrichment.BirthApplicationEnrichment;
import digit.models.BirthApplicationSearchCriteria;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.repository.BirthRegistrationRepository;
import digit.validators.BirthApplicationValidatorTest;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
public class BirthRegistrationServiceTest {

    @MockBean
    private BirthApplicationValidatorTest validator;

    @MockBean
    private BirthApplicationEnrichment enrichmentUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private WorkflowService workflowService;

    @MockBean
    private BirthRegistrationRepository birthRegistrationRepository;

    @MockBean
    private BirthRegistrationService birthRegistrationService;

    @Test
    public void registerBtRequestSuccess(){
        BirthRegistrationRequest birthRegistrationRequest = new BirthRegistrationRequest();
        birthRegistrationRequest.setRequestInfo(new RequestInfo());
        birthRegistrationRequest.setBirthRegistrationApplications(new ArrayList<>());
        assertEquals(birthRegistrationService.registerBtRequest(birthRegistrationRequest), birthRegistrationRequest.getBirthRegistrationApplications());
    }

    @Test
    public void searchBtApplicationsSuccess(){
        RequestInfo requestInfo = new RequestInfo();
        BirthApplicationSearchCriteria birthApplicationSearchCriteria = new BirthApplicationSearchCriteria();
        birthApplicationSearchCriteria.setTenantId("123");
        List<BirthRegistrationApplication> applications = new ArrayList<>();
        when(birthRegistrationRepository.getApplications(birthApplicationSearchCriteria)).thenReturn(applications);
        assertEquals(birthRegistrationService.searchBtApplications(requestInfo,birthApplicationSearchCriteria), new ArrayList<>());
    }

    @Test
    public void updateBtApplicationSuccess(){
        BirthRegistrationRequest birthRegistrationRequest = new BirthRegistrationRequest();
        birthRegistrationRequest.setRequestInfo(new RequestInfo());
        birthRegistrationRequest.setBirthRegistrationApplications(new ArrayList<>());
        assertEquals(birthRegistrationService.updateBtApplication(birthRegistrationRequest), null);
    }

}
