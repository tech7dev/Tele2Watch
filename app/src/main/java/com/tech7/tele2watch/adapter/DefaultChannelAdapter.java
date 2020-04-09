package com.tech7.tele2watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tech7.tele2watch.R;
import com.tech7.tele2watch.object.Channel;
import com.tech7.tele2watch.object.Country;

import java.util.ArrayList;
import java.util.List;

public class DefaultChannelAdapter extends RecyclerView.Adapter<DefaultChannelAdapter.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Channel> channels;
    private List<Channel> channelsListFull;

    private RecyclerViewClickListener listener;

    public DefaultChannelAdapter(Context ctx, List<Channel> channels, RecyclerViewClickListener listener){
        this.inflater = LayoutInflater.from(ctx);
        this.channels = channels;
        this.listener = listener;
        channelsListFull = new ArrayList<>(channels);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_default_channel,parent,false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind the data
        holder.Group_title.setText(channels.get(position).getGroup_title());
        Picasso.get().load(channels.get(position).getTvg_logo()).into(holder.Tvg_logo);

    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Channel> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(channelsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Channel channel : channelsListFull) {
                    if (channel.getGroup_title().toLowerCase().contains(filterPattern)) {
                        filteredList.add(channel);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            channels.clear();
            channels.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Group_title;
        ImageView Tvg_logo;
        CardView cardview;

        public ViewHolder(@NonNull View itemView, final RecyclerViewClickListener listener){
            super(itemView);
            Group_title = itemView.findViewById(R.id.txtGroup_title);
            Tvg_logo = itemView.findViewById(R.id.imgTvg_logo);
            cardview = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onRowClicked(getAdapterPosition());
                }
            });

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onViewClicked(v, getAdapterPosition());
                }
            });
        }
    }

    //get position of video in recyclerview
    public Channel getChanneltAt(int position) {
        return channels.get(position);
    }

    public interface RecyclerViewClickListener {

        void onRowClicked(int position);
        void onViewClicked(View v, int position);
    }
}
