package digit.controllers;


import digit.models.*;
import digit.service.BirthRegistrationService;
import digit.util.ResponseInfoFactory;
import digit.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.egov.common.contract.response.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import javax.validation.Valid;
    import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-03-14T01:36:21.586087Z[Africa/Dakar]")
@Controller
    @RequestMapping("")
    public class BirthApiController{

        private final ObjectMapper objectMapper;

        private final HttpServletRequest request;

        @Autowired
        private BirthRegistrationService birthRegistrationService;

        @Autowired
        private ResponseInfoFactory responseInfoFactory;

        @Autowired
        public BirthApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        }

                @RequestMapping(value="/birth/registration/v1/_create", method = RequestMethod.POST)
                public ResponseEntity<BirthRegistrationResponse> birthRegistrationV1CreatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new Birth Registration Application(s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody BirthRegistrationRequest body) {
                    List<BirthRegistrationApplication> applications = birthRegistrationService.registerBtRequest(body);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(applications).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

                @RequestMapping(value="/birth/registration/v1/_search", method = RequestMethod.POST)
                public ResponseEntity<BirthRegistrationResponse> birthRegistrationV1SearchPost(@Parameter(in = ParameterIn.DEFAULT, description = "Parameter to carry Request metadata in the request body", schema=@Schema()) @RequestBody RequestInfoWrapper requestInfoWrapper, @Valid @ModelAttribute BirthApplicationSearchCriteria birthApplicationSearchCriteria) {
                    List<BirthRegistrationApplication> applications = birthRegistrationService.searchBtApplications(requestInfoWrapper.getRequestInfo(), birthApplicationSearchCriteria);
                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
                    BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(applications).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(response,HttpStatus.OK);
                }

                @RequestMapping(value="/birth/registration/v1/_update", method = RequestMethod.POST)
                public ResponseEntity<BirthRegistrationResponse> birthRegistrationV1UpdatePost(@Parameter(in = ParameterIn.DEFAULT, description = "Details for the new (s) + RequestInfo meta data.", required=true, schema=@Schema()) @Valid @RequestBody BirthRegistrationRequest body) {
                    BirthRegistrationApplication application = birthRegistrationService.updateBtApplication(body);

                    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(body.getRequestInfo(), true);
                    BirthRegistrationResponse response = BirthRegistrationResponse.builder().birthRegistrationApplications(Collections.singletonList(application)).responseInfo(responseInfo).build();
                    return new ResponseEntity<>(response, HttpStatus.OK);
                }

        }
