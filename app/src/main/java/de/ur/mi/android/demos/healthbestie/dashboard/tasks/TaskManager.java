package de.ur.mi.android.demos.healthbestie.dashboard.tasks;

import android.content.Context;
import java.util.ArrayList;
import java.util.Collections;
import de.ur.mi.android.demos.healthbestie.dashboard.room.TaskDatabaseHelper;

public class TaskManager {

    private final TaskManagerListener listener;
    private final ArrayList<Task> tasks;
    private final TaskDatabaseHelper db;

    public TaskManager(Context context, TaskManagerListener listener) {
        this.listener = listener;
        this.db = new TaskDatabaseHelper(context);
        this.tasks = db.getAllTasks();
    }

    public void requestUpdate() {
        listener.onTaskListUpdated();
    }

    public void addTask(String description) {
        Task taskToAdd = new Task(description);
        db.addTask(taskToAdd);
        tasks.add(taskToAdd);
        listener.onTaskListUpdated();
    }


    public void toggleTaskStateAtPosition(int position) {
        Task task = tasks.get(position);
        toggleTaskState(task);
    }

    public void toggleTaskStateForId(String id) {
        for (Task task : tasks) {
            if (task.id.toString().equals(id)) {
                toggleTaskState(task);
            }
        }
    }

    private void toggleTaskState(Task taskToToggle) {
        if (taskToToggle != null) {
            if (taskToToggle.isClosed()) {
                taskToToggle.markAsOpen();
            } else {
                taskToToggle.markAsClosed();
            }
            db.updateTask(taskToToggle);
            listener.onTaskListUpdated();
        }
    }

    public ArrayList<Task> getCurrentTasks() {
        ArrayList<Task> currentTasks = new ArrayList<>();
        for (Task task : tasks) {
            currentTasks.add(task.copy());
        }
        Collections.sort(currentTasks);
        return currentTasks;
    }

    public interface TaskManagerListener {

        void onTaskListUpdated();

    }
}

