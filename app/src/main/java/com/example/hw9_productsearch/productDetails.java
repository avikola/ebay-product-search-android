package com.example.hw9_productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class productDetails extends AppCompatActivity
{

    private String item_id;
    private String[] item_picture_urls;
    private String item_appbar_title;
    private String item_full_title;
    private String item_current_price;
    private String item_shipping_cost;
    private String item_brand;
    private String item_subtitle;
    private String[] item_all_specifics_values;
    private String item_store_name;
    private String item_store_url;
    private String item_product_url;
    private String item_feedback_score;
    private String item_positive_feedback_percent;
    private String item_feedback_rating_star;
    private String item_global_shipping;
    private String item_handling_time;
    private String item_condition;
    private String item_returns_accepted;
    private String item_returns_within;
    private String item_refund;
    private String item_shipping_cost_paid_by;
    private String[] photo_links;
    private int adapter_pos;

    private ViewPager detailsviews;
    private  TabLayout detailstabs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        detailsviews = findViewById(R.id.details_viewpage);
        detailstabs = findViewById(R.id.details_tabs);

        // Back Button:

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Passed ID:

        Intent intent = getIntent();
        item_id = intent.getStringExtra("ItemID");
        item_appbar_title = intent.getStringExtra("ItemTitle");
        item_full_title = intent.getStringExtra("TitleFull");
        adapter_pos = intent.getIntExtra("ADAPTER",0);
        getSupportActionBar().setTitle(item_appbar_title);

        // Wish list Listener:
        createFabListener();

        createTabs();

        getAllStrings();
    }

    private void getAllStrings()
    {
        String details_url = "http://avishkarkola-hw8.us-east-2.elasticbeanstalk.com/details/"+item_id;

        Log.i("ProductDetails","details_url: "+details_url);

        final RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                details_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    JSONObject temp_obj = response.getJSONObject("Item");
                    item_id = temp_obj.getString("ItemID");
                    if (temp_obj.has("PictureURL") && temp_obj.getJSONArray("PictureURL").length() > 0)
                    {
                        item_picture_urls = new String[temp_obj.getJSONArray("PictureURL").length()];
                        for(int i = 0; i < temp_obj.getJSONArray("PictureURL").length(); i++)
                            item_picture_urls[i] = temp_obj.getJSONArray("PictureURL").getString(i);
                    }
                    else
                    {
                        item_picture_urls = new String[1];
                        item_picture_urls[0] = "null";
                    }

                    if(temp_obj.has("ShippingCostSummary") && temp_obj.getJSONObject("ShippingCostSummary").has("ShippingServiceCost"))
                        item_shipping_cost = temp_obj.getJSONObject("ShippingCostSummary").getJSONObject("ShippingServiceCost").getString("Value");
                    else
                        item_shipping_cost = "null";

                    if(item_shipping_cost.matches("0"))
                        item_shipping_cost = "Free Shipping";
                    else
                        if(!item_shipping_cost.matches("null"))
                            item_shipping_cost = "$" + item_shipping_cost;

                    if(temp_obj.has("CurrentPrice"))
                        item_current_price = "$"+temp_obj.getJSONObject("CurrentPrice").getString("Value");
                    else
                        item_current_price = "Price: N/A";

                    if(temp_obj.has("ItemSpecifics") && temp_obj.getJSONObject("ItemSpecifics").has("NameValueList"))
                    {
                        item_all_specifics_values = new String[temp_obj.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length()];
                        for(int i = 0; i < temp_obj.getJSONObject("ItemSpecifics").getJSONArray("NameValueList").length(); i++)
                        {
                            if(temp_obj.getJSONObject("ItemSpecifics")
                                    .getJSONArray("NameValueList")
                                    .getJSONObject(i).getString("Name").matches("Brand"))
                                item_brand = temp_obj.getJSONObject("ItemSpecifics")
                                        .getJSONArray("NameValueList")
                                        .getJSONObject(i).getJSONArray("Value").getString(0);
                            else
                            {
                                item_all_specifics_values[i] = temp_obj.getJSONObject("ItemSpecifics")
                                        .getJSONArray("NameValueList").getJSONObject(i)
                                        .getJSONArray("Value").getString(0);
                                String temp = item_all_specifics_values[i];
                                item_all_specifics_values[i] = temp.substring(0,1).toUpperCase() + temp.substring(1);
                            }
                        }
                    }
                    else
                    {
                        item_brand = "null";
                        item_all_specifics_values = new String[1];
                        item_all_specifics_values[0] = "null";
                    }

                    if(temp_obj.has("Storefront"))
                    {
                        if(temp_obj.getJSONObject("Storefront").has("StoreName"))
                            item_store_name = temp_obj.getJSONObject("Storefront").getString("StoreName");
                        else
                            item_store_name = "null";

                        if(temp_obj.getJSONObject("Storefront").has("StoreURL"))
                            item_store_url = temp_obj.getJSONObject("Storefront").getString("StoreURL");
                        else
                            item_store_url = "null";
                    }
                    else
                    {
                        item_store_name = "null";
                        item_store_url = "null";
                    }

                    if(temp_obj.has("HandlingTime"))
                    {
                        item_handling_time = temp_obj.getString("HandlingTime");
                        if(item_handling_time.matches("0") || item_handling_time.matches("1"))
                            item_handling_time += " day";
                        else
                            item_handling_time += " days";
                    }
                    else
                        item_handling_time = "null";

                    if(temp_obj.has("GlobalShipping"))
                    {
                        item_global_shipping = temp_obj.getString("GlobalShipping");
                        if(item_global_shipping.matches("true"))
                            item_global_shipping = "Yes";
                        else
                            item_global_shipping = "No";
                    }
                    else
                        item_global_shipping = "null";

                    if(temp_obj.has("ConditionDisplayName"))
                        item_condition = temp_obj.getString("ConditionDisplayName");
                    else
                        item_condition = "null";

                    if(temp_obj.has("ReturnPolicy"))
                    {
                        if(temp_obj.getJSONObject("ReturnPolicy").has("Refund"))
                            item_refund = temp_obj.getJSONObject("ReturnPolicy").getString("Refund");
                        else
                            item_refund = "null";

                        if(temp_obj.getJSONObject("ReturnPolicy").has("ReturnsWithin"))
                            item_returns_within = temp_obj.getJSONObject("ReturnPolicy").getString("ReturnsWithin");
                        else
                            item_returns_within = "null";

                        if(temp_obj.getJSONObject("ReturnPolicy").has("ShippingCostPaidBy"))
                            item_shipping_cost_paid_by = temp_obj.getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy");
                        else
                            item_shipping_cost_paid_by = "null";

                        if(temp_obj.getJSONObject("ReturnPolicy").has("ReturnsAccepted"))
                            item_returns_accepted = temp_obj.getJSONObject("ReturnPolicy").getString("ReturnsAccepted");
                        else
                            item_returns_accepted = "null";
                    }
                    else
                    {
                        item_refund = "null";
                        item_returns_within = "null";
                        item_shipping_cost_paid_by = "null";
                        item_returns_accepted = "null";
                    }

                    if(temp_obj.has("Seller"))
                    {
                        if(temp_obj.getJSONObject("Seller").has("FeedbackRatingStar"))
                            item_feedback_rating_star = temp_obj.getJSONObject("Seller").getString("FeedbackRatingStar");
                        else
                            item_feedback_rating_star = "null";

                        if(temp_obj.getJSONObject("Seller").has("FeedbackScore"))
                            item_feedback_score = temp_obj.getJSONObject("Seller").getString("FeedbackScore");
                        else
                            item_feedback_score = "null";

                        if(temp_obj.getJSONObject("Seller").has("PositiveFeedbackPercent"))
                            item_positive_feedback_percent = temp_obj.getJSONObject("Seller").getString("PositiveFeedbackPercent");
                        else
                            item_positive_feedback_percent = "null";
                    }
                    else
                    {
                        item_feedback_rating_star = "null";
                        item_feedback_score = "null";
                        item_positive_feedback_percent = "null";
                    }

                    item_product_url = temp_obj.getString("ViewItemURLForNaturalSearch");
                    if(temp_obj.has("Subtitle"))
                        item_subtitle = temp_obj.getString("Subtitle");
                    else
                        item_subtitle = "null";

                    transferStrings();

                    findViewById(R.id.progressBar_details).setVisibility(View.GONE);
                    findViewById(R.id.progressBar_details_text).setVisibility(View.GONE);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

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
        getPhotos();
        getSimilar();
    }

    private void getPhotos()
    {

        String photos_url = "http://avishkarkola-hw8.us-east-2.elasticbeanstalk.com/search/"+Uri.parse(item_full_title.replaceAll("/"," "));

        Log.i("ProductDetails","photos_url: "+item_full_title);

        final RequestQueue mQueue1 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET,
                photos_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    if(response.has("items"))
                    {
                        JSONArray temp_obj = response.getJSONArray("items");
                        photo_links = new String[temp_obj.length()];
                        for(int i = 0; i < temp_obj.length(); i++)
                            photo_links[i] = temp_obj.getJSONObject(i).getString("link");
                    }
                    else
                    {
                        photo_links = new String[1];
                        photo_links[0] = "null";
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                mQueue1.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mQueue1.stop();
            }
        });

        mQueue1.add(request1);
    }

    private String[] similar_item_urls;
    private String[] similar_title;
    private String[] similar_shipping_cost;
    private String[] similar_timeLeft;
    private String[] similar_price;
    private String[] similar_image_urls;
    private String similar_total;

    private void getSimilar()
    {
        String similar_url = "http://avishkarkola-hw8.us-east-2.elasticbeanstalk.com/similar/"+item_id;

        Log.i("ProductDetails","similar_url: "+similar_url);

        final RequestQueue mQueue2 = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET,
                similar_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    int counter;
                    JSONObject temp_obj = response.getJSONObject("getSimilarItemsResponse");
                    if(temp_obj.has("itemRecommendations")&& temp_obj.getJSONObject("itemRecommendations").has("item"))
                    {
                        temp_obj = temp_obj.getJSONObject("itemRecommendations");
                        JSONArray temp_arr = temp_obj.getJSONArray("item");
                        counter = temp_arr.length();
                        similar_total = ""+counter;

                        similar_shipping_cost = new String[counter];
                        similar_price = new String[counter];
                        similar_title = new String[counter];
                        similar_item_urls = new String[counter];
                        similar_timeLeft = new String[counter];
                        similar_image_urls = new String[counter];

                        for(int i=0; i < counter; i++)
                        {
                            temp_obj = temp_arr.getJSONObject(i);

                            if(temp_obj.has("shippingCost"))
                                similar_shipping_cost[i] = temp_obj.getJSONObject("shippingCost").getString("__value__");
                            else
                                similar_shipping_cost[i] = "null";

                            if(similar_shipping_cost[i].matches("0.00"))
                                similar_shipping_cost[i] = "Free Shipping";
                            else
                                if(!similar_shipping_cost[i].matches("null"))
                                    similar_shipping_cost[i] = "$" + similar_shipping_cost[i];

                            if(temp_obj.has("buyItNowPrice"))
                                similar_price[i] = "$"+temp_obj.getJSONObject("buyItNowPrice").getString("__value__");
                            else
                                similar_price[i] = "Price: N/A";

                            if(temp_obj.has("viewItemURL"))
                                similar_item_urls[i] = temp_obj.getString("viewItemURL");
                            else
                                similar_item_urls[i] = "null";

                            if(temp_obj.has("title"))
                                similar_title[i] = temp_obj.getString("title");
                            else
                                similar_title[i] = "null";

                            if(temp_obj.has("timeLeft"))
                                similar_timeLeft[i] = temp_obj.getString("timeLeft");
                            else
                                similar_timeLeft[i] = "null";

                            if(temp_obj.has("imageURL"))
                                similar_image_urls[i] = temp_obj.getString("imageURL");
                            else
                                similar_image_urls[i] = "null";
                        }
                    }
                    else
                    {
                        similar_shipping_cost = new String[1];
                        similar_shipping_cost[0] = "null";
                        similar_price = new String[1];
                        similar_price[0] = "null";
                        similar_title = new String[1];
                        similar_title[0] = "null";
                        similar_item_urls = new String[1];
                        similar_item_urls[0] = "null";
                        similar_timeLeft = new String[1];
                        similar_timeLeft[0] = "null";
                        similar_image_urls = new String[1];
                        similar_image_urls[0] = "null";
                        similar_total = "0";
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                mQueue2.stop();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mQueue2.stop();
            }
        });

        mQueue2.add(request2);

    }

    private void transferStrings()
    {

        SectionsPagerAdapter adapt = new SectionsPagerAdapter(getSupportFragmentManager(), detailstabs.getTabCount());
        detailsviews.setAdapter(adapt);

        detailsviews.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(detailstabs));

        detailstabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tabSelected)
            {
                detailsviews.setCurrentItem(tabSelected.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tabSelected){}

            @Override
            public void onTabReselected(TabLayout.Tab tabSelected){

            }
        });
    }

    private void createTabs()
    {
        TabLayout.Tab firstTab = detailstabs.newTab();
        firstTab.setText("PRODUCT");
        firstTab.setIcon(R.drawable.information_variant);
        detailstabs.addTab(firstTab);

        TabLayout.Tab secondTab = detailstabs.newTab();
        secondTab.setText("SHIPPING");
        secondTab.setIcon(R.drawable.truck_delivery);
        detailstabs.addTab(secondTab);

        TabLayout.Tab thirdTab = detailstabs.newTab();
        thirdTab.setText("PHOTOS");
        thirdTab.setIcon(R.drawable.google);
        detailstabs.addTab(thirdTab);

        TabLayout.Tab fourthTab = detailstabs.newTab();
        fourthTab.setText("SIMILAR");
        fourthTab.setIcon(R.drawable.equal);
        detailstabs.addTab(fourthTab);
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        final int total__tabs;

        SectionsPagerAdapter(FragmentManager fm, int total__tabs) {
            super(fm);
            this.total__tabs = total__tabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Bundle b1 = new Bundle();
                    b1.putStringArray("PictureURL", item_picture_urls);
                    b1.putString("Title", item_full_title);
                    b1.putString("CurrentPrice", item_current_price);
                    b1.putString("ShippingCost", item_shipping_cost);
                    b1.putString("Brand", item_brand);
                    b1.putString("Subtitle", item_subtitle);
                    b1.putStringArray("AllItemSpecifics", item_all_specifics_values);
                    Fragment t1 = new ProductTab();
                    t1.setArguments(b1);
                    return t1;
                case 1:
                    Bundle b2 = new Bundle();
                    b2.putString("StoreName",item_store_name);
                    b2.putString("StoreURL",item_store_url);
                    b2.putString("FeedbackScore",item_feedback_score);
                    b2.putString("PositiveFeedbackPercent",item_positive_feedback_percent);
                    b2.putString("FeedbackRatingStar",item_feedback_rating_star);
                    b2.putString("ShippingCost",item_shipping_cost);
                    b2.putString("GlobalShipping",item_global_shipping);
                    b2.putString("HandlingTime",item_handling_time);
                    b2.putString("Condition",item_condition);
                    b2.putString("ReturnsAccepted",item_returns_accepted);
                    b2.putString("ReturnsWithin",item_returns_within);
                    b2.putString("Refund",item_refund);
                    b2.putString("ShippingCostPaidBy",item_shipping_cost_paid_by);
                    Fragment t2 = new ShippingTab();
                    t2.setArguments(b2);
                    return t2;
                case 2:
                    Bundle b3 = new Bundle();
                    b3.putStringArray("Photos", photo_links);
                    Fragment t3 = new PhotosTab();
                    t3.setArguments(b3);
                    return t3;
                case 3:
                    Bundle b4 = new Bundle();
                    b4.putStringArray("Title", similar_title);
                    b4.putStringArray("ItemURLS", similar_item_urls);
                    b4.putStringArray("Price", similar_price);
                    b4.putStringArray("ShipCost", similar_shipping_cost);
                    b4.putStringArray("timeLeft", similar_timeLeft);
                    b4.putStringArray("ImageURLS", similar_image_urls);
                    b4.putString("Total", similar_total);
                    Fragment t4 = new SimilarTab();
                    t4.setArguments(b4);
                    return t4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return total__tabs;
        }
    }

    private void createFabListener()
    {
        final FloatingActionButton fab = findViewById(R.id.wishlist_fab);

        SharedPreferences pref_wishlist = this.getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);
        String wishlist_ids = pref_wishlist.getString("WISHLIST", null);

        if(wishlist_ids != null)
        {
            List<String> wishlist_list = new ArrayList<>(Arrays.asList(wishlist_ids.split(","))) ;

            if(wishlist_list.contains(item_id))
                Picasso.get().load(R.drawable.cart_off_white).into(fab);
            else
                Picasso.get().load(R.drawable.cart_plus_white).into(fab);
        }
        else
            Picasso.get().load(R.drawable.cart_plus_white).into(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref_wishlist = getApplicationContext().getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);
                final SharedPreferences.Editor editor = pref_wishlist.edit();
                final String wishlist_ids = pref_wishlist.getString("WISHLIST", null);

                List<String> wishlist_list;
                if(wishlist_ids != null)
                {
                    wishlist_list = new ArrayList<>(Arrays.asList(wishlist_ids.split(","))) ;

                    if(wishlist_list.contains(item_id))
                    {
                        wishlist_list.remove(item_id);
                        Picasso.get().load(R.drawable.cart_plus_white).into(fab);
                        Toast.makeText(getApplicationContext(), item_appbar_title +" was removed from wish list",Toast.LENGTH_SHORT).show();
                        editor.putInt("REMOVED",adapter_pos);
                    }
                    else
                    {
                        wishlist_list.add(item_id);
                        Picasso.get().load(R.drawable.cart_off_white).into(fab);
                        Toast.makeText(getApplicationContext(), item_appbar_title +" was added to wish list",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    wishlist_list = new ArrayList<>();
                    wishlist_list.add(item_id);
                    Picasso.get().load(R.drawable.cart_off_white).into(fab);
                    Toast.makeText(getApplicationContext(), item_appbar_title +" was added to wish list",Toast.LENGTH_SHORT).show();
                }

                String list_to_string = TextUtils.join(",",wishlist_list);
                editor.putString("WISHLIST", list_to_string);
                editor.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.facebook_share_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (android.R.id.home == item.getItemId() )
            finish();

        if (R.id.fb_button == item.getItemId())
        {
            if(TextUtils.isEmpty(item_appbar_title) || TextUtils.isEmpty(item_current_price) || TextUtils.isEmpty(item_product_url))
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String quote = "Buy "+ item_appbar_title +" for "+item_current_price+" from Ebay!";
                        String url = "https://www.facebook.com/dialog/share?app_id=284794219082312&display=popup&href="
                                +Uri.parse(item_product_url)+"&quote="+quote
                                +"&hashtag=%23CSCI571Spring2019Ebay";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                },1000);
            }
            else
            {
                String quote = "Buy "+ item_appbar_title +" for "+item_current_price+" from Ebay!";
                String url = "https://www.facebook.com/dialog/share?app_id=284794219082312&display=popup&href="
                        +Uri.parse(item_product_url)+"&quote="+quote
                        +"&hashtag=%23CSCI571Spring2019Ebay";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
