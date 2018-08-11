package com.example.emily.bakingrecipes;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.emily.bakingrecipes.RecipeDetails.Recipe;
import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;

public class RecipeDetailActivity extends AppCompatActivity {

    public final static String RECIPE_EXTRA = "recipe";
    public final static String STEP_EXTRA = "stepExtra";
    public final static String STEP_NUMBER = "stepNumber";
    public final static String MASTER_TAG = "MasterFragment";
    public final static String STEP_TAG = "StepDetails";

    private Recipe currentRecipe;
    private boolean mTwoPane;
    private SharedViewModel model;

    @SuppressWarnings("RedundantIfStatement")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_master_holder);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this).get(SharedViewModel.class);
        model.getmSelected();

        //create fragment with instructions and list of steps
        RecipeDetailMasterFragment recipeDetailMasterFragment = new RecipeDetailMasterFragment();

        if (savedInstanceState == null){
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(RECIPE_EXTRA)) {
                finish();
                Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT).show();
            } else {
                if (findViewById(R.id.recipe_linear_layout) != null) {
                    mTwoPane = true;
                } else {
                    mTwoPane = false;
                }
            }
            //if there's no previous info, get the recipe from the intent
            currentRecipe = intent.getParcelableExtra(RECIPE_EXTRA);
            model.select(currentRecipe.getSteps().get(0));
        } else {
            if (findViewById(R.id.recipe_linear_layout) != null) {
                mTwoPane = true;
            } else {
                mTwoPane = false;
            }
            //if there is info, get it from the bundle
            currentRecipe = savedInstanceState.getParcelable(RECIPE_EXTRA);
            RecipeStep temp = savedInstanceState.getParcelable(STEP_EXTRA);
            model.select(temp);
        }
        //pass the current recipe to the new fragment

        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_EXTRA, currentRecipe);
        recipeDetailMasterFragment.setArguments(bundle);

        //create the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(MASTER_TAG) == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_details_container, recipeDetailMasterFragment, MASTER_TAG)
                    .addToBackStack(null)
                    .commit();
            fragmentManager.executePendingTransactions();
        }



        //if two-pane, also create the step fragment
        if (mTwoPane){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            Bundle stepBundle = new Bundle();

            stepBundle.putBoolean(SeparateStepActivity.IS_DOUBLE_PANE, true);

            if (savedInstanceState == null){
                //if there's no information, set the current state to the first step
                model.select(currentRecipe.getSteps().get(0));
                stepDetailFragment.setArguments(stepBundle);

                if (fragmentManager.findFragmentByTag(STEP_TAG) == null) {
                    fragmentManager.beginTransaction()
                            .add(R.id.step_container, stepDetailFragment, STEP_TAG)
                            .commit();
                    fragmentManager.executePendingTransactions();
                }
            } else {
                RecipeStep currentStep = savedInstanceState.getParcelable(STEP_EXTRA);
                model.select(currentStep);
            }



            //observer on ViewModel to replace fragment when step changes
            final Observer<RecipeStep> stepObserver = new Observer<RecipeStep>() {
                @Override
                public void onChanged(@Nullable RecipeStep recipeStep) {
                    //replace step detail fragment
                    if (model.isClicked()) {
                        StepDetailFragment stepDetailFragment2 = new StepDetailFragment();
                        FragmentManager fragmentManager2 = getSupportFragmentManager();

                        Bundle changeBundle = new Bundle();
                        changeBundle.putBoolean(SeparateStepActivity.IS_DOUBLE_PANE, true);
                        stepDetailFragment2.setArguments(changeBundle);
                        fragmentManager2.beginTransaction()
                                .replace(R.id.step_container, stepDetailFragment2)
                                .commit();
                    }
                }
            };
            //set the observer on the model
            model.getmSelected().observe(this, stepObserver);
        } else {
            //Not a tablet, so create new activity instead of fragment for step detail
            if (savedInstanceState != null){
                RecipeStep inStep = savedInstanceState.getParcelable(STEP_EXTRA);
                model.select(inStep);
            } else {
                model.select(currentRecipe.getSteps().get(0));
            }

            final Context context = this;

            //observer on viewmodel to start new activity when step changes
            final Observer<RecipeStep> stepObserver = new Observer<RecipeStep>() {
                @Override
                public void onChanged(@Nullable RecipeStep recipeStep) {
                    if (model.isClicked()){
                        Intent separateStepIntent = new Intent(context, SeparateStepActivity.class);
                        separateStepIntent.putExtra(RECIPE_EXTRA, currentRecipe);
                        separateStepIntent.putExtra(STEP_NUMBER, model.getSelected().getId());
                        startActivity(separateStepIntent);
                    }
                }
            };

            model.getmSelected().observe(this, stepObserver);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_EXTRA, currentRecipe);
        outState.putParcelable(STEP_EXTRA, model.getSelected());
        model.unClick();
    }
}
