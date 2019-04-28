package com.example.hw9_productsearch;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarViewAdapter extends RecyclerView.Adapter<SimilarViewAdapter.mySimHolder>
{
    @NonNull
    @Override
    public SimilarViewAdapter.mySimHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater mInflater = LayoutInflater.from(sContext);
        View recycle_view = mInflater.inflate(R.layout.similar_card_view, viewGroup, false);
        return new SimilarViewAdapter.mySimHolder(recycle_view);
    }

    @Override
    public void onBindViewHolder(@NonNull mySimHolder mySimHolder, final int i)
    {
        mySimHolder.setIsRecyclable(false);

        ImageView temp = SimilarViewAdapter.mySimHolder.sim_img;
        String temp_img_url = sData.get(i).getImageurls();

        SimilarViewAdapter.mySimHolder.sim_title.setText(sData.get(i).getTitle());
        int temp1 = sData.get(i).getTimeLeft();
        String conv;

        if (temp1 == 0 || temp1 == 1)
            conv = temp1+" Day Left";
        else
            conv = temp1+" Days Left";

        SimilarViewAdapter.mySimHolder.sim_days.setText(conv);

        conv= "$"+sData.get(i).getPrice();
        SimilarViewAdapter.mySimHolder.sim_price.setText(conv);

        conv = sData.get(i).getShipping_cost();

        SimilarViewAdapter.mySimHolder.sim_ship.setText(conv);

        Picasso.get().load(temp_img_url).into(temp);
    }


    @Override
    public int getItemCount() {
        return sData.size();
    }

    private final Context sContext;
    private static List<SimilarItem> sData;

    SimilarViewAdapter(Context sContext, List<SimilarItem> sData)
    {
        this.sContext = sContext;
        SimilarViewAdapter.sData = sData;
    }

    public static class mySimHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private static ImageView sim_img;
        private static TextView sim_title;
        private static TextView sim_days;
        private static TextView sim_ship;
        private static TextView sim_price;
        final Context inContext;

        mySimHolder(View itemView)
        {
            super(itemView);
            sim_title = itemView.findViewById(R.id.similar_card_title);
            sim_ship = itemView.findViewById(R.id.similar_card_ship);
            sim_price = itemView.findViewById(R.id.similar_card_price);
            sim_days = itemView.findViewById(R.id.similar_card_days);
            sim_img = itemView.findViewById(R.id.similar_card_img);
            inContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            String url = sData.get(getAdapterPosition()).getItem_urls();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            inContext.startActivity(i);
        }
    }
}
