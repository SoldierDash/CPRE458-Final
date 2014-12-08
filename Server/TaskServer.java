package TaskScheduler;

/**
 * Created by Brandon on 11/27/2014.
 */
public class TaskServer {
    final long period;
    final long runtime;
    final long relative_deadline;
    long virtual_time; // Time, where 0 occurs at server creation.

    public TaskServer(int period, int computation, int deadline) {
        virtual_time = 0;
        this.period = period;
        this.runtime = computation;
        this.relative_deadline = deadline;
    }

    public Task increment(long real_time) {
        Task task = null;

        if(virtual_time % period == 0)
            task = new Task(this, real_time + relative_deadline, runtime);

        virtual_time++;
        return task;
    }
}
