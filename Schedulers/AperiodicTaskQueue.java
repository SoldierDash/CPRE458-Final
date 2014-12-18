package Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class AperiodicTaskQueue {

	public class AperiodicTask implements Comparable<AperiodicTask> {
		
		private String name;
		private int arrivalTime;
		private int computationTime;
		private int timeStarted;
		private int timeEnded;
		
		public AperiodicTask(String name, int arrivalTime, int computationTime) {
			this.name = name;
			this.arrivalTime = arrivalTime;
			this.computationTime = computationTime;
			this.timeStarted = -1;
			this.timeEnded = -1;
		}
		
		public String getName() {
			return name;
		}
		
		public int getStartTime() {
			return arrivalTime;
		}
		
		public int getComputationTime() {
			return computationTime;
		}

		public int getTimeStarted() {
			return this.timeStarted;
		}
		
		public void setTimeStarted(int timeStarted) {
			this.timeStarted = timeStarted;
		}
		
		public int getTimeEnded() {
			return this.timeEnded;
		}
		
		public void setTimeEnded(int timeEnded) {
			this.timeEnded = timeEnded;
		}
		
		@Override
		public int compareTo(AperiodicTask o) {
			return this.getStartTime() - o.getStartTime();
		}
		
	}
	
	private class ListItem {
		
		private String name;
		private int startTime;
		
		public ListItem(String name, int startTime) {
			this.name = name;
			this.startTime = startTime;
		}
		
		public String getName() {
			return this.name;
		}
		
		public int getStartTime() {
			return this.startTime;
		}
		
	}
	
	private HashMap<String, AperiodicTask> tasks;
	private LinkedList<ListItem> schedule;
	private boolean initialized;
	
	public AperiodicTaskQueue() {
		this.tasks = new HashMap<String, AperiodicTask>();
		this.schedule = new LinkedList<ListItem>();
		this.initialized = false;
	}
	
	public void addTask(String name, int arrivalTime, int computationTime) {
		if (this.initialized == true) {
			throw new IllegalStateException("Tasks cannot be added to an aperiodic task queue once it has been initialized.");
		}
		tasks.put(name, new AperiodicTask(name, arrivalTime, computationTime));
	}
	
	public void initialize() {
		LinkedList<AperiodicTask> taskList = new LinkedList<AperiodicTask>();
		for (AperiodicTask at : tasks.values()) {
			taskList.add(at);
		}
		Collections.sort(taskList);
		schedule.clear();
		for (AperiodicTask t : taskList) {
			for (int i = 0; i < t.getComputationTime(); i++) {
				schedule.add(new ListItem(t.getName(), t.getStartTime()));
			}
		}
		this.initialized = true;
	}
	
	public String getTaskAtTime(int time) {
		if (this.initialized == false) {
			throw new IllegalStateException("Aperiodic task queues must be initialized before they can be used.");
		}
		String taskName = null;
		if (schedule.isEmpty() == false) {
			if (time >= schedule.peek().getStartTime()) {
				taskName = schedule.removeFirst().getName();
				if (tasks.get(taskName).getTimeStarted() == -1) {
					tasks.get(taskName).setTimeStarted(time);
				}
				if (schedule.isEmpty()) {
					tasks.get(taskName).setTimeEnded(time + 1);
				} else {
					if (schedule.peek().getName().equals(taskName) == false) {
						tasks.get(taskName).setTimeEnded(time + 1);
					}
				}
			}
		}

		return taskName;
	}
	
	public boolean isDone() {
		if (this.initialized == false) {
			throw new IllegalStateException("Aperiodic task queues must be initialized before they can be used.");
		}
		return schedule.isEmpty();
	}
	
	public ArrayList<AperiodicTask> getTasks() {
		if (this.initialized == false) {
			throw new IllegalStateException("Aperiodic task queues must be initialized before they can be used.");
		}
		ArrayList<AperiodicTask> taskList = new ArrayList<AperiodicTask>();
		for (AperiodicTask at : tasks.values()) {
			taskList.add(at);
		}
		Collections.sort(taskList, new Comparator<AperiodicTask>() {

			@Override
			public int compare(AperiodicTask arg0, AperiodicTask arg1) {
				return arg0.getName().compareTo(arg1.getName());
			}
			
		});
		return taskList;
	}
	
}
