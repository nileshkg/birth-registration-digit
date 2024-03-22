package digit.enrichment;

import digit.TestConfiguration;
import digit.models.BirthRegistrationApplication;
import digit.models.BirthRegistrationRequest;
import digit.service.UserService;
import digit.util.IdgenUtil;
import digit.util.UserUtil;
import org.egov.common.contract.request.RequestInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Import(TestConfiguration.class)
public class BirthApplicationEnrichmentTest {

    @MockBean
    private IdgenUtil idgenUtil;

    @MockBean
    private UserService userService;

    @MockBean
    private BirthApplicationEnrichment birthApplicationEnrichment;

    @Test
    public void enrichBirthApplicationTest() {
        BirthRegistrationRequest birthRegistrationRequest = new BirthRegistrationRequest();
        BirthRegistrationApplication birthRegistrationApplication = new BirthRegistrationApplication();
        birthRegistrationApplication.setTenantId("pb");

        birthRegistrationRequest.setBirthRegistrationApplications(Arrays.asList(birthRegistrationApplication));
        when(idgenUtil.getIdList(birthRegistrationRequest.getRequestInfo(), birthRegistrationRequest.getBirthRegistrationApplications().get(0).getTenantId(),
                "product.id", "P-[cy:yyyy-MM-dd]-[SEQ_PRODUCT_P]", birthRegistrationRequest.getBirthRegistrationApplications()
                        .size())).thenReturn(new ArrayList<>());

        birthApplicationEnrichment.enrichBirthApplication(birthRegistrationRequest);
        verify(idgenUtil, times(0)).getIdList(any(RequestInfo.class), anyString(), anyString(), anyString(), anyInt());
    }
}
