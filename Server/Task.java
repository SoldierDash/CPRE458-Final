package Server;

import java.util.Comparator;

/**
 * A single instance of a task
 */
public class Task implements TaskScheduler.Task {

    public enum Status {
        RUNNING,
        COMPLETED,
        MISSED,
        IGNORE
    }

    final int id;

    final long start_time;
    final long real_deadline;
    long remaining_runtime;
    Server parent;


    public Task(Server parent, int id, long start_time, long deadline, long runtime) {
        this.parent = parent;
        this.id = id;
        this.start_time = start_time;
        this.real_deadline = deadline;
        this.remaining_runtime = runtime;
    }

    public Status increment(long real_time) {
        if(real_time >= real_deadline)
            return Status.MISSED;

        if(!run(real_time))
            return Status.IGNORE;

        remaining_runtime--;

        if(remaining_runtime <= 0)
            return Status.COMPLETED;
        else
            return Status.RUNNING;
    }

    protected boolean run (long time) {
        return true;
    }

    public long getDeadline() {
        return real_deadline;
    }

    public long getPeriod() {
        return parent.period;
    }

    public long getStart() {return this.start_time; }

    @Override
    public String getName() {
        return parent.getName() + " " + id;
    }

    public static class EDF implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            return Long.compare(o1.getDeadline(), o2.getDeadline());
        }
    }

    public static class RMS implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            return Long.compare(o1.getPeriod(), o2.getPeriod());
        }
    }

    public static class FIFO implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            return Long.compare(o1.getStart(), o2.getStart());
        }
    }
}
