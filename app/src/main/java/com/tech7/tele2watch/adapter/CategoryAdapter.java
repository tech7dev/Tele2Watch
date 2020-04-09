package com.tech7.tele2watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.object.Category;
import com.tech7.tele2watch.object.Channel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Category> categories;
    private RecyclerViewClickListenerCategory listener;

    public CategoryAdapter(Context ctx, List<Category> categories, RecyclerViewClickListenerCategory listener){
        this.inflater = LayoutInflater.from(ctx);
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_category,parent,false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind the data
        holder.txtCategory.setText(categories.get(position).getCategoryName());
        Picasso.get().load(categories.get(position).getCategoryImage()).into(holder.imgCategory);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCategory;
        ImageView imgCategory;
        CardView cardview;

        public ViewHolder(@NonNull View itemView, final RecyclerViewClickListenerCategory listener){
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            cardview = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onCategoryRowClicked(getAdapterPosition());
                }
            });

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onCategoryViewClicked(v, getAdapterPosition());
                }
            });
        }
    }

    //get position of category in recyclerview
    public Category getCategoryAt(int position) {
        return categories.get(position);
    }

    public interface RecyclerViewClickListenerCategory {

        void onCategoryRowClicked(int position);
        void onCategoryViewClicked(View v, int position);
    }
}
