package Schedulers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PollingScheduler extends Scheduler {

	public PollingScheduler(int serverComputationTime, int serverPeriodTime) {
		super(serverComputationTime, serverPeriodTime);
	}
	
	public String getNextTask() {
		refreshTasks();
		String nextTask = null;
		PeriodicTask pt = periodicTasks.peek();
		if (pt != null) {
			if (pt.getName().equals("SERVER")) { /* If the periodic server task is to be run, check that an aperiodic task is ready to run. If not, the aperiodi server will not run until its next period arrives. */
				nextTask = this.aperiodicTasks.getTaskAtTime(time);
				if (nextTask == null) {
					this.refreshList.add(this.periodicTasks.remove());
					pt = this.periodicTasks.peek();
					if (pt != null) {
						nextTask = pt.getName();
						updateTask(pt);
					}
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
