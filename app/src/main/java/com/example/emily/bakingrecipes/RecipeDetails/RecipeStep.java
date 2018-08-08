package com.example.emily.bakingrecipes.RecipeDetails;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStep implements Parcelable{
    private int id;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;




    private RecipeStep(){}

    private RecipeStep(Parcel in){
        id = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public String toString() {
        return id + " " + shortDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RecipeStep> CREATOR =
            new Parcelable.Creator<RecipeStep>(){
                @Override
                public RecipeStep createFromParcel(Parcel parcel) {
                    return new RecipeStep(parcel);
                }

                @Override
                public RecipeStep[] newArray(int i) {
                    return new RecipeStep[i];
                }
            };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

}
