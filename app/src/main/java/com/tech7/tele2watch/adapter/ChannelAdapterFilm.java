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
import com.tech7.tele2watch.object.Channel;

import java.util.List;

public class ChannelAdapterFilm extends RecyclerView.Adapter<ChannelAdapterFilm.ViewHolder> {
    LayoutInflater inflater;
    List<Channel> channels;
    private RecyclerViewClickListenerFilm listener;

    public ChannelAdapterFilm(Context ctx, List<Channel> channels, RecyclerViewClickListenerFilm listener){
        this.inflater = LayoutInflater.from(ctx);
        this.channels = channels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_channel,parent,false);
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView Group_title;
        ImageView Tvg_logo;
        CardView cardview;

        public ViewHolder(@NonNull View itemView, final RecyclerViewClickListenerFilm listener){
            super(itemView);
            Group_title = itemView.findViewById(R.id.txtGroup_title);
            Tvg_logo = itemView.findViewById(R.id.imgTvg_logo);
            cardview = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onFilmRowClicked(getAdapterPosition());
                }
            });

            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onFilmViewClicked(v, getAdapterPosition());
                }
            });
        }
    }

    //get position of video in recyclerview
    public Channel getChanneltAt(int position) {
        return channels.get(position);
    }

    public interface RecyclerViewClickListenerFilm {

        void onFilmRowClicked(int position);
        void onFilmViewClicked(View v, int position);
    }
}
