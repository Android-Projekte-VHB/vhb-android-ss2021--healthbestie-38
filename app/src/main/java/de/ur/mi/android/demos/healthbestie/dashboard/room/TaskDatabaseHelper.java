package de.ur.mi.android.demos.healthbestie.dashboard.room;
import android.content.Context;
import androidx.room.Room;
import java.util.ArrayList;
import de.ur.mi.android.demos.healthbestie.dashboard.tasks.Task;

public class TaskDatabaseHelper {

    private static final String DATABASE_NAME = "tasks-db";
    private final Context context;
    private TaskDatabase db;

    public TaskDatabaseHelper(Context context) {
        this.context = context;
        initDatabase();
    }

    private void initDatabase() {
        db = Room.databaseBuilder(context, TaskDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public void addTask(Task task) {
        db.taskDao().insertTask(task);
    }

    public void updateTask(Task task) {
        db.taskDao().updateTask(task);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<Task>(db.taskDao().getAllTasks());
    }

}
