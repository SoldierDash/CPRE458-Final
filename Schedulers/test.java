package Schedulers;

public class test {

	public static void main(String args[] ) {
		PollingScheduler ps = new PollingScheduler(2, 5);
		ps.addPeriodicTask("PT1", 2, 5);
		ps.addPeriodicTask("PT2", 4, 15);
		ps.addAperiodicTask("AT1", 3, 1);
		ps.addAperiodicTask("AT2", 6, 2);
		ps.addAperiodicTask("AT3", 11, 1);
		ps.addAperiodicTask("AT4", 11, 1);
		
		for (int i = 0; i < 15; i++) {
			System.out.println(ps.getNextTask());
		}
	}
	
}
