package TaskScheduler;

import java.util.Comparator;

/**
 * A single instance of a task
 */
public class Task {
    long remaining_runtime;
    final long real_deadline;
    TaskServer parent;

    public Task(TaskServer parent, long deadline, long runtime) {
        this.remaining_runtime = runtime;
        this.real_deadline = deadline;
        this.parent = parent;
    }

    public Status increment(long real_time) {
        if(real_time >= real_deadline)
            return Status.MISSED;

        remaining_runtime--;

        if(remaining_runtime <= 0)
            return Status.COMPLETED;
        else
            return Status.RUNNING;
    }

    public long getDeadline() {
        return real_deadline;
    }

    public long getPeriod() {
        return parent.period;
    }

    public enum Status {
        RUNNING,
        COMPLETED,
        MISSED
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
}
