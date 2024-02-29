package com.example.trabajocm.ui.borrado;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BorradoViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BorradoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is borrado fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

}
