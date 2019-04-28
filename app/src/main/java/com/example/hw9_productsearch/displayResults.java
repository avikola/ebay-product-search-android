package com.example.hw9_productsearch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class displayResults extends AppCompatActivity
{
    private String keyword;
    private String category;
    private String new_bool;
    private String used_bool;
    private String unspecified_bool;
    private String localpickup_bool;
    private String freeshipping_bool;
    private String miles;
    private String zipcode;
    private ProgressBar results_loading;
    private TextView results_loading_text;
    private TextView showing_text;

    private List<Product> listProducts;
    private RecyclerViewAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Back Button:

        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Passed Values;

        Intent intent = getIntent();

        keyword = intent.getStringExtra("Keyword");
        category = intent.getStringExtra("Category");
        switch (category)
        {
            case "Art":
                category="art";
                break;
            case "Baby":
                category="baby";
                break;
            case "Books":
                category="books";
                break;
            case "Clothing, Shoes & Accessories":
                category="clothes";
                break;
            case "Computers/Tablets & Networking":
                category="computers";
                break;
            case "Health & Beauty":
                category="health";
                break;
            case "Music":
                category="music";
                break;
            case "Videos Games & Consoles":
                category="games";
                break;
            default:
                category="";
                break;
        }
        new_bool = intent.getStringExtra("New");
        used_bool = intent.getStringExtra("Used");
        unspecified_bool = intent.getStringExtra("Unspecified");
        localpickup_bool = intent.getStringExtra("LocalPickup");
        freeshipping_bool = intent.getStringExtra("FreeShipping");
        miles = intent.getStringExtra("Miles");
        zipcode = intent.getStringExtra("Zipcode");

        results_loading = findViewById(R.id.progressBar_results);
        results_loading_text = findViewById(R.id.progressBar_results_text);
        showing_text = findViewById(R.id.showing_results_text);


        results_loading.setVisibility(View.VISIBLE);
        results_loading_text.setVisibility(View.VISIBLE);
        listProducts = new ArrayList<>();

        RecyclerView res_rv = findViewById(R.id.results_recyclerview);
        rvAdapter = new RecyclerViewAdapter(this,listProducts);
        res_rv.setLayoutManager(new GridLayoutManager(this, 2));
        res_rv.setAdapter(rvAdapter);

        loadPage();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        rvAdapter.notifyDataSetChanged();
    }

    private void loadPage()
    {
        // Main Process:
        String results_url = "http://avishkarkola-hw8.us-east-2.elasticbeanstalk.com/results";
        results_url += "?keyword="+keyword;
        results_url += "&category="+category;
        results_url += "&new="+new_bool;
        results_url += "&used="+used_bool;
        results_url += "&unspecified="+unspecified_bool;
        results_url += "&local_pickup="+localpickup_bool;
        results_url += "&free_shipping="+freeshipping_bool;
        results_url += "&distance="+miles;
        results_url += "&location="+zipcode;

        Log.i("displayResults","results_url: "+results_url);

        final RequestQueue mQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                results_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                listTheGrid(response);

                results_loading.setVisibility(View.GONE);
                results_loading_text.setVisibility(View.GONE);

                if(listProducts.size() != 0)
                {
                    showing_text.setVisibility(View.VISIBLE);

                    String textpt1 = "Showing ";
                    String textpart = textpt1 + listProducts.size();
                    String text_show = textpart + " results for ";
                    String text_full = text_show + keyword;
                    Spannable spannable = new SpannableString(text_full);

                    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mainOrange)), textpt1.length(), (textpt1 + listProducts.size()).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mainOrange)), text_show.length(), (text_show + keyword).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, text_full.length(), 0);
                    showing_text.setText(spannable, TextView.BufferType.SPANNABLE);
                }
                else
                    findViewById(R.id.results_error).setVisibility(View.VISIBLE);

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

    private void listTheGrid(JSONObject response)
    {
        try
        {
            int counter;
            String id, title, img_url, zip, shipping, condition, price;
            JSONArray temp_arr = response.getJSONArray("findItemsAdvancedResponse");
            JSONObject temp_obj = temp_arr.getJSONObject(0);
            temp_arr = temp_obj.getJSONArray("searchResult");
            temp_obj = temp_arr.getJSONObject(0);
            counter = temp_obj.getInt("@count");
            if(temp_obj.has("item"))
            {
                temp_arr = temp_obj.getJSONArray("item");
                for(int i = 0; i < counter; i++)
                {
                    id = temp_arr.getJSONObject(i).getJSONArray("itemId").getString(0);
                    title = temp_arr.getJSONObject(i).getJSONArray("title").getString(0);
                    String title_full = title;
                    title = ellipsizer(title);
                    if(temp_arr.getJSONObject(i).has("galleryURL"))
                        img_url = temp_arr.getJSONObject(i).getJSONArray("galleryURL").getString(0);
                    else
                        img_url = "http://www.51allout.co.uk/wp-content/uploads/2012/02/Image-not-found.gif";

                    if(temp_arr.getJSONObject(i).has("postalCode"))
                    {
                        zip = temp_arr.getJSONObject(i).getJSONArray("postalCode").getString(0);
                        zip = "Zip: "+zip;
                    }
                    else
                        zip = "Zip: N/A";

                    if(temp_arr.getJSONObject(i).has("shippingInfo") && temp_arr.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).has("shippingServiceCost"))
                        shipping = temp_arr.getJSONObject(i).getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__");
                    else
                        shipping = "Shipping: N/A";

                    if(shipping.matches("0.0"))
                        shipping = "Free Shipping";
                    else
                        shipping = "$"+shipping;

                    if (temp_arr.getJSONObject(i).has("condition"))
                        condition = temp_arr.getJSONObject(i).getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0);
                    else
                        condition = "Condition: N/A";

                    if(temp_arr.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).has("currentPrice"))
                        price = "$"+temp_arr.getJSONObject(i).getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__");
                    else
                        price = "Price: N/A";
                    listProducts.add(new Product(id, title, zip, shipping, condition, price, img_url, R.drawable.cart_plus, title_full));
                }
                setTheAdapter();
            }
            else
            {
                showing_text.setVisibility(View.GONE);

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void setTheAdapter()
    {
        rvAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (android.R.id.home == item.getItemId() )
        {
            SharedPreferences temp = getApplicationContext().getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = temp.edit();
            editor.putInt("BACK",1);
            editor.commit();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        SharedPreferences temp = getApplicationContext().getSharedPreferences("com.example.hw9_productsearch.WISHLIST_ID_LIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = temp.edit();
        editor.putInt("BACK",1);
        editor.commit();
    }
}
