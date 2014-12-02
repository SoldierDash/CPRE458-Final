package TaskScheduler;

import java.util.List;

public abstract class Scheduler {

	private int aperiodicServerComputation;
	private int aperiodicServerPeriod;
	
	Scheduler(int aperiodicServerComputation, int aperiodicServerPeriod) {
		this.aperiodicServerComputation = aperiodicServerComputation;
		this.aperiodicServerPeriod = aperiodicServerPeriod;
	}
	
	protected int getAperiodicServerComputation() {
		return this.aperiodicServerComputation;
	}
	
	protected int getAperiodicServerPeriod() {
		return this.aperiodicServerPeriod;
	}
	
	/*
	 * Add a periodic task to the simulation.
	 */
	public abstract void addPeriodicTask(String name, int computation, int period);
	
	/*
	 * Add an aperiodic task to the simulation.
	 */
	public abstract void addAperiodicTask(String name, int start, int computation);
	
	/*
	 * Run the simulation with the tasks passed into the addPeriodicTask() and addAperiodicTask() methods.
	 */
	public abstract void increment();
	
	/* Runs the simulation for the given number of time units. */
	public void run(int timeUnits) {
		for (int i = 0; i < timeUnits; i++) {
			increment();
		}
	}
	
	/*
	 * Returns a list of Tasks, where each element corresponds to the task to be executed at that unit of time.
	 * Returns null if task set is not schedulable.
	 * Throws IllegalStateException if increment() has not been called.
	 */
	public abstract List<? extends Task> getSchedule();
	
	public abstract List<? extends Task> getMissedTasks();
	
}
