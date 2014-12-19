package Statistics;

import java.util.LinkedList;
import java.util.List;

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

		List<Scheduler> schedulers = new LinkedList<Scheduler>();
		List<List<String>> scheduled_tasks = new LinkedList<List<String>>(); // 3D array, woahhhhh

		/*
		1.) Create BackgroundScheduler, PollingScheduler, and DeferrableScheduler objects.
		The PollingScheduler and DeferrableScheduler constructors take a server computation
		time and period arguements.
		*/
		schedulers.add(new BackgroundScheduler());
		schedulers.add(new PollingScheduler(2, 5));
		schedulers.add(new DeferrableScheduler(2, 5));

		/*
		 * 2.) Add the periodic and aperiodic tasks to each scheduler object
		 * 3.) Call the scheduler object's initialize() function. Once this is done, no more tasks can be added.
		 */

		for(Scheduler scheduler: schedulers) {
			scheduler.addPeriodicTask("PT 1", 1, 4);
			scheduler.addPeriodicTask("PT 2", 3, 8);
			scheduler.addAperiodicTask("AT 1", 7, 2);
			scheduler.addAperiodicTask("AT 2", 11, 5);
			scheduler.initialize();

			if(!scheduler.isScheduleable()) {
				System.out.println(scheduler.getName() + " is not scheduleable.");
				continue;
			}

			scheduler.runToCompletion();

			System.out.println(scheduler.getName());
			for (String s : scheduler.getOutput()) {
				System.out.print(s + ", ");
			}
			System.out.println();

			int num_of_tasks = scheduler.getAperiodicTasks().size();

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

			System.out.println("Aperiodic Finish Time: " + scheduler.finishAperiodicTime());
			System.out.println("Average Response Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicResponse()); //(total_response_time/(double)num_of_tasks));
			System.out.println("Average Execution Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicExecution()); //(total_execution_time/(double)num_of_tasks));
			System.out.println("Average Completion Time of " + num_of_tasks + " A_tasks: " + scheduler.avgAperiodicCompletion()); //(total_completion_time/(double)num_of_tasks));
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
