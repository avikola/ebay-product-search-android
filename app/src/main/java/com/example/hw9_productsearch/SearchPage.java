package com.example.hw9_productsearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class SearchPage extends Fragment
{

    public SearchPage(){}

    @Override
    public  void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    private RequestQueue requestQueue;
    private AutoCompleteTextView zip_text;
    private String final_zip;
    private String[] result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View search_view = inflater.inflate(R.layout.search_fragment, container, false);

        final EditText key_text = search_view.findViewById(R.id.keyword_box);
        final TextView key_req = search_view.findViewById(R.id.keyword_required);
        final CheckBox nearby_check = search_view.findViewById(R.id.nearby_box);
        final RadioButton current_check = search_view.findViewById(R.id.current_radio);
        final RadioButton zip_check = search_view.findViewById(R.id.zip_radio);

        zip_text = search_view.findViewById(R.id.zipcode_box);

        final TextView zip_req = search_view.findViewById(R.id.zip_required);
        final Button clear_btn = search_view.findViewById(R.id.clear_button);
        final Button submit_btn = search_view.findViewById(R.id.search_button);

        zip_text.setEnabled(false);

        // get zip:

        final RequestQueue mQueue = Volley.newRequestQueue(getContext());
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "http://ip-api.com/json",
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try
                {
                    final_zip = response.getString("zip");
                    mQueue.stop();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                mQueue.stop();
            }
        });

        mQueue.add(request);

        // Nearby Search:

        nearby_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {

            final int DP_sizer = 185;
            final int DP_sizer2 = 24;
            int find_px;
            int find_px2;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    EditText reveal_text = search_view.findViewById(R.id.miles_box);
                    reveal_text.setVisibility(View.VISIBLE);
                    reveal_text = search_view.findViewById(R.id.zipcode_box);
                    reveal_text.setVisibility(View.VISIBLE);

                    RadioButton radio_reveal = search_view.findViewById(R.id.current_radio);
                    radio_reveal.setVisibility(View.VISIBLE);
                    radio_reveal = search_view.findViewById(R.id.zip_radio);
                    radio_reveal.setVisibility(View.VISIBLE);

                    TextView reveal_label = search_view.findViewById(R.id.from_label);
                    reveal_label.setVisibility(View.VISIBLE);

                    Button move_btn = search_view.findViewById(R.id.search_button);

                    find_px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, DP_sizer, getResources()
                                    .getDisplayMetrics());
                    find_px2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, DP_sizer2, getResources()
                                    .getDisplayMetrics());

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) move_btn.getLayoutParams();
                    params.setMargins(find_px2, find_px, 0, 0);
                    move_btn.setLayoutParams(params);

                    if(zip_check.isChecked())
                        if(reveal_text.getText().toString().matches(""))
                            zip_req.setVisibility(View.VISIBLE);
                        else
                            zip_req.setVisibility(View.GONE);
                    else
                        zip_req.setVisibility(View.GONE);

                }
                else
                {
                    EditText reveal_text = search_view.findViewById(R.id.miles_box);
                    reveal_text.setVisibility(View.INVISIBLE);
                    reveal_text = search_view.findViewById(R.id.zipcode_box);
                    reveal_text.setVisibility(View.INVISIBLE);

                    RadioButton radio_reveal = search_view.findViewById(R.id.current_radio);
                    radio_reveal.setVisibility(View.INVISIBLE);
                    radio_reveal = search_view.findViewById(R.id.zip_radio);
                    radio_reveal.setVisibility(View.INVISIBLE);

                    TextView reveal_label = search_view.findViewById(R.id.from_label);
                    reveal_label.setVisibility(View.INVISIBLE);

                    Button move_btn = search_view.findViewById(R.id.search_button);

                    find_px2 = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, DP_sizer2, getResources()
                                    .getDisplayMetrics());

                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) move_btn.getLayoutParams();
                    params.setMargins(find_px2, find_px2, 0, 0);
                    move_btn.setLayoutParams(params);

                    zip_req.setVisibility(View.GONE);
                }
            }
        });

        // On Radio Change:

        current_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    zip_req.setVisibility(View.GONE);
                    zip_check.setChecked(false);
                    zip_text.setEnabled(false);
                }
            }
        });

        zip_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    current_check.setChecked(false);
                    zip_text.setEnabled(true);
                }
            }
        });

        // Autocomplete:

        requestQueue = Volley.newRequestQueue(getContext());

        zip_text.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        "http://api.geonames.org/postalCodeSearchJSON?postalcode_startsWith="+s.toString()+"&username=avishkarkola&country=US&maxRows=5",
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        result = setAutoAdapter(response);

                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(result!=null && result.length>0) {
                                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, result);
                                            AutoCompleteTextView zip_text2 = search_view.findViewById(R.id.zipcode_box);
                                            zip_text2.setAdapter(adapter);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }, 300);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        requestQueue.stop();
                    }
                });

                requestQueue.add(request);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear Button:

        clear_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                EditText text_reset = search_view.findViewById(R.id.keyword_box);
                text_reset.setText("");

                text_reset = search_view.findViewById(R.id.miles_box);
                text_reset.setText("");

                text_reset = search_view.findViewById(R.id.zipcode_box);
                text_reset.setText("");

                Spinner category_reset = search_view.findViewById(R.id.category_spinner);
                category_reset.setSelection(0);

                CheckBox box_reset = search_view.findViewById(R.id.new_box);
                box_reset.setChecked(false);

                box_reset = search_view.findViewById(R.id.used_box);
                box_reset.setChecked(false);

                box_reset = search_view.findViewById(R.id.unspecified_box);
                box_reset.setChecked(false);

                box_reset = search_view.findViewById(R.id.localpickup_box);
                box_reset.setChecked(false);

                box_reset = search_view.findViewById(R.id.freeshipping_box);
                box_reset.setChecked(false);

                box_reset = search_view.findViewById(R.id.nearby_box);
                box_reset.setChecked(false);

                current_check.setChecked(true);
                zip_check.setChecked(false);

                TextView key_req = search_view.findViewById(R.id.keyword_required);
                key_req.setVisibility(View.GONE);
                zip_req.setVisibility(View.GONE);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                boolean toaster = false;

                if (key_text.getText().toString().trim().matches(""))
                {
                    key_req.setVisibility(View.VISIBLE);
                    toaster = true;
                }
                else
                    key_req.setVisibility(View.GONE);

                if(zip_check.isChecked())
                {
                    if(zip_text.getText().toString().matches(""))
                    {
                        zip_req.setVisibility(View.VISIBLE);
                        toaster = true;
                    }
                    else
                        zip_req.setVisibility(View.GONE);
                }
                else
                    zip_req.setVisibility(View.GONE);

                if(toaster)
                    Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_SHORT).show();

                else       // Main Search Transfer
                {
                    Intent intent = new Intent(getContext(), displayResults.class);

                    intent.putExtra("Keyword",key_text.getText().toString().trim());

                    Spinner temp_spin = search_view.findViewById(R.id.category_spinner);
                    intent.putExtra("Category",temp_spin.getSelectedItem().toString());

                    CheckBox box_passer = search_view.findViewById(R.id.new_box);

                    intent.putExtra("New", box_passer.isChecked()?"true":"false");

                    box_passer = search_view.findViewById(R.id.used_box);

                    intent.putExtra("Used", box_passer.isChecked()?"true":"false");

                    box_passer = search_view.findViewById(R.id.unspecified_box);

                    intent.putExtra("Unspecified", box_passer.isChecked()?"true":"false");

                    box_passer = search_view.findViewById(R.id.localpickup_box);

                    intent.putExtra("LocalPickup", box_passer.isChecked()?"true":"false");

                    box_passer = search_view.findViewById(R.id.freeshipping_box);

                    intent.putExtra("FreeShipping", box_passer.isChecked()?"true":"false");

                    box_passer = search_view.findViewById(R.id.nearby_box);

                    intent.putExtra("Nearby", box_passer.isChecked()?"true":"false");

                    EditText miles = search_view.findViewById(R.id.miles_box);

                    String temp_dist;

                    if(nearby_check.isChecked())
                        temp_dist = miles.getText().toString().matches("")?"10":miles.getText().toString();
                    else
                        temp_dist = "0";

                    intent.putExtra("Miles", temp_dist);

                    if(current_check.isChecked())
                        intent.putExtra("Zipcode", final_zip);
                    else
                        intent.putExtra("Zipcode", zip_text.getText().toString());

                    startActivity(intent);

                }
            }
        });
        return search_view;
    }

    private String[] setAutoAdapter(JSONObject response)
    {
        String[] zip_auto_list = new String[5];
        try
        {
            zip_auto_list = new String[response.getJSONArray("postalCodes").length()];

            for(int i = 0; i < response.getJSONArray("postalCodes").length(); i ++)
                zip_auto_list[i] = response.getJSONArray("postalCodes").getJSONObject(i).getString("postalCode");

            return zip_auto_list;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return zip_auto_list;
    }

}
