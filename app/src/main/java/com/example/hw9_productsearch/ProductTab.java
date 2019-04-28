package com.example.hw9_productsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductTab extends Fragment
{
    public ProductTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private static View product_view;

    private String item_full_title;
    private String item_current_price;
    private String item_shipping_cost;
    private String item_brand;
    private String item_subtitle;
    private String[] item_all_specifics_values;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        product_view = inflater.inflate(R.layout.product_tab_fragment, container, false);

        String[] item_picture_urls = getArguments().getStringArray("PictureURL");
        item_full_title = getArguments().getString("Title");
        item_current_price = getArguments().getString("CurrentPrice");
        item_shipping_cost = getArguments().getString("ShippingCost");
        item_brand = getArguments().getString("Brand");
        item_subtitle = getArguments().getString("Subtitle");
        item_all_specifics_values = getArguments().getStringArray("AllItemSpecifics");

        LinearLayout gallery_slider = product_view.findViewById(R.id.id_gallery);
        LayoutInflater temper = LayoutInflater.from(getContext());
        View slider_view;
        HorizontalScrollView hsv = product_view.findViewById(R.id.gallery_slider);

        if(item_picture_urls !=null && item_picture_urls.length > 0)
            for (String item_picture_url : item_picture_urls)
            {
                slider_view = temper.inflate(R.layout.image_slider, gallery_slider, false);
                ImageView slider = slider_view.findViewById(R.id.slider_viewer);
                Picasso.get().load(item_picture_url).into(slider);
                gallery_slider.addView(slider_view);
            }

        if(item_picture_urls[0].matches("null"))
            hsv.setVisibility(View.GONE);

        setEverything();

        return product_view;
    }

    private void setEverything()
    {
        TextView view_text = product_view.findViewById(R.id.product_tab_title);
        view_text.setText(item_full_title);

        view_text = product_view.findViewById(R.id.product_tab_price_text);
        view_text.setText(item_current_price);

        view_text = product_view.findViewById(R.id.product_tab_ship_text);

        if(item_shipping_cost.matches("null"))
            view_text.setVisibility(View.GONE);
        else
            if(item_shipping_cost.matches("Free Shipping"))
                view_text.setText("With "+item_shipping_cost);
            else
                view_text.setText("With "+item_shipping_cost+" Shipping");

        view_text = product_view.findViewById(R.id.product_tab_subtitle_text);

        LinearLayout lin = product_view.findViewById(R.id.subtitle_layout);

        if(item_subtitle.matches("null"))
            lin.setVisibility(View.GONE);
        else
            view_text.setText(item_subtitle);

        view_text = product_view.findViewById(R.id.product_tab_price_text_h);
        view_text.setText(item_current_price);

        view_text = product_view.findViewById(R.id.product_tab_brand_text);
        lin = product_view.findViewById(R.id.brand_layout);

        if(TextUtils.isEmpty(item_brand) || item_brand.matches("null"))
            lin.setVisibility(View.GONE);
        else
            view_text.setText(item_brand);

        view_text = product_view.findViewById(R.id.spec_bullets);
        lin = product_view.findViewById(R.id.spec_layout);
        TableRow hr = product_view.findViewById(R.id.hr2);

        if((TextUtils.isEmpty(item_brand)||item_brand.matches("null")) && item_all_specifics_values[0].matches("null"))
        {
            lin.setVisibility(View.GONE);
            view_text.setVisibility(View.GONE);
            hr.setVisibility(View.GONE);
        }
        else
        {
            StringBuilder bullet_string= new StringBuilder();
            if(!(TextUtils.isEmpty(item_brand)||item_brand.matches("null")))
                bullet_string.append("&#8226; ").append(item_brand).append("<br/><br/>");

            for (String item_all_specifics_value : item_all_specifics_values)
            {
                if(item_all_specifics_value != null)
                    bullet_string.append("&#8226; ").append(item_all_specifics_value).append("<br/><br/>");
            }

            view_text.setText(Html.fromHtml(bullet_string.toString()));
        }

    }
}
