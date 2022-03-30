package ad.supplier.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author natalija
 */
@Data
@EqualsAndHashCode
@ToString
@Builder
public class BidResponse {
    private String id;
    private Double bid;
    private String content;
}
