package TaskScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestScheduler extends Scheduler {

	private class TaskItem implements Task {
		
		private String name;
		
		public TaskItem(String name) {
			this.name = name;
		}
		
		public String getName() {
			return this.name;
		}
	}
	
	private ArrayList<String> periodicTasks;
	private ArrayList<String> aperiodicTasks;
	private ArrayList<TaskItem> schedule;
	private Random random;
	
	public TestScheduler(int aperiodicServerComputation, int aperiodicServerPeriod) {
		super(aperiodicServerComputation, aperiodicServerPeriod);
		random = new Random();
		periodicTasks = new ArrayList<String>();
		aperiodicTasks = new ArrayList<String>();
		schedule = new ArrayList<TaskItem>();
	}
	
	@Override
	public void addPeriodicTask(String name, int computation, int period) {
		periodicTasks.add(name);
	}

	@Override
	public void addAperiodicTask(String name, int start, int computation) {
		aperiodicTasks.add(name);
	}

	@Override
	public void increment() {
		int num = random.nextInt(2);
		if (num == 0) {
			TaskItem t;
			if (periodicTasks.size() == 0) {
				t = new TaskItem(null);
			} else {
				num = random.nextInt(periodicTasks.size());
				t = new TaskItem(periodicTasks.get(num));
			}
			schedule.add(t);
		} else {
			TaskItem t;
			if (aperiodicTasks.size() == 0) {
				t = new TaskItem(null);
			} else {
				num = random.nextInt(aperiodicTasks.size());
				t = new TaskItem(aperiodicTasks.get(num));
			}
			schedule.add(t);
		}
	}

	@Override
	public List<? extends Task> getSchedule() {
		return schedule;
	}

	@Override
	public List<? extends Task> getMissedTasks() {
		// TODO Auto-generated method stub
		return null;
	}

}
