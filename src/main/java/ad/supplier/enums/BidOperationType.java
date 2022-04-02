package ad.supplier.enums;

/**
 * @author natalija
 */

/**
 * Definition of auction request processing.
 * Can be configured as: sequential or parallel broadcast. (.properties file)
 */
public enum BidOperationType {

    PARALLEL("PARALLEL"), SEQUENTIAL("SEQUENTIAL");

    BidOperationType(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
