package Server;

/**
 * Created by Brandon on 12/7/2014.
 */
public class AperiodicTask extends Task {
    AperiodicServer parent;
    public AperiodicTask(AperiodicServer parent, int id, long start_time, long deadline, long runtime, TaskManager.AperiodicType type) {
        super(parent, id, start_time, deadline, runtime);
        this.parent = parent;
    }

    @Override
    public boolean run(long time) {
        Task.Status status;
        Task task;
        do {
            task = parent.tasks.poll();

            if(task == null)
                status = Task.Status.COMPLETED;
            else
                status = task.increment(time);

        } while (status == Task.Status.MISSED);

        if (task == null)
            return false;
        else
            return true;
    }
}
