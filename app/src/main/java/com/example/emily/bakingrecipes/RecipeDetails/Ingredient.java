package com.example.emily.bakingrecipes.RecipeDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable{

    private double quantity;
    private String measure;
    private String ingredient;

    private Ingredient(){}

    private Ingredient(Parcel in){
        quantity = in.readDouble();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public String toString() {
        return quantity + " " + measure + " " + ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static final Parcelable.Creator<Ingredient> CREATOR =
            new Parcelable.Creator<Ingredient>(){
                @Override
                public Ingredient createFromParcel(Parcel parcel) {
                    return new Ingredient(parcel);
                }

                @Override
                public Ingredient[] newArray(int i) {
                    return new Ingredient[i];
                }
            };
}
