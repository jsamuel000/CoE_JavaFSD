import java.util.*;

class Task implements Comparable {
	String id;
	String description;
	int priority;

	Task(String id, String description, int priority) {
		this.id = id;
		this.description = description;
		this.priority = priority;
	}

	String getId() {
		return id;
	}

	String getDescription() {
		return description;
	}

	int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(Object o) {
		Task other = (Task) o;
		return Integer.compare(other.priority, this.priority);
	}

	@Override
	public String toString() {
		return String.format("Task[ID=%s, Description=%s, Priority=%d]", id, description, priority);
	}
}

class TaskManager {
	PriorityQueue taskQueue = new PriorityQueue();
	Map taskMap = new HashMap();

	void addTask(String id, String description, int priority) {
		if (taskMap.containsKey(id)) {
			System.out.println("Task with ID " + id + " already exists.");
			return;
		}
		Task task = new Task(id, description, priority);
		taskQueue.offer(task);
		taskMap.put(id, task);
	}

	void removeTask(String id) {
		Task task = (Task) taskMap.remove(id);
		if (task != null) {
			taskQueue.remove(task);
		} else {
			System.out.println("Task with ID " + id + " not found.");
		}
	}

	Task getHighestPriorityTask() {
		return (Task) taskQueue.peek();
	}

	void displayTasks() {
		for (Object obj : taskQueue) {
			System.out.println((Task) obj);
		}
	}

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		manager.addTask("1", "Fix bug in login", 5);
		manager.addTask("2", "Implement user authentication", 8);
		manager.addTask("3", "Write documentation", 3);

		System.out.println("Highest Priority Task: " + manager.getHighestPriorityTask());

		manager.removeTask("2");
		System.out.println("After removing task 2, Highest Priority Task: " + manager.getHighestPriorityTask());
	}
}
