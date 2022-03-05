package com.detection.diseases.maize.ui.community.payload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.ui.community.model.CropsModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CropsGridAdapter extends ArrayAdapter<CropsModel> {
    public CropsGridAdapter(@NonNull Context context, List<CropsModel> cropsModels) {
        super(context, 0, cropsModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_crop_view, parent, false);
        }
        CropsModel cropsModel = getItem(position);
        RoundedImageView image = listItemView.findViewById(R.id.image_crop);
        TextView cropName = listItemView.findViewById(R.id.crop_name);
        cropName.setText(cropsModel.getCropName());
        Picasso.get().load(cropsModel.getImageId()).fit().centerCrop().into(image);
        return listItemView;
    }

}
