package ad.supplier.exception;

/**
 * @author natalija
 */

/**
 * Thrown if no available bid found for initial request.
 */
public class NoAvailableBidException extends Exception{
    public NoAvailableBidException(int auctionId) {
        super(String.format("There are no available bids for auctionId: %s", auctionId));
    }

    public NoAvailableBidException(int auctionId, Throwable cause) {
        super(String.format("There are no available bids for auctionId: %s"), cause);
    }
}
