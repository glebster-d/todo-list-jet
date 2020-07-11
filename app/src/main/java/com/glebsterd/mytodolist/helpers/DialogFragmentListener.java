package com.glebsterd.mytodolist.helpers;

import androidx.annotation.NonNull;

/**
 *
 */
public interface DialogFragmentListener {

    void onFinishEditingDialog(@NonNull String dialogName, @NonNull String data);
}
