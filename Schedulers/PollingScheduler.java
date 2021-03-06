package Schedulers;

import java.util.Iterator;

public class PollingScheduler extends Scheduler {

	private PeriodicTask serverTask;
	
	public PollingScheduler(int serverComputationTime, int serverPeriodTime) {
		this.serverTask = new PeriodicTask("SERVER", serverComputationTime, serverPeriodTime);
		this.periodicTasks.add(serverTask);
	}
	
	@Override
	public String getNextTask() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		} else if (this.scheduleable == false) {
			throw new IllegalStateException("Task set is not scheduleable.");
		}
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

	@Override
	public String getName() {
		return "Polling Scheduler";
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
