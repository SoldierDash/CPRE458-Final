package Server;

import java.util.LinkedList;

/**
 * Created by Brandon on 12/7/2014.
 */
public class AperiodicTask extends Task {
    AperiodicServer parent;
    TaskManager.AperiodicType type;
    String name;
    
    public AperiodicTask(AperiodicServer parent, int id, long start_time, long deadline, long runtime, TaskManager.AperiodicType type) {
        super(parent, id, start_time, deadline, runtime);
        this.parent = parent;
        this.type = type;
    }

    @Override
    public Status run(long time) {
        Task.Status status;
        Task task;
        LinkedList<Task> readd =  new LinkedList<Task>();
        do {
            task = parent.tasks.poll();

            if(task == null) {
                parent.tasks.addAll(readd);
                if(type == TaskManager.AperiodicType.POLLING)
                    return Status.TERMINATED;
                else if (type == TaskManager.AperiodicType.DEFFERABLE)
                    return Status.IGNORE;
                else
                	return Status.TERMINATED;
            }

            //System.out.println(task.getName() + " " + task.getStart() + " " + task.remaining_runtime);

            status = task.increment(time);
            if(status == Status.RUNNING || status == Status.IGNORE)
                readd.add(task);



        } while (status == Task.Status.MISSED || status == Status.TERMINATED || status == Status.IGNORE);

        parent.tasks.addAll(readd);
        name = task.getName();
        return Status.RUNNING;
    }
    
   @Override
   public String getName() {
	   return name;
   }
}
