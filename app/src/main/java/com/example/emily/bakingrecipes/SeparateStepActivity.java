package com.example.emily.bakingrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.emily.bakingrecipes.RecipeDetails.Recipe;
import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class SeparateStepActivity extends AppCompatActivity {

    private SharedViewModel model;
    private Recipe currentRecipe;
    private int current_id;
    private RecipeStep currentStep;
    public final static String BUNDLE_RECIPE = "bundleRecipe";
    public final static String BUNDLE_STEP = "bundleStep";
    public final static String LANDSCAPE = "isLandscape";
    public final static String IS_DOUBLE_PANE = "isDoublePane";
    public final static String STEP_TAG = "stepDetail";
    ImageButton prevButton;
    ImageButton nextButton;
    private boolean mLandscape;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_step_holder);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        model = ViewModelProviders.of(this).get(SharedViewModel.class);

        mLandscape = (findViewById(R.id.single_step_landscape) != null);

        if (savedInstanceState == null){
            Intent incomingIntent = getIntent();
            if (incomingIntent == null || !incomingIntent.hasExtra(RecipeDetailActivity.RECIPE_EXTRA)){
                finish();
            }

            model.getmSelected();

            currentRecipe = incomingIntent.getParcelableExtra(RecipeDetailActivity.RECIPE_EXTRA);

            current_id = incomingIntent.getIntExtra(RecipeDetailActivity.STEP_NUMBER, 0);
            currentStep = currentRecipe.findStepById(current_id);



        } else {
            currentRecipe = savedInstanceState.getParcelable(BUNDLE_RECIPE);
            currentStep = model.getSelected();
            current_id = currentStep.getId();
        }

        model.select(currentStep);

        if (!mLandscape) {

            nextButton = findViewById(R.id.btn_next_step);
            prevButton = findViewById(R.id.btn_prev_step);

            if (current_id == 0){
                prevButton.setVisibility(View.INVISIBLE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
            }
            if (current_id == (currentRecipe.getSteps().size() - 1)){
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }
        }

        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_STEP, currentStep);
        bundle.putBoolean(LANDSCAPE, mLandscape);
        bundle.putBoolean(IS_DOUBLE_PANE, false);
        stepDetailFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager.findFragmentByTag(STEP_TAG) == null) {
                fragmentManager.beginTransaction()
                        .add(R.id.step_container, stepDetailFragment, STEP_TAG)
                        .commit();

                fragmentManager.executePendingTransactions();
            }


        //observer triggered when step changes
        final Observer<RecipeStep> stepObserver = new Observer<RecipeStep>() {
            @Override
            public void onChanged(@Nullable RecipeStep recipeStep) {
                if (model.isClicked()) {
                    StepDetailFragment stepDetailFragment2 = new StepDetailFragment();

                    Bundle inBundle = new Bundle();
                    inBundle.putParcelable(BUNDLE_STEP, recipeStep);
                    inBundle.putBoolean(LANDSCAPE, mLandscape);
                    inBundle.putBoolean(IS_DOUBLE_PANE, false);

                    stepDetailFragment2.setArguments(inBundle);

                    FragmentManager fragmentManager2 = getSupportFragmentManager();

                    fragmentManager2.beginTransaction()
                            .replace(R.id.step_container, stepDetailFragment2)
                            .addToBackStack(null)
                            .commit();
                } else { model.clickUpon(); }
            }
        };

        //set the observer
        model.getmSelected().observe(this, stepObserver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECIPE, currentRecipe);
        model.unClick();
    }



    public void prevButtonClicked(View view){
        current_id = currentRecipe.getPreviousId(model.getSelected().getId());
        if (current_id == -1){
            Toast.makeText(this, "error retrieving step", Toast.LENGTH_SHORT).show();
        } else {
            currentStep = currentRecipe.findStepById(current_id);

            if (currentRecipe.isFirstId(current_id)) {
                prevButton.setVisibility(View.INVISIBLE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
            }
            if (currentRecipe.isLastId(current_id)){
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }

            model.select(currentStep);
        }
    }

    public void nextButtonClicked(View view){
        current_id = currentRecipe.getNextId(model.getSelected().getId());
        if (current_id == -1){
            Toast.makeText(this, "error retrieving step", Toast.LENGTH_SHORT).show();
        } else {
            currentStep = currentRecipe.findStepById(current_id);

            if (currentRecipe.isFirstId(current_id)) {
                prevButton.setVisibility(View.INVISIBLE);
            } else {
                prevButton.setVisibility(View.VISIBLE);
            }
            if (currentRecipe.isLastId(current_id)) {
                nextButton.setVisibility(View.INVISIBLE);
            } else {
                nextButton.setVisibility(View.VISIBLE);
            }

            model.select(currentStep);
        }
    }

}
