package com.example.hw9_productsearch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wssholmes.stark.circular_score.CircularScoreView;


public class ShippingTab extends Fragment
{

    public ShippingTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View shipping_view;

    private String item_store_name;
    private String item_store_url;
    private String item_feedback_score;
    private String item_positive_feedback_percent;
    private String item_feedback_rating_star;
    private String item_shipping_cost;
    private String item_global_shipping;
    private String item_handling_time;
    private String item_condition;
    private String item_returns_accepted;
    private String item_returns_within;
    private String item_refund;
    private String item_shipping_cost_paid_by;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        shipping_view = inflater.inflate(R.layout.shipping_tab_fragment, container, false);

        item_store_name = getArguments().getString("StoreName");
        item_store_url = getArguments().getString("StoreURL");
        item_feedback_score = getArguments().getString("FeedbackScore");
        item_positive_feedback_percent = getArguments().getString("PositiveFeedbackPercent");
        item_feedback_rating_star = getArguments().getString("FeedbackRatingStar");
        item_shipping_cost = getArguments().getString("ShippingCost");
        item_global_shipping = getArguments().getString("GlobalShipping");
        item_handling_time = getArguments().getString("HandlingTime");
        item_condition = getArguments().getString("Condition");
        item_returns_accepted = getArguments().getString("ReturnsAccepted");
        item_returns_within = getArguments().getString("ReturnsWithin");
        item_refund = getArguments().getString("Refund");
        item_shipping_cost_paid_by = getArguments().getString("ShippingCostPaidBy");

        setEverything();

        return shipping_view;
    }

    private void setEverything()
    {
        boolean check_empty1 = false, check_empty2 = false, check_empty3 = false;

        // Sold By:

        TextView view_text = shipping_view.findViewById(R.id.ship_tab_store_name_text);
        LinearLayout lin = shipping_view.findViewById(R.id.store_name_layout);

        if(item_store_name.matches("null")
                && item_feedback_score.matches("null")
                && item_positive_feedback_percent.matches("null")
                && item_feedback_rating_star.matches("null"))
        {
            shipping_view.findViewById(R.id.sold_by_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.store_name_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.f_score_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.popularity_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.f_star_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.hr1).setVisibility(View.GONE);
            check_empty1 = true;
        }
        else
        {
            if(item_store_name.matches("null"))
                lin.setVisibility(View.GONE);
            else
            {
                SpannableString store_link = new SpannableString(item_store_name);
                store_link.setSpan(new UnderlineSpan(), 0, store_link.length(), 0);
                view_text.setText(store_link);

                view_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(item_store_url));
                        startActivity(i);
                    }
                });
            }

            ImageView view_img = shipping_view.findViewById(R.id.ship_feedback_star);
            if (item_feedback_rating_star.matches("null"))
                shipping_view.findViewById(R.id.f_star_layout).setVisibility(View.GONE);
            else
            {
                if(item_feedback_rating_star.contains("Shooting"))
                {
                    String temp = item_feedback_rating_star.replace("Shooting","");
                    switch (temp)
                    {
                        case "Yellow":
                            view_img.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Turquoise":
                            view_img.setColorFilter(Color.parseColor("#40E0D0"), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Purple":
                            view_img.setColorFilter(Color.parseColor("#800080"), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Red":
                            view_img.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Green":
                            view_img.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Silver":
                            view_img.setColorFilter(Color.parseColor("#C0C0C0"), PorterDuff.Mode.SRC_ATOP);
                            break;
                        default:
                            break;
                    }
                    view_img.setImageResource(R.drawable.star_shooting);
                }
                else
                {
                    String temp = item_feedback_rating_star;
                    switch (temp)
                    {
                        case "Yellow":
                            view_img.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Turquoise":
                            view_img.setColorFilter(Color.parseColor("#40E0D0"), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Purple":
                            view_img.setColorFilter(Color.parseColor("#800080"), PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Red":
                            view_img.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Green":
                            view_img.setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
                            break;
                        case "Blue":
                            view_img.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_ATOP);
                            break;
                        default:
                            break;
                    }
                    view_img.setImageResource(R.drawable.star_circle);
                }
            }

            lin = shipping_view.findViewById(R.id.popularity_layout);
            CircularScoreView pop_circler = shipping_view.findViewById(R.id.pop_circle);

            if(item_positive_feedback_percent.matches("null"))
                lin.setVisibility(View.GONE);
            else
                pop_circler.setScore((int)Math.floor(Double.parseDouble(item_positive_feedback_percent)));

            lin = shipping_view.findViewById(R.id.f_score_layout);
            view_text = shipping_view.findViewById(R.id.ship_tab_feedback_score);

            if(item_feedback_score.matches("null"))
                lin.setVisibility(View.GONE);
            else
                view_text.setText(item_feedback_score);

        }

        // Shipping Info:

        if(item_shipping_cost.matches("null")
                && item_global_shipping.matches("null")
                && item_handling_time.matches("null")
                && item_condition.matches("null"))
        {
            shipping_view.findViewById(R.id.shipping_info_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.shipping_cost_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.global_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.handling_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.condition_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.hr2).setVisibility(View.GONE);
            check_empty2 = true;
        }
        else
        {
            view_text = shipping_view.findViewById(R.id.ship_tab_cost_text);
            if(item_shipping_cost.matches("null"))
                shipping_view.findViewById(R.id.shipping_cost_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_shipping_cost);

            view_text = shipping_view.findViewById(R.id.ship_tab_global);
            if(item_global_shipping.matches("null"))
                shipping_view.findViewById(R.id.global_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_global_shipping);

            view_text = shipping_view.findViewById(R.id.ship_tab_handling_text);
            if(item_handling_time.matches("null"))
                shipping_view.findViewById(R.id.handling_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_handling_time);

            view_text = shipping_view.findViewById(R.id.ship_condition_text);
            if(item_condition.matches("null"))
                shipping_view.findViewById(R.id.condition_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_condition);
        }

        // Return Policy:

        if(item_returns_accepted.matches("null")
                && item_returns_within.matches("null")
                && item_refund.matches("null")
                && item_shipping_cost_paid_by.matches("null"))
        {
            shipping_view.findViewById(R.id.policy_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.within_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.return_policy_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.refund_layout).setVisibility(View.GONE);
            shipping_view.findViewById(R.id.ship_by_layout).setVisibility(View.GONE);
            check_empty3 = true;
        }
        else
        {
            view_text = shipping_view.findViewById(R.id.ship_tab_policy_text);
            if(item_returns_accepted.matches("null"))
                shipping_view.findViewById(R.id.policy_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_returns_accepted);

            view_text = shipping_view.findViewById(R.id.ship_tab_within_text);
            if(item_returns_within.matches("null"))
                shipping_view.findViewById(R.id.within_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_returns_within);

            view_text = shipping_view.findViewById(R.id.ship_tab_refund_text);
            if(item_refund.matches("null"))
                shipping_view.findViewById(R.id.refund_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_refund);

            view_text = shipping_view.findViewById(R.id.ship_ship_by_text);
            if(item_shipping_cost_paid_by.matches("null"))
                shipping_view.findViewById(R.id.ship_by_layout).setVisibility(View.GONE);
            else
                view_text.setText(item_shipping_cost_paid_by);
        }

        if(check_empty1 && check_empty2 && check_empty3)
            shipping_view.findViewById(R.id.ship_error).setVisibility(View.VISIBLE);
        else
            shipping_view.findViewById(R.id.ship_error).setVisibility(View.GONE);

    }
}
