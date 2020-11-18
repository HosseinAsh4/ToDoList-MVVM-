package com.example.todolistwithroom.updateTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.todolistwithroom.R;
import com.example.todolistwithroom.model.AppDatabase;
import com.example.todolistwithroom.model.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class UpdateTaskDialog extends DialogFragment {
    private OnUpdateItem onUpdateItem;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onUpdateItem = (OnUpdateItem) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_task, null, false);
        final TextInputLayout etl_dialog_title = view.findViewById(R.id.etl_dialog_title);
        final TextInputEditText et_dialog_title = view.findViewById(R.id.et_dialog_title);
        View btn_dialog_save = view.findViewById(R.id.btn_dialog_save);
        btn_dialog_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_dialog_title.length() > 0) {
                    onUpdateItem.updateTask(String.valueOf(et_dialog_title.getText()));
                    dismiss();
                } else {
                    etl_dialog_title.setError("عنوان نباید خالی باشد");
                }
            }
        });
        builder.setView(view);
        return builder.create();
    }

    public interface OnUpdateItem {
        void updateTask(String title);
    }
}
