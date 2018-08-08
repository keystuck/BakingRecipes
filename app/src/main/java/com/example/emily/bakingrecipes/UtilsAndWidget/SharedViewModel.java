package com.example.emily.bakingrecipes.UtilsAndWidget;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.emily.bakingrecipes.RecipeDetails.Recipe;
import com.example.emily.bakingrecipes.RecipeDetails.RecipeStep;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<RecipeStep> mSelected = null;
    boolean clicked;

    public MutableLiveData<RecipeStep> getmSelected() {
        if (mSelected == null){
            mSelected = new MutableLiveData<RecipeStep>();
            clicked = false;
        }
        return mSelected;
    }

    public void select(RecipeStep step){
        mSelected.setValue(step);
    }

    public void clickUpon(){ clicked = true; }

    public boolean isClicked(){ return clicked; }

    public RecipeStep getSelected(){
        return mSelected.getValue();
    }
}
