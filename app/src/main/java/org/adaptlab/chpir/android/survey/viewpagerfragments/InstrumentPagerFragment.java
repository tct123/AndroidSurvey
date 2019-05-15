package org.adaptlab.chpir.android.survey.viewpagerfragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.adaptlab.chpir.android.survey.R;
import org.adaptlab.chpir.android.survey.adapters.InstrumentAdapter;
import org.adaptlab.chpir.android.survey.entities.Instrument;
import org.adaptlab.chpir.android.survey.entities.Settings;
import org.adaptlab.chpir.android.survey.utils.AppUtil;
import org.adaptlab.chpir.android.survey.viewmodelfactories.ProjectInstrumentViewModelFactory;
import org.adaptlab.chpir.android.survey.viewmodels.ProjectInstrumentViewModel;
import org.adaptlab.chpir.android.survey.viewmodels.SettingsViewModel;

import java.util.List;

public class InstrumentPagerFragment extends Fragment {
    private static final String TAG = "InstrumentViewPagerFrag";

    private InstrumentAdapter mInstrumentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInstrumentAdapter = new InstrumentAdapter(this.getContext());

        if (AppUtil.getProjectId() == 0) {
            SettingsViewModel settingsViewModel = ViewModelProviders.of(getActivity()).get(SettingsViewModel.class);
            settingsViewModel.getSettings().observe(this, new Observer<Settings>() {
                @Override
                public void onChanged(@Nullable Settings settings) {
                    if (settings != null && settings.getProjectId() != null)
                        setViewModel(Long.valueOf(settings.getProjectId()));
                }
            });
        } else {
            setViewModel(AppUtil.getProjectId());
        }

    }

    private void setViewModel(long projectId) {
        ProjectInstrumentViewModelFactory factory = new ProjectInstrumentViewModelFactory(getActivity().getApplication(), projectId);
        ProjectInstrumentViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(ProjectInstrumentViewModel.class);
        viewModel.getInstruments().observe(this, new Observer<List<Instrument>>() {
            @Override
            public void onChanged(@Nullable final List<Instrument> instruments) {
                mInstrumentAdapter.setInstruments(instruments);
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_instrument, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.instrumentRecyclerView);
        recyclerView.setAdapter(mInstrumentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.border));
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

}