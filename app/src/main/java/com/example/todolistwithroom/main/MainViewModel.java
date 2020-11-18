package com.example.todolistwithroom.main;

import com.example.todolistwithroom.model.Task;
import com.example.todolistwithroom.model.TaskDao;

import java.util.List;

import io.reactivex.Single;

public class MainViewModel {
    private TaskDao taskDao;

    public MainViewModel(TaskDao taskDao) {
        this.taskDao = taskDao;
    }

    public Single<List<Task>> getTask() {
        return taskDao.getTasks();
    }

    public Single<Integer> deleteTask(Task task) {
        return taskDao.delete(task);
    }

    public Single<Integer> updateTask(Task task) {
        return taskDao.update(task);
    }

    public void deleteAll() {
        taskDao.deleteAll();
    }
    public Single<List<Task>> search(String query){
        return taskDao.search(query);
    }
}
