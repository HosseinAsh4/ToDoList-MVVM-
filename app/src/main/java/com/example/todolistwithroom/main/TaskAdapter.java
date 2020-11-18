package com.example.todolistwithroom.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistwithroom.R;
import com.example.todolistwithroom.model.Task;

import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Task> tasks;
    private OnItemCallBack onItemCallBack;
    private static final String TAG = "TaskAdapter";

    public TaskAdapter(List<Task> tasks, OnItemCallBack onItemCallBack) {
        this.tasks = tasks;
        this.onItemCallBack = onItemCallBack;
    }

    public void deleteTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.remove(i);
                //tasks.remove(task);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void insert(Task task) {
        tasks.add(0, task);
        notifyItemInserted(0);
    }

    public void update(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.get(i).setTitle(task.getTitle());
                notifyItemChanged(i);
                break;
            }
        }
    }
    public void updateIsChecked(Task task){
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTitle() == task.getTitle()) {
                tasks.get(i).setCompleted(task.isCompleted());
                notifyItemChanged(i);
                break;
            }
        }
     }
    public void deleteAll() {
        tasks.clear();
        notifyDataSetChanged();
    }

    public void searchAll(List<Task> list) {
        tasks.clear();
        tasks.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox_task;
        private View btn_task_delete;
        private ConstraintLayout constraint_task_view;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            checkBox_task = itemView.findViewById(R.id.checkBox_task);
            btn_task_delete = itemView.findViewById(R.id.btn_task_delete);
            constraint_task_view = itemView.findViewById(R.id.constraint_task_view);
        }

        public void bind(final Task task) {
            checkBox_task.setOnCheckedChangeListener(null);
            checkBox_task.setText(task.getTitle());
            checkBox_task.setChecked(task.isCompleted());
            btn_task_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemCallBack.onItemDelete(task);
                }
            });
            checkBox_task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setCompleted(isChecked);
                    onItemCallBack.onItemCheckedChanged(task);
                }
            });
            constraint_task_view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemCallBack.onItemUpdateLongPressed(task);
                    Log.i(TAG, "onLongClick: ");
                    return false;
                }
            });
        }

    }

    public interface OnItemCallBack {
        void onItemUpdateLongPressed(Task task);

        void onItemDelete(Task task);

        void onItemCheckedChanged(Task task);
    }
}
