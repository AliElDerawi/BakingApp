package com.nanodegree.bakingapp.ui.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanodegree.bakingapp.R;
import com.nanodegree.bakingapp.model.RecipesResult;
import com.nanodegree.bakingapp.util.GlideApp;
import com.nanodegree.bakingapp.util.OnItemClickListener;
import com.nanodegree.bakingapp.util.RecipeImages;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterRecipeAdapter extends RecyclerView.Adapter<MasterRecipeAdapter.ViewHolder> {

    private ArrayList<RecipesResult> mRecipeNameList;
    private OnItemClickListener onItemClickListener;


    public MasterRecipeAdapter(ArrayList<RecipesResult> mRecipeNameList, OnItemClickListener onItemClickListener) {
        this.mRecipeNameList = mRecipeNameList;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String mRecipeName = mRecipeNameList.get(position).getRecipeName();

        int mRecipeImage = RecipeImages.getImageDrawable(mRecipeName);
//        TODO When using Glide try to use with(this) as possible, if couldn't use (getContext)
        GlideApp.with(holder.mRecipeImageView.getContext()).load(mRecipeNameList.get(position).getRecipeImageUrl()).error(mRecipeImage).into(holder.mRecipeImageView);
        holder.mRecipeNameTextView.setText(mRecipeName);


    }

    @Override
    public int getItemCount() {
        if (mRecipeNameList != null && mRecipeNameList.size() > 0)
            return mRecipeNameList.size();
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_image_view)
        ImageView mRecipeImageView;
        @BindView(R.id.recipe_name_text_view)
        TextView mRecipeNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }
}
