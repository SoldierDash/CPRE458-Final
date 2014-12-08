package TaskScheduler;

import java.util.*;

/**
 * Task Manager
 *
 * Created by Brandon on 11/27/2014.
 */
public class TaskManager {

    long time; //no see
    Collection<TaskServer> servers;
    Collection<Task> missed_tasks;
    List<Task> task_history;
    PriorityQueue<Task> tasks;


    TaskManager() {
        time = 0;
        servers = new HashSet<TaskServer>();
        task_history = new LinkedList<Task>();
        missed_tasks = new LinkedList<Task>();
        tasks = new PriorityQueue<Task>(new Task.EDF());
    }

    public List<? extends Task> getHistory() {
        return task_history;
    }

    public Collection<? extends Task> getMissed() { return missed_tasks; }

    public void addServer(TaskServer server) {
        servers.add(server);
    }

    public void increment() {

        for(TaskServer server: servers) {
            Task new_task = server.increment(time);
            if(new_task != null) {
                tasks.add(new_task);
            }
        }

        // Attempt to run next task until a task runs/finishs
        Task.Status status;
        Task task;
        do {
            task = tasks.poll();

            if(task == null)
                status = Task.Status.COMPLETED;
            else
                status = task.increment(time);

            switch(status) {
                case RUNNING:
                    tasks.add(task);
                case COMPLETED:
                    task_history.add(task);
                    break;
                case MISSED:
                    missed_tasks.add(task);
            }

        } while (status == Task.Status.MISSED);
        time++;
    }
}
