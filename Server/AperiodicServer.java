package TaskScheduler;

/**
 * Created by Brandon on 11/27/2014.
 */
public class AperiodicServer extends TaskServer {
    public AperiodicServer(int period, int computation, int deadline) {
        super(period, computation, deadline);
    }
}
