package com.example.hw9_productsearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WishlistPage extends Fragment
{
    public WishlistPage(){}

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private View wish_view;
    private List<Product> listWishes;
    private Double total_price = 0.0;

    private RecyclerViewAdapter rvAdapter;
    private SwipeRefreshLayout refresher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        wish_view = inflater.inflate(R.layout.wishlist_fragment, container, false);

        listWishes = new ArrayList<>();

        return wish_view;
    }

    private void loadWishlist()
    {
        SharedPreferences pref_wishlist = getContext().getSharedPreferences(getString(R.string.pref_wishlist_items), Context.MODE_PRIVATE);

        String wishlist_ids = pref_wishlist.getString("WISHLIST", null);

        List<String> wishlist_list;

        if(wishlist_ids != null)
            wishlist_list = new ArrayList<>(Arrays.asList(wishlist_ids.split(","))) ;
        else
            wishlist_list = new ArrayList<>();

        final int list_length = wishlist_list.size();
        for( int i = 0; i < list_length; i++)
        {
            if(!wishlist_list.get(i).matches(""))
            {
                String wish_url = "http://avishkarkola-hw8.us-east-2.elasticbeanstalk.com/details/"+wishlist_list.get(i);

                Log.i("WishlistPage","wish_url: "+wish_url);

                final RequestQueue mQueue = Volley.newRequestQueue(getContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        wish_url,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listTheWishes(response);
                        mQueue.stop();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        mQueue.stop();
                    }
                });

                mQueue.add(request);
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        total_price = 0.0;
        final ProgressBar loader = wish_view.findViewById(R.id.progressBar_wishes);
        final TextView none_wish = wish_view.findViewById(R.id.no_wishes);
        final TextView loading_text = wish_view.findViewById(R.id.loading_wishes);
        final TextView total_display = wish_view.findViewById(R.id.total_items_text);
        final TextView total_price_display = wish_view.findViewById(R.id.total_price_text);

        none_wish.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        loading_text.setVisibility(View.VISIBLE);

        listWishes.clear();
        setTheAdapter();
        loadWishlist();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loader.setVisibility(View.GONE);
                        loading_text.setVisibility(View.GONE);
                        if(listWishes.size() == 0)
                            none_wish.setVisibility(View.VISIBLE);

                        else
                            rvAdapter.notifyDataSetChanged();

                        total_display.setText("Wishlist Total ("+listWishes.size()+" items):");
                        total_price_display.setText("$"+String.format(Locale.US,"%.2f", total_price));

                    }
                });

            }
        }, 1100);

    }


    private void listTheWishes(JSONObject response)
    {
        try
        {
            String id, title, img_url, zip, shipping, condition, price;

            JSONObject temp_obj = response.getJSONObject("Item");
            id = temp_obj.getString("ItemID");
            title = temp_obj.getString("Title");
            String full_title = title;
            title = ellipsizer(title);

            if (temp_obj.has("PictureURL") && temp_obj.getJSONArray("PictureURL").length() > 0)
                img_url = temp_obj.getJSONArray("PictureURL").getString(0);
            else
                img_url = "http://www.51allout.co.uk/wp-content/uploads/2012/02/Image-not-found.gif";

            if(temp_obj.has("PostalCode"))
            {
                zip = temp_obj.getString("PostalCode");
                zip = "Zip: "+zip;
            }
            else
                zip = "Zip: N/A";

            if(temp_obj.has("ShippingCostSummary") && temp_obj.getJSONObject("ShippingCostSummary").has("ShippingServiceCost"))
                shipping = temp_obj.getJSONObject("ShippingCostSummary").getJSONObject("ShippingServiceCost").getString("Value");
            else
                shipping = "Shipping: N/A";

            if(shipping.matches("0"))
                shipping = "Free Shipping";
            else
                if(!shipping.matches("Shipping: N/A"))
                    shipping = "$" + shipping;

            if(temp_obj.has("ConditionDisplayName"))
                condition = temp_obj.getString("ConditionDisplayName");
            else
                condition = "Condition: N/A";

            if(temp_obj.has("CurrentPrice"))
            {
                price = temp_obj.getJSONObject("CurrentPrice").getString("Value");
                total_price += Double.valueOf(price);
                price = "$" + price;
            }
            else
                price = "Price: N/A";

            listWishes.add(new Product(id, title, zip, shipping, condition, price, img_url, R.drawable.cart_off, full_title));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void setTheAdapter()
    {
        RecyclerView res_rv = wish_view.findViewById(R.id.wishlist_recyclerview);
        rvAdapter = new RecyclerViewAdapter(getActivity(), getContext(),listWishes);
        res_rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        res_rv.setAdapter(rvAdapter);
    }

    private String ellipsizer(String str)
    {
        int t1 = 35;
        if (str.length() > t1)
        {
            while (str.charAt(t1-1)!= ' ')
                t1--;

            return str.substring(0,t1)+"...";
        }
        else
            return str;
    }
}
