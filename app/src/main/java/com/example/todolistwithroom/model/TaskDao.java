package com.example.todolistwithroom.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.google.android.material.circularreveal.CircularRevealHelper;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.subjects.ReplaySubject;

@Dao
public interface TaskDao {

    @Insert
    Single<Long> insert(Task task);

    @Delete
    Single<Integer> delete(Task task);

    @Update
    Single<Integer> update(Task task);

    @Query("SELECT * FROM tbl_tasks")
    Single<List<Task>> getTasks();

    @Query("SELECT * FROM TBL_TASKS WHERE title LIKE '%'|| :query || '%'")
    Single<List<Task>> search(String query);

    @Query("DELETE FROM tbl_tasks")
    void deleteAll();

}
