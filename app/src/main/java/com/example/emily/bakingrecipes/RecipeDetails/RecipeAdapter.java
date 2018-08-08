package com.example.emily.bakingrecipes.RecipeDetails;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emily.bakingrecipes.RecipeDetailActivity;
import com.example.emily.bakingrecipes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private Context mContext;
    private ArrayList<Recipe> mRecipeList;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipeArrayList){
        this.mContext = context;
        mRecipeList = recipeArrayList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, final int position) {
        String imagePath = mRecipeList.get(position).getImage();
        if (imagePath == null || imagePath.isEmpty()){
            holder.pic.setVisibility(View.GONE);
        } else {
            Picasso.with(mContext)
                    .load(imagePath)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.pic);
        }

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.RECIPE_EXTRA, mRecipeList.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.title.setText(mRecipeList.get(position).getName());
    }

    public void clear(){ mRecipeList.clear(); }
    public void addAll(List<Recipe> recipes){ mRecipeList.addAll(recipes); }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView title;
        ConstraintLayout layout;
        public RecipeViewHolder(View itemView){
            super(itemView);
            pic = itemView.findViewById(R.id.iv_recipe_img);
            title = itemView.findViewById(R.id.tv_recipe_name);
            layout = itemView.findViewById(R.id.recipe_list_item);
        }
    }

}
