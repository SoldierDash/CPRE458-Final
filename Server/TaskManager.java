package Server;

import TaskScheduler.Scheduler;

import java.util.*;

/**
 * Task Manager
 *
 * Created by Brandon on 11/27/2014.
 */
public class TaskManager extends Scheduler {
    public enum AperiodicType {
        POLLING,
        DEFFERABLE,
        SPORADIC,
        PRIORITY_EXCHANGE
    }

    public enum SchedulerType {
        RMS,
        EDF
    }

    long time; //no see
    Collection<Server> servers;
    List<Task> task_history;
    List<Task> missed_tasks;
    PriorityQueue<Task> tasks;

    AperiodicServer aperiodic_server;


    public TaskManager(SchedulerType sched_type, AperiodicType aper_type, String aperiodicName, int aperiodicPeriod, int aperiodicComp) {
        time = 0;
        servers = new HashSet<Server>();
        task_history = new LinkedList<Task>();
        missed_tasks = new LinkedList<Task>();
        switch(sched_type) {
            case RMS:
                tasks = new PriorityQueue<Task>(new Task.RMS());
            case EDF:
                tasks = new PriorityQueue<Task>(new Task.EDF());
        }

        aperiodic_server = new AperiodicServer(aperiodicName, aperiodicComp, aperiodicPeriod, aperiodicComp, aper_type);
        addServer(aperiodic_server);
    }

    private void addServer(Server server) {
        servers.add(server);
    }

    @Override
    public void addPeriodicTask(String name, int period, int computation) {
        addServer(new Server(name, period, computation, period));
    }

    @Override
    public void addAperiodicTask(String name, int computation) {
        aperiodic_server.add(time, name, computation);
    }

    public void increment() {

        for(Server server: servers) {
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

        } while (status != Task.Status.RUNNING && status != Task.Status.COMPLETED);
        time++;
    }

    @Override
    public List<? extends TaskScheduler.Task> getSchedule() {
        return task_history;
    }

    @Override
    public List<? extends TaskScheduler.Task> getMissedTasks() {
        return missed_tasks;
    }
}
