package Schedulers;

import java.util.Iterator;

public class BackgroundScheduler extends Scheduler {

	public BackgroundScheduler() {}

	@Override
	public String getNextTask() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		} /* else if (this.scheduleable == false) {
			throw new IllegalStateException("Task set is not scheduleable.");
		} */
		refreshTasks();
		String nextTask = null;
		PeriodicTask pt = periodicTasks.peek();
		if (pt != null) {
			nextTask = pt.getName();
			updateTask(pt);
		} else {
			nextTask = aperiodicTasks.getTaskAtTime(this.time);
		}
	
		this.time += 1;
		return (nextTask != null) ? nextTask : "";
	}

	@Override
	public String getName() {
		return "Background Scheduler";
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
