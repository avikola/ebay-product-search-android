package com.example.hw9_productsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class SimilarTab extends Fragment
{

    public SimilarTab() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Spinner s1, s2;

    private String[] item_urls;
    private String[] title;
    private String[] shipping_cost;
    private String[] timeLeft;
    private String[] price;
    private String[] img_urls;
    private int total;
    private int[] days;
    private double[] pricer;

    private List<SimilarItem> listSimilar;

    private SimilarViewAdapter simAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View similar_view = inflater.inflate(R.layout.similar_tab_fragment, container, false);

        total = Integer.parseInt(getArguments().getString("Total"));

        item_urls = new String[total];
        title = new String[total];
        shipping_cost = new String[total];
        timeLeft = new String[total];
        price = new String[total];
        img_urls = new String[total];
        days = new int[total];
        pricer = new double[total];

        item_urls = getArguments().getStringArray("ItemURLS");
        title = getArguments().getStringArray("Title");
        shipping_cost = getArguments().getStringArray("ShipCost");
        timeLeft = getArguments().getStringArray("timeLeft");
        price = getArguments().getStringArray("Price");
        img_urls = getArguments().getStringArray("ImageURLS");

        ProgressBar prog_similar = similar_view.findViewById(R.id.progressBar_similar_details);
        TextView prog_text = similar_view.findViewById(R.id.progressBar_similar_details_text);

        listSimilar = new ArrayList<>();
        List<SimilarItem> backup = new ArrayList<>();

        RecyclerView sim_rv = similar_view.findViewById(R.id.sim_recyclerview);
        simAdapter = new SimilarViewAdapter(getContext(), listSimilar);
        sim_rv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        sim_rv.setAdapter(simAdapter);

        s1 = similar_view.findViewById(R.id.first_spinner);
        s2 = similar_view.findViewById(R.id.second_spinner);

        s2.setEnabled(false);

        setDefault();

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Default:
                if(position != 0)
                    s2.setEnabled(true);
                else
                    s2.setEnabled(false);

                if(position == 0)
                    setDefault();

                // Name:
                if(position == 1)
                {
                    if(s2.getSelectedItemPosition()==0)
                        setAscName();
                    else
                        setDescName();
                }

                // Price:
                if(position == 2)
                {
                    if(s2.getSelectedItemPosition()==0)
                        setAscPrice();
                    else
                        setDescPrice();
                }

                // Days:
                if(position == 3)
                {
                    if(s2.getSelectedItemPosition()==0)
                        setAscDays();
                    else
                        setDescDays();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // Asc
                if(position == 0)
                {
                    if(s1.getSelectedItemPosition()==1)
                        setAscName();
                    else if(s1.getSelectedItemPosition()==2)
                        setAscPrice();
                    else if(s1.getSelectedItemPosition()==3)
                        setAscDays();
                }
                // Desc:
                if(position == 1)
                {
                    if(s1.getSelectedItemPosition()==1)
                        setDescName();
                    else if(s1.getSelectedItemPosition()==2)
                        setDescPrice();
                    else if(s1.getSelectedItemPosition()==3)
                        setDescDays();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        prog_text.setVisibility(View.GONE);
        prog_similar.setVisibility(View.GONE);

        return similar_view;
    }

    private void setDefault()
    {
        listSimilar.clear();
        for(int i = 0; i <total; i++)
        {
            String temp = timeLeft[i];
            temp = temp.substring(temp.lastIndexOf("P")+1, temp.lastIndexOf("D"));
            days[i] = Integer.parseInt(temp);
            String temp2 = price[i].replace("$","");
            pricer[i] = Double.parseDouble(temp2);
            listSimilar.add(new SimilarItem(item_urls[i],title[i],shipping_cost[i],days[i],pricer[i],total,img_urls[i]));
        }
        simAdapter.notifyDataSetChanged();
    }

    private void setAscName()
    {
        Collections.sort(listSimilar, new sortListbyAscName());
        simAdapter.notifyDataSetChanged();
    }

    private void setDescName()
    {
        Collections.sort(listSimilar, new sortListbyDescName());
        simAdapter.notifyDataSetChanged();
    }

    private void setAscPrice()
    {
        Collections.sort(listSimilar, new sortListbyAscPrice());
        simAdapter.notifyDataSetChanged();
    }

    private void setDescPrice()
    {
        Collections.sort(listSimilar, new sortListbyDescPrice());
        simAdapter.notifyDataSetChanged();
    }

    private void setAscDays()
    {
        Collections.sort(listSimilar, new sortListbyAscDays());
        simAdapter.notifyDataSetChanged();
    }

    private void setDescDays()
    {
        Collections.sort(listSimilar, new sortListbyDescDays());
        simAdapter.notifyDataSetChanged();
    }

    class sortListbyAscName implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            String n1, n2;
            n1 = o1.getTitle().toLowerCase().trim();
            n2 = o2.getTitle().toLowerCase().trim();
            return n1.compareTo(n2);
        }
    }

    class sortListbyDescName implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            String n1, n2;
            n1 = o1.getTitle().toLowerCase().trim();
            n2 = o2.getTitle().toLowerCase().trim();
            return n2.compareTo(n1);
        }
    }

    class sortListbyAscPrice implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            Double n1, n2;
            n1 = o1.getPrice();
            n2 = o2.getPrice();
            return n1.compareTo(n2);
        }
    }

    class sortListbyDescPrice implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            Double n1, n2;
            n1 = o1.getPrice();
            n2 = o2.getPrice();
            return n2.compareTo(n1);
        }
    }

    class sortListbyAscDays implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            Integer n1, n2;
            n1 = o1.getTimeLeft();
            n2 = o2.getTimeLeft();
            return n1.compareTo(n2);
        }
    }

    class sortListbyDescDays implements Comparator<SimilarItem>
    {
        @Override
        public int compare(SimilarItem o1, SimilarItem o2)
        {
            Integer n1, n2;
            n1 = o1.getTimeLeft();
            n2 = o2.getTimeLeft();
            return n2.compareTo(n1);
        }
    }

}
