package Schedulers;

import java.util.Iterator;

public class DeferrableScheduler extends Scheduler {

	private PeriodicTask serverTask;
	
	public DeferrableScheduler(int serverComputationTime, int serverPeriodTime) {
		this.serverTask = new PeriodicTask("SERVER", serverComputationTime, serverPeriodTime);
		this.periodicTasks.add(serverTask);
	}
	
	@Override
	public String getNextTask() {
		refreshTasks();
		String nextTask = null;
		PeriodicTask pt = periodicTasks.peek();
		if (pt != null) {
			if (pt.getName().equals("SERVER")) {
				nextTask = this.aperiodicTasks.getTaskAtTime(time);
				if (nextTask == null) { /* If there is no aperiodic task to be run, the next periodic task will be run for one time unit. */
					periodicTasks.remove(); /* Must remove the periodic task for the aperiodic server from the priority queue so that we can view the next periodic task to be run. */
					PeriodicTask pt2 = periodicTasks.peek();
					if (pt2 != null) {
						nextTask = pt2.getName();
						updateTask(pt2);
					}
					periodicTasks.add(pt); /* Add the periodic task for the aperiodic server back into the priority queue so that it can be chosen at a later time if an aperiodic task arrives. */
				} else {
					updateTask(pt);
				}
			} else {
				nextTask = pt.getName();
				updateTask(pt);
			}
		}
	
		this.time += 1;
		return (nextTask != null) ? nextTask : "";
	}
	
	/* 
	 * Decrement the remaining computation time of the given periodic task, removing it from the periodic tasks lists and adding
	 * it to the refresh list if necessary.
	 */
	private void updateTask(PeriodicTask pt) {
		if (pt.getTimeRemaining() > 1) {
			pt.setTimeRemaining(pt.getTimeRemaining() - 1);
		} else {
			this.refreshList.add(periodicTasks.remove());
		}
	}
	
	/* Replenish the computation time of every task in the task list whos period has been renewed. */
	private void refreshTasks() {
		/* The Deferrable periodic task server might not have been added to the refresh list, so we manually udpate it. */
		if (this.time % serverTask.getPeriod() == 0) {
			serverTask.refresh();
		}
		Iterator<PeriodicTask> iterator = this.refreshList.iterator();
		while (iterator.hasNext()) {
			PeriodicTask pt = iterator.next();
			if (this.time % pt.getPeriod() == 0) {
				pt.refresh();
				iterator.remove();
				this.periodicTasks.add(pt);
			}
		}
	}
	
}
