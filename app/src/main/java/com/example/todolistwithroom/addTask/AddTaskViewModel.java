package com.example.todolistwithroom.addTask;

import com.example.todolistwithroom.model.Task;
import com.example.todolistwithroom.model.TaskDao;

import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public class AddTaskViewModel {
    private TaskDao taskDao;
    public AddTaskViewModel(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public Single<Long> getOnAddTask(Task task) {
        return taskDao.insert(task);
    }

}
