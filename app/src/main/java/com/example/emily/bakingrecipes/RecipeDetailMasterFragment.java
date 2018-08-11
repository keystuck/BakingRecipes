package com.example.emily.bakingrecipes;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emily.bakingrecipes.RecipeDetails.Ingredient;
import com.example.emily.bakingrecipes.RecipeDetails.Recipe;
import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;
import com.example.emily.bakingrecipes.RecipeDetails.StepAdapter;
import com.example.emily.bakingrecipes.UtilsAndWidget.BakingAppWidgetProvider;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;

import java.util.ArrayList;

public class RecipeDetailMasterFragment extends Fragment {
    private static final String BUNDLED_RECIPE = "bundledRecipe";

    private SharedViewModel model;

    private ArrayList<Ingredient> currentIngredientArrayList = new ArrayList<Ingredient>();
    private ArrayList<RecipeStep> currentRecipeStepArrayList = new ArrayList<RecipeStep>();

    Recipe currentRecipe;
    Context mContext;

    RecyclerView mStepsRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    StepAdapter mAdapter;
    TextView titleTextView;
    TextView ingTextView;

    public interface StepClickListener{
        void onItemClicked(RecipeStep step, View view);
    }

    public RecipeDetailMasterFragment(){}


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String temp = currentRecipe.getName();
        BakingAppWidgetProvider.updateFromRecipe(getActivity(), temp, currentRecipe.getIngredients());
        outState.putParcelable(BUNDLED_RECIPE, currentRecipe);
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            currentRecipe = savedInstanceState.getParcelable(BUNDLED_RECIPE);
        } else {
            Recipe temp = getArguments().getParcelable(RecipeDetailActivity.RECIPE_EXTRA);
            currentRecipe = temp;
        }
        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        View rootView = inflater.inflate(R.layout.recipe_layout, container, false);
        titleTextView = rootView.findViewById(R.id.tv_recipe_title);
        ingTextView = rootView.findViewById(R.id.tv_ingredient_list);
        mStepsRecyclerView = rootView.findViewById(R.id.steps_recycler_view);
        mContext = getActivity();

        populateUI();
        return rootView;
    }

    public void populateUI(){
        titleTextView.setText(currentRecipe.getName());
            currentIngredientArrayList = currentRecipe.getIngredients();
            currentRecipeStepArrayList = currentRecipe.getSteps();

            StringBuilder ingredientListText = new StringBuilder();
            for (Ingredient ingredient : currentIngredientArrayList){
                ingredientListText.append(ingredient.toString());
                ingredientListText.append("\n");
            }
            ingTextView.setText(ingredientListText);

            mAdapter = new StepAdapter(currentRecipeStepArrayList, getActivity(), new StepClickListener() {
                @Override
                public void onItemClicked(RecipeStep step, View view) {
                    model.clickUpon();
                    model.select(step);
                }
            });

            mLayoutManager = new LinearLayoutManager(mContext);
            mStepsRecyclerView.setLayoutManager(mLayoutManager);
            mStepsRecyclerView.setAdapter(mAdapter);
    }

}
