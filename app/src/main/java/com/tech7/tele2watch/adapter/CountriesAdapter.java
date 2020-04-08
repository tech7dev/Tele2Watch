package com.tech7.tele2watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tech7.tele2watch.R;
import com.tech7.tele2watch.object.Country;

import java.util.ArrayList;
import java.util.List;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Country> countries;
    private List<Country> countriesListFull;

    private RecyclerViewClickListenerCountry listener;

    public CountriesAdapter(Context ctx, List<Country> countries, RecyclerViewClickListenerCountry listener) {
        this.inflater = LayoutInflater.from(ctx);
        this.countries = countries;
        this.listener = listener;
        countriesListFull = new ArrayList<>(countries);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_countries, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCountryCode;
        TextView txtCountry;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView, final RecyclerViewClickListenerCountry listener) {
            super(itemView);
            txtCountryCode = itemView.findViewById(R.id.txtCountryCode);
            txtCountry = itemView.findViewById(R.id.txtCountry);
            parentLayout = itemView.findViewById(R.id.parentLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onCountryRowClicked(getAdapterPosition());
                }
            });

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Country> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(countriesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Country country : countriesListFull) {
                    if (country.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(country);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countries.clear();
            countries.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
