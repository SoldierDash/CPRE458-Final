package Schedulers;

public class test {

	public static void main(String args[] ) {
		/* Test 1: http://beru.univ-brest.fr/~singhoff/cheddar/publications/muller14.pdf */
		/*
		DeferrableScheduler ps1 = new DeferrableScheduler(2, 5);
		ps1.addPeriodicTask("PT1", 2, 5);
		ps1.addPeriodicTask("PT2", 4, 15);
		ps1.addAperiodicTask("AT1", 3, 1);
		ps1.addAperiodicTask("AT2", 6, 2);
		ps1.addAperiodicTask("AT3", 11, 1);
		ps1.addAperiodicTask("AT4", 11, 1);
		
		for (int i = 0; i < 15; i++) {
			System.out.println(ps1.getNextTask());
		}
		*/
		
		/* Test 2: http://www.electro.fisica.unlp.edu.ar/temas/p7/HRT/Chapter5.pdf */
		
		DeferrableScheduler ps2 = new DeferrableScheduler(2, 5);
		ps2.addPeriodicTask("PT1", 1, 4);
		ps2.addPeriodicTask("PT2", 2, 6);
		ps2.addAperiodicTask("AT1", 2, 2);
		ps2.addAperiodicTask("AT2", 8, 1);
		ps2.addAperiodicTask("AT3", 12, 2);
		ps2.addAperiodicTask("AT4", 19, 1);
		
		for (int i = 0; i < 24; i++) {
			System.out.println(ps2.getNextTask());
		}
		
	}
	
}
