package com.tech7.livetv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tech7.livetv.R;
import com.tech7.livetv.object.Channel;
import com.tech7.livetv.object.Country;

import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Country> countries;
    private RecyclerViewClickListenerCountry listener;

    public CountriesAdapter(Context ctx, List<Country> countries, RecyclerViewClickListenerCountry listener){
        this.inflater = LayoutInflater.from(ctx);
        this.countries = countries;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_countries,parent,false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind the data
        holder.txtCountryCode.setText(countries.get(position).getCountryCode());
        holder.txtCountry.setText(countries.get(position).getCountry());

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtCountryCode;
        TextView txtCountry;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView, final RecyclerViewClickListenerCountry listener){
            super(itemView);
            txtCountryCode = itemView.findViewById(R.id.txtCountryCode);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            parentLayout = itemView.findViewById(R.id.parentLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onCountryRowClicked(getAdapterPosition());
                }
            });

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onCountryViewClicked(v, getAdapterPosition());
                }
            });
        }
    }

    //get position of video in recyclerview
    public Country getCountryAt(int position) {
        return countries.get(position);
    }

    public interface RecyclerViewClickListenerCountry {

        void onCountryRowClicked(int position);
        void onCountryViewClicked(View v, int position);
    }
}
