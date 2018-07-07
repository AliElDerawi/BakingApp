package com.nanodegree.bakingapp.ui.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.request.RecipeStepsRequest;
import com.nanodegree.bakingapp.util.OnItemClickListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.viewHolder> {

    private ArrayList<RecipeStepsRequest> mRecipeStepsArrayList;
    private OnItemClickListener onItemClickListener;

    public RecipeStepsAdapter(ArrayList<RecipeStepsRequest> mRecipeStepsArrayList, OnItemClickListener onItemClickListener){
        this.mRecipeStepsArrayList = mRecipeStepsArrayList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_steps,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.mRecipeStepNumber.setText(String.valueOf(mRecipeStepsArrayList.get(position).getStepId()));
        holder.mRecipeStepShortDescription.setText(mRecipeStepsArrayList.get(position).getStepShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mRecipeStepsArrayList != null && mRecipeStepsArrayList.size() >0) return mRecipeStepsArrayList.size();
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.recipe_step_number_text_view)
        TextView mRecipeStepNumber;
        @BindView(R.id.recipe_step_short_description_text_view)
        TextView mRecipeStepShortDescription;
        @BindView(R.id.my_recipe_step_layout)
        CardView mRecipeLayout;

        public viewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mRecipeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
