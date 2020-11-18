package com.example.todolistwithroom.addTask;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.todolistwithroom.R;
import com.example.todolistwithroom.model.AppDatabase;
import com.example.todolistwithroom.model.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddTaskDialog extends DialogFragment {
    private OnSaveTaskCallBack onSaveTask;
    private AddTaskViewModel viewModel;
    private Disposable disposable;
    private static final String TAG = "AddTaskDialog";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onSaveTask = (OnSaveTaskCallBack) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_task, null, false);
        final TextInputEditText titleEt = view.findViewById(R.id.et_dialog_title);
        final TextInputLayout inputLayout = view.findViewById(R.id.etl_dialog_title);
        View saveBtn = view.findViewById(R.id.btn_dialog_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEt.length() == 0) {
                    inputLayout.setError("عنوان نباید خالی باشد");
                } else {
                    viewModel=new AddTaskViewModel(AppDatabase.getAppDatabase(getContext()).getTaskDao());
                    final Task task = new Task();
                    task.setTitle(String.valueOf(titleEt.getText()));
                    task.setCompleted(false);
                    viewModel.getOnAddTask(task).subscribeOn(AndroidSchedulers.mainThread())
                            .subscribe(new SingleObserver<Long>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    disposable = d;
                                }

                                @Override
                                public void onSuccess(Long aLong) {
                                    task.setId(aLong);
                                    onSaveTask.onSaveTask(task);
                                    dismiss();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Log.e(TAG, "onError: "+e.toString());
                                }
                            });
                }

            }
        });
        builder.setView(view);
        return builder.create();
    }

    public interface OnSaveTaskCallBack {
        void onSaveTask(Task task);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }
}
