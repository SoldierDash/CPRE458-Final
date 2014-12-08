package Server;

import TaskScheduler.Task;

import java.util.List;

/**
 * Created by Brandon on 11/29/2014.
 */
public class Test {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager(TaskManager.SchedulerType.EDF, TaskManager.AperiodicType.POLLING, "AS", 4, 1);
        manager.addPeriodicTask("S1", 3, 1);
        manager.addPeriodicTask("S2", 3, 1);
        //manager.addPeriodicTask("S3", 4, 1);
        manager.addAperiodicTask("Test", 3, 2);
        for(int i = 0; i < 10; i++) {
            manager.increment();
            List<? extends TaskScheduler.Task> scheduled = manager.getSchedule();
            System.out.print("Completed: ");
            for(Task task: scheduled) {
                if(task == null)
                    System.out.print("N/A" + ", ");
                else
                    System.out.print(task.getName() +  ", ");
            }
            System.out.println();

            List<? extends TaskScheduler.Task> missed = manager.getMissedTasks();
            System.out.print("Missed:    ");
            for(Task task: missed) {
                System.out.print(task.getName() + ", ");
            }
            System.out.println();
        }
    }
}
