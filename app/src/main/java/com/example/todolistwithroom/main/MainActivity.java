package com.example.todolistwithroom.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todolistwithroom.addTask.AddTaskDialog;
import com.example.todolistwithroom.R;
import com.example.todolistwithroom.model.AppDatabase;
import com.example.todolistwithroom.model.Task;
import com.example.todolistwithroom.updateTask.UpdateTaskDialog;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements AddTaskDialog.OnSaveTaskCallBack, TaskAdapter.OnItemCallBack,
        UpdateTaskDialog.OnUpdateItem {
    private RecyclerView rv_main_tasks;
    private TaskAdapter taskAdapter;
    private MainViewModel viewModel;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String TAG = "MainActivity";
    private Task taskUpdated;
    private ImageView img_delete_all;
    private EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new MainViewModel(AppDatabase.getAppDatabase(this).getTaskDao());
        initRecyclerView();
        initViews();
    }

    private void initViews() {
        rv_main_tasks = findViewById(R.id.rv_main_tasks);
        img_delete_all = findViewById(R.id.img_delete_all);
        edt_search = findViewById(R.id.edt_search);
        View fab_main_addNewTask = findViewById(R.id.fab_main_addNewTask);
        fab_main_addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskDialog addTaskDialog = new AddTaskDialog();
                addTaskDialog.show(getSupportFragmentManager(), null);
            }
        });
        img_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.deleteAll();
                taskAdapter.deleteAll();
            }
        });
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                viewModel.search(s.toString()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<List<Task>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                            }

                            @Override
                            public void onSuccess(List<Task> list) {
                                taskAdapter.searchAll(list);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "onError: " + e.toString());
                            }
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initRecyclerView() {
        viewModel.getTask().subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Task> tasks) {
                        taskAdapter = new TaskAdapter(tasks, MainActivity.this);
                        rv_main_tasks.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                        rv_main_tasks.setAdapter(taskAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeDisposable != null)
            compositeDisposable.clear();
    }

    @Override
    public void onSaveTask(Task task) {
        long result = task.getId();

        if (result > 0) {
            taskAdapter.insert(task);
            //AddTaskViewModel addTaskViewModel=new AddTaskViewModel(AppDatabase.getAppDatabase(MainActivity.this).getTaskDao());
            //addTaskViewModel.getOnAddTask(task).subscribeOn(AndroidSchedulers.mainThread())
            //      .subscribe();
        }
    }

    @Override
    public void onItemUpdateLongPressed(final Task task) {
        Log.i(TAG, "onItemCheckedChanged: " + task);
        UpdateTaskDialog updateTaskDialog = new UpdateTaskDialog();
        updateTaskDialog.show(getSupportFragmentManager(), null);
        taskUpdated = task;
    }

    @Override
    public void onItemDelete(final Task task) {
        viewModel.deleteTask(task).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.i(TAG, "onSuccess: " + integer);
                        taskAdapter.deleteTask(task);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }
                });
    }

    @Override
    public void onItemCheckedChanged(final Task task) {
        viewModel.updateTask(task).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        taskAdapter.updateIsChecked(task);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.toString());
                    }
                });
    }

    @Override
    public void updateTask(final String title) {
        taskUpdated.setTitle(title);
        viewModel.updateTask(taskUpdated).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        taskAdapter.update(taskUpdated);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.toString());
                    }
                });
    }
}