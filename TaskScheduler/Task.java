package TaskScheduler;


public interface Task {

	public String getName();

	public long getDeadline();

	public long getPeriod();

	public long getStart();
	
}
