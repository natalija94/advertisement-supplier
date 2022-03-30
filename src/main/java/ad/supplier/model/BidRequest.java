package ad.supplier.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;

/**
 * @author natalija
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
public class BidRequest {
    private String id;
    private Map<String, String> attributes;
}
