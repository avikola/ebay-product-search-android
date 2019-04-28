package com.example.hw9_productsearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>
{
    private final Context mContext;
    private static List<Product> mData;
    private Activity act;

    RecyclerViewAdapter(Context mContext, List<Product> mData) {
        this.mContext = mContext;
        RecyclerViewAdapter.mData = mData;
    }

    RecyclerViewAdapter(Activity act, Context mContext, List<Product> mData) {
        this.act = act;
        this.mContext = mContext;
        RecyclerViewAdapter.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View recycle_view = mInflater.inflate(R.layout.cardview_item, viewGroup, false);
        return new MyViewHolder(recycle_view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {

        myViewHolder.setIsRecyclable(false);

        ImageView temp = MyViewHolder.product_card_img;
        String image_url = mData.get(i).getProductImageURL();

        Picasso.get().load(image_url).into(temp);

        MyViewHolder.product_card_id.setText(mData.get(i).getProductID());
        MyViewHolder.product_card_title.setText(mData.get(i).getProductTitle());
        MyViewHolder.product_card_zip.setText(mData.get(i).getProductZip());
        MyViewHolder.product_card_shipping.setText(mData.get(i).getProductShipping());
        MyViewHolder.product_card_condition.setText(mData.get(i).getProductCondition());
        MyViewHolder.product_card_price.setText(mData.get(i).getProductPrice());

        SharedPreferences pref_wishlist = mContext.getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);

        String current_id = mData.get(i).getProductID();
        String wishlist_ids = pref_wishlist.getString("WISHLIST", null);
        final ImageView temp_icon = MyViewHolder.product_card_wishlist_icon;

        if(wishlist_ids != null)
        {
            List<String> wishlist_list = new ArrayList<>(Arrays.asList(wishlist_ids.split(","))) ;

            if(wishlist_list.contains(current_id))
                Picasso.get().load(R.drawable.cart_off).into(temp_icon);
            else
                Picasso.get().load(R.drawable.cart_plus).into(temp_icon);
        }
        else
            Picasso.get().load(R.drawable.cart_plus).into(temp_icon);

        temp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String current_id = mData.get(i).getProductID();
                final String current_title = mData.get(i).getProductTitle();
                SharedPreferences pref_wishlist = mContext.getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref_wishlist.edit();
                final String wishlist_ids = pref_wishlist.getString("WISHLIST", null);

                List<String> wishlist_list;

                if(wishlist_ids != null)
                {
                    wishlist_list = new ArrayList<>(Arrays.asList(wishlist_ids.split(","))) ;

                    if(wishlist_list.contains(current_id))
                    {
                        wishlist_list.remove(current_id);
                        Picasso.get().load(R.drawable.cart_plus).into(temp_icon);
                        Toast.makeText(mContext,current_title+" was removed from wish list",Toast.LENGTH_SHORT).show();
                        for(int i = 0 ; i < mData.size() ; i++){
                            if(current_id.equalsIgnoreCase(mData.get(i).getProductID())){
                                if(act != null)
                                {
                                    TextView t1 = act.findViewById(R.id.total_items_text);
                                    t1.setText("Wishlist Total ("+(mData.size()-1)+" items):");
                                    t1 = act.findViewById(R.id.total_price_text);

                                    String tp = t1.getText().toString().replace("$","");

                                    double tots = Double.parseDouble(tp);
                                    double sub = Double.parseDouble(mData.get(i).getProductPrice().replace("$",""));
                                    tots = tots-sub;

                                    t1.setText("$"+String.format(Locale.US,"%.2f", tots));

                                    //noinspection SuspiciousListRemoveInLoop
                                    mData.remove(i);
                                    if(mData.size() == 0)
                                        act.findViewById(R.id.no_wishes).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        notifyDataSetChanged();
                    }
                    else
                    {
                        wishlist_list.add(current_id);
                        Picasso.get().load(R.drawable.cart_off).into(temp_icon);
                        Toast.makeText(mContext,current_title+" was added to wish list",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    wishlist_list = new ArrayList<>();
                    wishlist_list.add(current_id);
                    Picasso.get().load(R.drawable.cart_off).into(temp_icon);
                    Toast.makeText(mContext,current_title+" was added to wish list",Toast.LENGTH_SHORT).show();
                }

                String list_to_string = TextUtils.join(",",wishlist_list);
                editor.putString("WISHLIST", list_to_string);
                editor.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener    // on click
    {
        private static TextView product_card_id;
        private static ImageView product_card_img;
        private static TextView product_card_title;
        private static TextView product_card_zip;
        private static TextView product_card_shipping;
        private static TextView product_card_condition;
        private static ImageView product_card_wishlist_icon;
        private static TextView product_card_price;
        final Context innerContext;

        MyViewHolder(View itemView)
        {
            super(itemView);

            product_card_id = itemView.findViewById(R.id.results_product_id);
            product_card_img = itemView.findViewById(R.id.results_product_img);
            product_card_title = itemView.findViewById(R.id.results_product_title);
            product_card_zip = itemView.findViewById(R.id.results_product_zip);
            product_card_shipping = itemView.findViewById(R.id.results_product_shipping);
            product_card_condition = itemView.findViewById(R.id.results_product_condition);
            product_card_wishlist_icon = itemView.findViewById(R.id.results_product_wishlist_icon);
            product_card_price = itemView.findViewById(R.id.results_product_price);

            innerContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }

        public void onClick(View view)
        {
            Intent intent = new Intent(innerContext, productDetails.class);
            intent.putExtra("ItemID", mData.get(getAdapterPosition()).getProductID());
            intent.putExtra("ItemTitle", mData.get(getAdapterPosition()).getProductTitle());
            intent.putExtra("TitleFull", mData.get(getAdapterPosition()).getTitleFull());
            intent.putExtra("ADAPTER",getAdapterPosition());
            innerContext.startActivity(intent);
        }
    }
}
