package de.ur.mi.android.demos.healthbestie.dashboard.shopping_list_function;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import de.ur.mi.android.demos.healthbestie.R;

import de.ur.mi.android.demos.healthbestie.dashboard.room.TaskListAdapter;
import de.ur.mi.android.demos.healthbestie.dashboard.tasks.Task;
import de.ur.mi.android.demos.healthbestie.dashboard.tasks.TaskManager;

public class ShoppingList extends AppCompatActivity implements TaskManager.TaskManagerListener {

    private TaskManager taskManager;
    private TaskListAdapter taskListAdapter;
    private EditText taskDescriptionInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTaskManager();
        initUI();
        taskManager.requestUpdate();


    }

    private void initTaskManager() {
        taskManager = new TaskManager(getApplicationContext(), this);
    }

    private void initUI() {
        setContentView(R.layout.activity_shopping_list);
        initListView();
        initInputElements();
    }

    private void initListView() {
        taskListAdapter = new TaskListAdapter(this);
        ListView taskList = findViewById(R.id.task_list);
        taskList.setAdapter(taskListAdapter);
        taskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                taskManager.toggleTaskStateForId(taskManager.getCurrentTasks().get(position).id.toString());
                return true;
            }
        });
    }

    private void initInputElements() {
        taskDescriptionInput = findViewById(R.id.input_text);
        Button inputButton = findViewById(R.id.input_button);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentInput = taskDescriptionInput.getText().toString();
                onUserInputClicked(currentInput);
            }
        });
    }

    private void onUserInputClicked(String input) {
        if (input.length() > 0) {
            taskManager.addTask(input);
            taskDescriptionInput.setText("");
            taskDescriptionInput.requestFocus();
        }
    }

    @Override
    public void onTaskListUpdated() {
        
        ArrayList<Task> currentTasks = taskManager.getCurrentTasks();
        taskListAdapter.setTasks(currentTasks);
    }



}