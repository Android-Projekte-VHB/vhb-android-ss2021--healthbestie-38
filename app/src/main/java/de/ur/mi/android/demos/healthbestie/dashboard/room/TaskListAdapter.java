package de.ur.mi.android.demos.healthbestie.dashboard.room;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import de.ur.mi.android.demos.healthbestie.R;
import de.ur.mi.android.demos.healthbestie.dashboard.tasks.Task;

public class TaskListAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> tasks;

    public TaskListAdapter(@NonNull Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

    private View inflateViewTask(int resource, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resource, parent, false);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = tasks.get(position);
        View viewForTask = convertView;
        if (viewForTask == null) {
            viewForTask = inflateViewTask(R.layout.activity_shopping_list_items, parent);
        }
        if (task != null) {
            if (task.isClosed() && viewForTask.getId() == R.id.list_item_default) {
                viewForTask = inflateViewTask(R.layout.activity_shopping_list_done, parent);
            } else if (!task.isClosed() && viewForTask.getId() == R.id.list_item_done) {
                viewForTask = inflateViewTask(R.layout.activity_shopping_list_items, parent);
            }
            bindTaskToView(task, viewForTask);
        }
        return viewForTask;
    }

    private void bindTaskToView(Task task, View view) {
        TextView taskDescription = view.findViewById(R.id.list_item_description);
        TextView taskCreationDate = view.findViewById(R.id.list_item_creationDate);
        taskDescription.setText(task.description);
        taskCreationDate.setText(getFormattedDateForUI(task.createdAt));
    }

    private String getFormattedDateForUI(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date now = new Date();
        long timeDifferenceInMilliseconds = Math.abs(now.getTime() - date.getTime());
        long timeDifferenceInDays = TimeUnit.DAYS.convert(timeDifferenceInMilliseconds, TimeUnit.MILLISECONDS);
        if (timeDifferenceInDays > 0) {
            sdf = new SimpleDateFormat("dd. MMMM", Locale.getDefault());
        }
        return sdf.format(date);
    }

    @Override
    public int getCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }
}
