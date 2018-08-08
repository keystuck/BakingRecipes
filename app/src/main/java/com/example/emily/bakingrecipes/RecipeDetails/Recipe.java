package com.example.emily.bakingrecipes.RecipeDetails;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<RecipeStep> steps;
    private int servings;
    private String image;

    private Recipe(){}

    private Recipe(Parcel in){
        ingredients = new ArrayList<Ingredient>();
        steps = new ArrayList<RecipeStep>();

        id = in.readInt();
        name = in.readString();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        in.readTypedList(steps, RecipeStep.CREATOR);
        servings = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Recipe> CREATOR =
            new Parcelable.Creator<Recipe>(){
                @Override
                public Recipe createFromParcel(Parcel parcel) {
                    return new Recipe(parcel);
                }

                @Override
                public Recipe[] newArray(int i) {
                    return new Recipe[i];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public String getImage(){
        return image;
    }

    public String getName(){
        return name;
    }

    public ArrayList<Ingredient> getIngredients(){ return ingredients; }
    public ArrayList<RecipeStep> getSteps(){ return steps; }

    public RecipeStep findStepById(int id) {
        int i = 0;
        while (i < steps.size() && (steps.get(i).getId() != id)) {
            i++;
        }
        if (i == steps.size()) {
            return null;
        } else {
            return steps.get(i);
        }
    }

    public int getPreviousId(int id){
        int x = id - 1;
        while (x >= 0 && (findStepById(x) == null)){
            x--;
        }
        if (x < 0){
            return -1;
        } else {
            return x;
        }
    }

    public int getNextId(int id){
        int x = id + 1;
        while (x < steps.size() && (findStepById(x) == null)){
            x++;
        }
        if (x == steps.size()){
            return -1;
        } else {
            return x;
        }
    }

    public boolean isLastId(int id){
        return findStepById(id) == steps.get(steps.size() - 1);
    }

    public boolean isFirstId(int id){
        return findStepById(id) == steps.get(0);
    }
}
