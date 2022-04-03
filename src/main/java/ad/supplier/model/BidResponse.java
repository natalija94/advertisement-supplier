package ad.supplier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

/**
 * @author natalija
 */

/**
 * Received response from auction. (Class representation of json.)
 */

@Data
@EqualsAndHashCode
@ToString
@Builder
@Jacksonized
public class BidResponse {
    @JsonProperty
    private int id;
    @JsonProperty
    private double bid;
    @JsonProperty
    private String content;
}
