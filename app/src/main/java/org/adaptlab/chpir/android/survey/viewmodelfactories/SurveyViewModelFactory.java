package org.adaptlab.chpir.android.survey.viewmodelfactories;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import org.adaptlab.chpir.android.survey.viewmodels.SurveyViewModel;

public class SurveyViewModelFactory implements ViewModelProvider.Factory {
    private String mUUID;
    private Application mApplication;

    public SurveyViewModelFactory(@NonNull Application application, String uuid) {
        this.mApplication = application;
        this.mUUID = uuid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SurveyViewModel.class)) {
            return (T) new SurveyViewModel(mApplication, mUUID);
        } else {
            return null;
        }
    }

}
