package de.ur.mi.android.demos.healthbestie.dashboard.tasks;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.UUID;


@Entity(tableName = "task_table")
public class Task implements Comparable<Task> {
    // id eines Tasks ist einzigartig und kann damit als Primärschlüssel verwendet werden
    @PrimaryKey
    // Primärschlüssel dürfen nicht null sein!
    @NonNull
    public final UUID id;
    public final String description;
    @ColumnInfo(name = "created_at")
    public final Date createdAt;
    @ColumnInfo(name = "current_state")
    private TaskState currentState;

    @Ignore
    public Task(String description) {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
        this.currentState = TaskState.OPEN;
        this.description = description;
    }

    public Task(String description, UUID id, Date createdAt, TaskState currentState) {
        this.id = id;
        this.createdAt = createdAt;
        this.currentState = currentState;
        this.description = description;
    }

    public TaskState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(TaskState currentState) {
        this.currentState = currentState;
    }

    public boolean isClosed() {
        return currentState == TaskState.CLOSED;
    }

    public void markAsOpen() {
        currentState = TaskState.OPEN;
    }

    public void markAsClosed() {
        currentState = TaskState.CLOSED;
    }

    public Task copy() {
        Date creationDateFromOriginal = getCreationDateCopy();
        return new Task(description, id, creationDateFromOriginal, currentState);
    }

    private Date getCreationDateCopy() {
        return new Date(createdAt.getTime());
    }

    @Override
    public int compareTo(Task otherTask) {
        if (this.isClosed() && !otherTask.isClosed()) {
            return 1;
        }
        if (!this.isClosed() && otherTask.isClosed()) {
            return -1;
        }
        return -this.createdAt.compareTo(otherTask.createdAt);
    }

    public enum TaskState {
        OPEN,
        CLOSED
    }

}

