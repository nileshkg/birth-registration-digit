package digit.service;

import digit.TestConfiguration;
import digit.enrichment.BirthApplicationEnrichment;
import digit.kafka.Producer;
import digit.models.BirthApplicationSearchCriteria;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.repository.BirthRegistrationRepository;
import digit.validators.BirthApplicationValidator;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
public class BirthRegistrationServiceTest {
    @Mock
    private BirthApplicationValidator validator;

    @Mock
    private BirthApplicationEnrichment enrichmentUtil;

    @Mock
    private UserService userService;

    @Mock
    private BirthRegistrationRepository birthRegistrationRepository;

    @Mock
    private Producer producer;

    @MockBean
    private BirthRegistrationService birthRegistrationService;


    @Test
    public void registerBtRequest_Test() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        when(birthRegistrationRepository.getApplications(any())).thenReturn(Collections.emptyList());

        List<BirthRegistrationApplication> result = birthRegistrationService.registerBtRequest(request);

        assertEquals(0, result.size());
        verify(validator, times(1)).validateBirthApplication(request);
        verify(enrichmentUtil, times(1)).enrichBirthApplication(request);
        verify(userService, times(1)).callUserService(request);
        verify(producer, times(1)).push(anyString(), eq(request));
    }

    @Test
    public void searchBtApplications_Test() {
        RequestInfo requestInfo = new RequestInfo();
        BirthApplicationSearchCriteria searchCriteria = new BirthApplicationSearchCriteria();
        when(birthRegistrationRepository.getApplications(any())).thenReturn(Collections.emptyList());

        List<BirthRegistrationApplication> result = birthRegistrationService.searchBtApplications(requestInfo, searchCriteria);

        assertEquals(0, result.size());
        verify(birthRegistrationRepository, times(1)).getApplications(searchCriteria);
    }

    @Test
    public void updateBtApplication_Test() {
        BirthRegistrationRequest request = new BirthRegistrationRequest();
        BirthRegistrationApplication application = new BirthRegistrationApplication();
        request.setBirthRegistrationApplications(Collections.singletonList(application));

        BirthRegistrationApplication result = birthRegistrationService.updateBtApplication(request);

        assertEquals(application, result);
        verify(validator, times(1)).validateApplicationExistence(application);
        verify(enrichmentUtil, times(1)).enrichBirthApplicationUponUpdate(request);
        verify(producer, times(1)).push(anyString(), eq(request));
    }
}
