package com.example.emily.bakingrecipes.RecipeDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emily.bakingrecipes.RecipeDetailMasterFragment;
import com.example.emily.bakingrecipes.UtilsAndWidget.SharedViewModel;
import com.example.emily.bakingrecipes.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{
    private ArrayList<RecipeStep> stepArrayList;

    private Context mContext;
    private final RecipeDetailMasterFragment.StepClickListener listener;

    public StepAdapter(ArrayList<RecipeStep> stepsInput, Context context, RecipeDetailMasterFragment.StepClickListener listener){
        stepArrayList = stepsInput;
        mContext = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        StepViewHolder vh = new StepViewHolder(layout);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        final RecipeStep currentStep = stepArrayList.get(position);
        if (currentStep == null){
            return;
        }
        int step_id = currentStep.getId();
        String step_short = currentStep.getShortDescription();
        String thumbnailLink = currentStep.getThumbnailURL();

        holder.stepNumberTextView.setText("" + step_id);
        holder.shortDescTextView.setText(step_short);
        if (!thumbnailLink.isEmpty()) {
            holder.stepThumbnailImageView.setVisibility(View.VISIBLE);
            Picasso.with(mContext)
                    .load(thumbnailLink)
                    .fit()
                    .centerInside()
                    .placeholder(R.drawable.no_image)
                    .error(R.drawable.no_image)
                    .into(holder.stepThumbnailImageView);
        } else {
            holder.stepThumbnailImageView.setVisibility(View.GONE);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClicked(currentStep, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepArrayList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder{
        TextView stepNumberTextView;
        TextView shortDescTextView;
        LinearLayout linearLayout;
        ImageView stepThumbnailImageView;

        public StepViewHolder(View itemView){
            super(itemView);
            stepNumberTextView = itemView.findViewById(R.id.tv_step_id);
            shortDescTextView = itemView.findViewById(R.id.tv_step_short);
            linearLayout = itemView.findViewById(R.id.step_recycler_layout);
            stepThumbnailImageView = itemView.findViewById(R.id.iv_step_thumbnail);
        }
    }
}
