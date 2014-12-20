package Statistics;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import Schedulers.*;

/* Excerpt from slide
CprE 458/558: Real-Time Systems (G. Manimaran)4
Performance Metrics
•Minimizing Schedule Length.
•Minimizing Sum of Completion Times.
•Maximizing Weighted Sum of Values (Useful in RT systems).
•Minimizing the Maximum Lateness (useful in RT systems).
 */

public class CombinedSchedulingStatistics {

	public static void main(String[] args) {

		List<List<Scheduler>> all_schedulers = new LinkedList<List<Scheduler>>();

		int as_time=1, as_period=10;
		System.out.println("Aperiodic Server (c="+as_time+",p="+as_period+")\t\t\t\t i=Number of aperiodic tasks where start=0, c=2");
		System.out.println("i\tFinish Time\t\t\tResponse Time\t\t\tExecution time\t\t\tLatency");
		System.out.println("\tBackground\tPolling\tDeferrable\tBackground\tPolling\tDeferrable\tBackground\tPolling\tDeferrable\tBackground\tPolling\tDeferrable");

		// Repeat test for all schedulers, incrementing i each time
		for(int i = 0; i <= 1000; i++) {
			List<Scheduler> schedulers = new LinkedList<Scheduler>();
			all_schedulers.add(schedulers);

			schedulers.add(new BackgroundScheduler());
			schedulers.add(new PollingScheduler(as_time, as_period));
			schedulers.add(new DeferrableScheduler(as_time, as_period));


			for (Scheduler scheduler : schedulers) {
				// Insert tasks
				scheduler.addPeriodicTask("PT 1", 1, 10);
				scheduler.addPeriodicTask("PT 2", 1, 9);
				for(int j = 0; j < i; j++) {
					scheduler.addAperiodicTask("AT " + i, 0, 2);
				}


				scheduler.initialize();
/*
				if (!scheduler.isScheduleable()) {
					System.out.println(scheduler.getName() + " is not scheduleable.");
					//System.exit(0);
				}
				*/

				scheduler.runToCompletion();

				/*
				System.out.println(scheduler.getName());
				for (String s : scheduler.getOutput()) {
					System.out.print(s + ", ");
				}
				System.out.println();
				*/

				/*
				for (AperiodicTaskQueue.AperiodicTask at : scheduler.getAperiodicTasks()) {

					System.out.println(at.getName() + ":");
					System.out.println(("\tStart time: " + at.getArrivalTime()));
					System.out.println(("\tTime Started: " + at.getTimeStarted()));
					System.out.println(("\tTime Ended: " + at.getTimeEnded()));
					System.out.println("\tResponse Time: " + at.getAvgResponseTime());
					System.out.println("\tExecution Time: " + at.getAvgExecutionTime());
					System.out.println("\tCompletion Time: " + at.getAvgCompletionTime());

				}
				System.out.println();
				*/

				/*
				System.out.println("Aperiodic Finish Time: " + scheduler.finishAperiodicTime());
				System.out.println("Average Response Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicResponse()); //(total_response_time/(double)num_of_tasks));
				System.out.println("Average Execution Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicExecution()); //(total_execution_time/(double)num_of_tasks));
				System.out.println("Average Completion Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicCompletion()); //(total_completion_time/(double)num_of_tasks));
				System.out.println();
				*/
			}

			System.out.print(i);
			for(Scheduler s : schedulers) {
				System.out.printf("\t" + s.finishAperiodicTime());
			}
			for(Scheduler s : schedulers) {
				System.out.printf("\t%.2f", s.avgAperiodicResponse());
			}
			for(Scheduler s : schedulers) {
				System.out.printf("\t%.2f", s.avgAperiodicExecution());
			}
			for(Scheduler s : schedulers) {
				System.out.printf("\t%.2f", s.avgAperiodicResponse() + s.avgAperiodicExecution());
			}


			System.out.println();
		}

/*
		
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
			System.out.println(("\tStart time: " + at.getArrivalTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getArrivalTime()));
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
			System.out.println(("\tStart time: " + at.getArrivalTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getArrivalTime()));
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
			System.out.println(("\tStart time: " + at.getArrivalTime()));
			System.out.println(("\tTime Started: " + at.getTimeStarted()));
			System.out.println(("\tTime Ended: " + at.getTimeEnded()));
			System.out.println("Response Time: " + (at.getTimeStarted() - at.getArrivalTime()));
			System.out.println("Execution Time: " + (at.getTimeEnded() - at.getTimeStarted()));
		}
		System.out.println();
		*/
	}

}
