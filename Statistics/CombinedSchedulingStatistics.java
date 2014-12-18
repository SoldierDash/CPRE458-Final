package Statistics;

import java.util.ArrayList;

import Schedulers.AperiodicTaskQueue;
import Schedulers.BackgroundScheduler;
import Schedulers.DeferrableScheduler;
import Schedulers.PollingScheduler;

public class CombinedSchedulingStatistics {

	public static void main(String[] args) {
		BackgroundScheduler bs = new BackgroundScheduler();
		PollingScheduler ps = new PollingScheduler(2, 5);
		DeferrableScheduler ds = new DeferrableScheduler(2, 5);
		
		bs.addPeriodicTask("PT 1", 1, 4);
		bs.addPeriodicTask("PT 2", 3, 8);
		bs.addAperiodicTask("AT 1", 7, 2);
		bs.addAperiodicTask("AT 2", 11, 5);
		bs.initialize();
		
		ps.addPeriodicTask("PT 1", 1, 4);
		ps.addPeriodicTask("PT 2", 3, 8);
		ps.addAperiodicTask("AT 1", 7, 2);
		ps.addAperiodicTask("AT 2", 11, 5);
		ps.initialize();
		
		ds.addPeriodicTask("PT 1", 1, 4);
		ds.addPeriodicTask("PT 2", 3, 8);
		ds.addAperiodicTask("AT 1", 7, 2);
		ds.addAperiodicTask("AT 2", 11, 5);
		ds.initialize();
		
		ArrayList<String> backgroundSchedule = new ArrayList<String>();
		ArrayList<String> pollingSchedule = new ArrayList<String>();
		ArrayList<String> deferrableSchedule = new ArrayList<String>();
		while (bs.isDone() == false || ps.isDone() == false || ds.isDone() == false) {
			backgroundSchedule.add(bs.getNextTask());
			pollingSchedule.add(ps.getNextTask());
			deferrableSchedule.add(ds.getNextTask());
		}
		
		System.out.println("Background Schedule:");
		for (String s : backgroundSchedule) {
			System.out.print(s + ", ");
		}
		System.out.println();
		for (AperiodicTaskQueue.AperiodicTask at : bs.getAperiodicTasks()) {
			System.out.println(at.getName() + ":");
			System.out.println(("\tStart time: " + at.getStartTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getStartTime()));
			System.out.println("Execution Time: " + (at.getTimeEnded() - at.getTimeStarted()));
		}
		System.out.println();
		
		System.out.println();
		System.out.println("Polling Schedule:");
		for (String s : pollingSchedule) {
			System.out.print(s + ", ");
		}
		System.out.println();
		for (AperiodicTaskQueue.AperiodicTask at : ps.getAperiodicTasks()) {
			System.out.println(at.getName() + ":");
			System.out.println(("\tStart time: " + at.getStartTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getStartTime()));
			System.out.println("Execution Time: " + (at.getTimeEnded() - at.getTimeStarted()));
		}
		System.out.println();
		
		System.out.println();
		System.out.println("Deferrable Schedule:");
		for (String s : deferrableSchedule) {
			System.out.print(s + ", ");
		}
		System.out.println();
		for (AperiodicTaskQueue.AperiodicTask at : ds.getAperiodicTasks()) {
			System.out.println(at.getName() + ":");
			System.out.println(("\tStart time: " + at.getStartTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getStartTime()));
			System.out.println("Execution Time: " + (at.getTimeEnded() - at.getTimeStarted()));
		}
		System.out.println();
		
	}

}
