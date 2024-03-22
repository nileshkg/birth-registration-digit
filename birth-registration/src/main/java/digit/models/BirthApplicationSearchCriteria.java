package digit.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

/**
 * BirthApplicationSearchCriteria
 */
@Validated
@javax.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-03-14T01:36:21.586087Z[Africa/Dakar]")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BirthApplicationSearchCriteria   {
        @JsonProperty("tenantId")
        @NotNull
        private String tenantId = null;

        @JsonProperty("status")
        private String status = null;

        @JsonProperty("ids")
        @Size(max=50)
        private List<String> ids = null;

        @JsonProperty("applicationNumber")
        @Size(min=2,max=64)
        private String applicationNumber = null;


        public BirthApplicationSearchCriteria addIdsItem(String idsItem) {
            if (this.ids == null) {
            this.ids = new ArrayList<>();
            }
        this.ids.add(idsItem);
        return this;
        }

}
