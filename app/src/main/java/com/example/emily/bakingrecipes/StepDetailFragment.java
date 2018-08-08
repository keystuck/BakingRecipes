package com.example.emily.bakingrecipes;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class StepDetailFragment extends Fragment{

    private RecipeStep currentStep;
    private SharedViewModel model;
    private boolean mLandscape;
    private boolean mDoublePane;

    private PlayerView playerView;
    private TextView stepDecriptionTextView;

    SimpleExoPlayer exoPlayer;
    DataSource.Factory dataSourceFactory;

    public StepDetailFragment(){ }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_description, container, false);

        stepDecriptionTextView = rootView.findViewById(R.id.tv_step_full_text);
        playerView = rootView.findViewById(R.id.pv_video_view);


        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        //if this fragment came from a single-pane viewer, get landscape info
        if (getArguments() != null){
            mDoublePane = getArguments().getBoolean(SeparateStepActivity.IS_DOUBLE_PANE);

            if (!mDoublePane) {
                mLandscape = getArguments().getBoolean(SeparateStepActivity.LANDSCAPE);
            }
        }
        currentStep = model.getSelected();


        if (currentStep != null) {
            if (!mDoublePane) {
                if (!mLandscape) {
                    stepDecriptionTextView.setText(currentStep.getDescription());
                    stepDecriptionTextView.setVisibility(View.VISIBLE);
                } else if (!currentStep.getVideoURL().isEmpty()) {
                    stepDecriptionTextView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    layoutParams.width = layoutParams.MATCH_PARENT;
                    layoutParams.height = layoutParams.MATCH_PARENT;
                    playerView.setLayoutParams(layoutParams);

                } else {
                    stepDecriptionTextView.setText(currentStep.getDescription());
                    stepDecriptionTextView.setVisibility(View.VISIBLE);
                    playerView.setVisibility(View.GONE);
                }
            }

            if (exoPlayer != null){
                exoPlayer.release();
            }
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultTrackSelector());
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingRecipes"), bandwidthMeter);

            setRecipeStep(currentStep);
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.unable_retrieve), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public void setRecipeStep(RecipeStep step){
        currentStep = step;

        Uri uri = Uri.parse(currentStep.getVideoURL());
        if (!uri.toString().isEmpty()){
            playerView.setVisibility(View.VISIBLE);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);
            exoPlayer.prepare(videoSource);
            playerView.setPlayer(exoPlayer);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        exoPlayer.release();
    }
}
