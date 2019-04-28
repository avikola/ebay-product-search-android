package com.example.hw9_productsearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;


public class PhotosTab extends Fragment {

    public PhotosTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View photos_view;
    private String[] photo_links;
    private final int[] img_views = {R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6, R.id.image_7, R.id.image_8};

    private ProgressBar prog_photos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        photos_view = inflater.inflate(R.layout.photos_tab_fragment, container, false);

        photo_links = getArguments().getStringArray("Photos");

        prog_photos = photos_view.findViewById(R.id.progressBar_photos_details);

        setTheImages();

        return photos_view;
    }

    private void setTheImages()
    {
        if(photo_links!=null && photo_links.length>0)
        {
            int count = photo_links.length;

            if(photo_links[0].matches("null"))
            {
                photos_view.findViewById(R.id.photos_error).setVisibility(View.VISIBLE);
            }
            else
            {
                for(int i = 0; i < count; i++)
                {
                    ImageView setit = photos_view.findViewById(img_views[i]);
                    Picasso.get().load(photo_links[i]).into(setit);
                    setit.setVisibility(View.VISIBLE);
                }
            }

            prog_photos.setVisibility(View.GONE);
            photos_view.findViewById(R.id.progressBar_photos_details_text).setVisibility(View.GONE);
        }
    }
}
