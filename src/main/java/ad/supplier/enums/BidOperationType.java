package ad.supplier.enums;

/**
 * @author natalija
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
