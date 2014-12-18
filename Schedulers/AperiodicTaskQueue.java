package Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class AperiodicTaskQueue {

	private class Task implements Comparable<Task> {
		
		private String name;
		private int startTime;
		private int computationTime;
		
		public Task(String name, int startTime, int computationTime) {
			this.name = name;
			this.startTime = startTime;
			this.computationTime = computationTime;
		}
		
		public String getName() {
			return name;
		}
		
		public int getStartTime() {
			return startTime;
		}
		
		public int getComputationTime() {
			return computationTime;
		}

		@Override
		public int compareTo(Task o) {
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
	
	private ArrayList<Task> taskList;
	private LinkedList<ListItem> schedule;
	
	public AperiodicTaskQueue() {
		this.taskList = new ArrayList<Task>();
		this.schedule = new LinkedList<ListItem>();
	}
	
	public void addTask(String name, int startTime, int computationTime) {
		taskList.add(new Task(name, startTime, computationTime));
		Collections.sort(taskList);
		schedule.clear();
		for (Task t : taskList) {
			for (int i = 0; i < t.getComputationTime(); i++) {
				schedule.add(new ListItem(t.getName(), t.getStartTime()));
			}
		}
	}
	
	public String getTaskAtTime(int time) {
		String taskName = null;
		if (schedule.isEmpty() == false) {
			if (time >= schedule.peek().getStartTime()) {
				taskName = schedule.removeFirst().getName();
			}
		}

		return taskName;
	}
	
	public boolean isDone() {
		return schedule.isEmpty();
	}
	
}
