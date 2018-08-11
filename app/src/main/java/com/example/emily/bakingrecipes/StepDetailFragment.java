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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;


public class StepDetailFragment extends Fragment{

    private static final String EXOPLAYER_POSITION = "playerPosition";
    private static final String PLAYER_STATE = "playWhenReady";

    private RecipeStep currentStep;
    private SharedViewModel model;
    private boolean mLandscape;
    private boolean mDoublePane;

    private PlayerView playerView;
    private TextView stepDescriptionTextView;
    private ImageView stepThumbnailImageView;

    SimpleExoPlayer exoPlayer;
    DataSource.Factory dataSourceFactory;

    public StepDetailFragment(){ }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_description, container, false);

        stepDescriptionTextView = rootView.findViewById(R.id.tv_step_full_text);
        playerView = rootView.findViewById(R.id.pv_video_view);
        stepThumbnailImageView = rootView.findViewById(R.id.iv_step_pic);
        long playerPosition = C.TIME_UNSET;
        boolean playWhenReady = false;


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
                    stepDescriptionTextView.setText(currentStep.getDescription());
                    stepDescriptionTextView.setVisibility(View.VISIBLE);

                    if (!currentStep.getThumbnailURL().isEmpty()) {
                        stepThumbnailImageView.setVisibility(View.VISIBLE);
                        Picasso.with(getContext())
                                .load(currentStep.getThumbnailURL())
                                .fit()
                                .centerInside()
                                .placeholder(R.drawable.no_image)
                                .error(R.drawable.no_image)
                                .into(stepThumbnailImageView);
                    } else {
                        stepThumbnailImageView.setVisibility(View.GONE);
                    }




                } else if (!currentStep.getVideoURL().isEmpty()) {
                    stepDescriptionTextView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);
                    stepThumbnailImageView.setVisibility(View.GONE);

                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    layoutParams.width = layoutParams.MATCH_PARENT;
                    layoutParams.height = layoutParams.MATCH_PARENT;
                    playerView.setLayoutParams(layoutParams);

                } else {
                    stepDescriptionTextView.setText(currentStep.getDescription());
                    stepDescriptionTextView.setVisibility(View.VISIBLE);
                    if (!currentStep.getThumbnailURL().isEmpty()) {
                        stepThumbnailImageView.setVisibility(View.VISIBLE);
                        Picasso.with(getContext())
                                .load(currentStep.getThumbnailURL())
                                .fit()
                                .centerInside()
                                .placeholder(R.drawable.no_image)
                                .error(R.drawable.no_image)
                                .into(stepThumbnailImageView);
                    } else {
                        stepThumbnailImageView.setVisibility(View.GONE);
                    }
                    playerView.setVisibility(View.GONE);
                }
            } else {
                stepDescriptionTextView.setText(currentStep.getDescription());
                stepDescriptionTextView.setVisibility(View.VISIBLE);
                if (!currentStep.getThumbnailURL().isEmpty()) {
                    stepThumbnailImageView.setVisibility(View.VISIBLE);
                    Picasso.with(getContext())
                            .load(currentStep.getThumbnailURL())
                            .fit()
                            .centerInside()
                            .placeholder(R.drawable.no_image)
                            .error(R.drawable.no_image)
                            .into(stepThumbnailImageView);
                } else {
                    stepThumbnailImageView.setVisibility(View.GONE);
                }
            }

            if (savedInstanceState != null){
                playerPosition = savedInstanceState.getLong(EXOPLAYER_POSITION);
                playWhenReady = savedInstanceState.getBoolean(PLAYER_STATE);


            }
            if (exoPlayer != null){
                exoPlayer.release();
            }
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                    new DefaultTrackSelector());
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingRecipes"), bandwidthMeter);

            setRecipeStep(currentStep, playerPosition, playWhenReady);
        }
        else {
            Toast.makeText(getActivity(), getResources().getString(R.string.unable_retrieve), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    public void setRecipeStep(RecipeStep step, long playerPosition, boolean playWhenReady){
        currentStep = step;


        Uri uri = Uri.parse(currentStep.getVideoURL());
        if (!uri.toString().isEmpty()){
            playerView.setVisibility(View.VISIBLE);

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(uri);

            if (!(playerPosition == C.TIME_UNSET)){
                exoPlayer.seekTo(playerPosition);
            }
            exoPlayer.setPlayWhenReady(playWhenReady);


            if (!(playerPosition == C.TIME_UNSET)){
                exoPlayer.seekTo(playerPosition);
            }
            exoPlayer.prepare(videoSource);
            if (!(playerPosition == C.TIME_UNSET)){
                exoPlayer.seekTo(playerPosition);
            }

            playerView.setPlayer(exoPlayer);
        } else {
            playerView.setVisibility(View.GONE);
        }
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        long playerPosition = exoPlayer.getCurrentPosition();
        outState.putLong(EXOPLAYER_POSITION, playerPosition);

        boolean getPlayerWhenReady = exoPlayer.getPlayWhenReady();
        outState.putBoolean(PLAYER_STATE, getPlayerWhenReady);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            exoPlayer.release();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            exoPlayer.release();
        }
    }
}
