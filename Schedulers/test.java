package Schedulers;

public class test {

	public static void main(String args[] ) {
		AperiodicTaskServer atq = new AperiodicTaskServer();
		atq.addTask("AT 1", 5, 3);
		atq.addTask("AT 2", 5, 4);
		atq.addTask("AT 3", 10, 1);
		atq.addTask("AT 4", 3, 3);
		
		for (int i = 0; i < 30; i++) {
			String id = atq.getTaskAtTime(i);
			if (id == null) {
				System.out.println("None");
			} else {
				System.out.println(id);
			}
		}
	}
	
}
