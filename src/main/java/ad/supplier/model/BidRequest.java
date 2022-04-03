package ad.supplier.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

/**
 * @author natalija
 */

/**
 * Request to be sent for auction. (Class representation of json.)
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
@Jacksonized
public class BidRequest {
    @JsonProperty
    private String id;
    @JsonProperty
    private Map<String, String> attributes;
}
