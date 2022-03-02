package com.detection.diseases.maize.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.model.GalleryImageModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryImageAdapter extends ArrayAdapter<GalleryImageModel> {
    public GalleryImageAdapter(@NonNull Context context, List<GalleryImageModel> galleryImageModels) {
        super(context, 0, galleryImageModels);
    }
 
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_image_view, parent, false);
        }
        GalleryImageModel galleryImageModel = getItem(position);
        RoundedImageView image = listitemView.findViewById(R.id.gallery_image_id);
        Picasso.get().load(galleryImageModel.getImage()).fit().centerCrop().into(image);
        return listitemView;
    }


}