package Server;

import java.util.PriorityQueue;

/**
 * Created by Brandon on 11/27/2014.
 */
public class AperiodicServer extends Server {
    PriorityQueue<Task> tasks;
    TaskManager.AperiodicType type;

    public AperiodicServer(String name, int period, int computation, int deadline, TaskManager.AperiodicType type) {
        super(name, period, computation, deadline);

        this.type = type;

        tasks = new PriorityQueue<Task>(new Task.FIFO());
    }

    public void add(long time, String name, int computation) { // String name, int start, int computation
        tasks.add(new Task(this, task_counter++, time, Long.MAX_VALUE, computation));
    }

    @Override
    protected Task generateTask(long time) {
        return new AperiodicTask(this, task_counter++, time, time + relative_deadline, runtime, type);
    }

    protected Task poll() {
        return tasks.poll();
    }
}
