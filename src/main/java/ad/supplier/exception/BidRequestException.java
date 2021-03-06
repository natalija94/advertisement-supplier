package ad.supplier.exception;

/**
 * @author natalija
 */

/**
 * Thrown if some unexpected exception thrown while sending BidRequest.
 */
public class BidRequestException extends Exception {
    String errorStatus;

    public BidRequestException(String message, String errorStatus) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public BidRequestException(Throwable cause, String errorStatus) {
        super(cause);
        this.errorStatus = errorStatus;
    }
}
