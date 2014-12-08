package TaskScheduler;

/**
 * Created by Brandon on 11/29/2014.
 */
public class Test {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.addServer(new TaskServer(2, 1, 2));
        manager.addServer(new TaskServer(3, 1, 3));
        manager.addServer(new TaskServer(2, 1, 2));
        for(int i = 0; i < 10; i++) {
            manager.increment();
            System.out.println("Completed: " + manager.getHistory());
            System.out.println("Missed:    " + manager.getMissed());
        }
    }
}
