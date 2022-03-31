package ad.supplier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * @author natalija
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
@Jacksonized
public class BidResponse {
    @JsonProperty
    private String id;
    @JsonProperty
    private Double bid;
    @JsonProperty
    private String content;
}
