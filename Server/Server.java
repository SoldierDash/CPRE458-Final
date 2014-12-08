package Server;

/**
 * Created by Brandon on 11/27/2014.
 */
public class Server {
    final long period;
    final long runtime;
    final long relative_deadline;
    final String name;
    long virtual_time; // Time, where 0 occurs at server creation.
    int task_counter = 0;

    public Server(String name, int period, int computation, int deadline) {
        virtual_time = 0;
        this.name = name;
        this.period = period;
        this.runtime = computation;
        this.relative_deadline = deadline;
    }

    public Task increment(long real_time) {
        Task task;

        if(virtual_time % period == 0)
            task = generateTask(real_time);
        else
            task = null;

        virtual_time++;
        return task;
    }

    public String getName() {
        return this.name;
    }

    protected Task generateTask(long time) {
        return new Task(this, task_counter++, time, time + relative_deadline, runtime);
    }
}
