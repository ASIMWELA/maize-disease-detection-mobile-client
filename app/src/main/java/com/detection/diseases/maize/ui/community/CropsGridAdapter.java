package com.detection.diseases.maize.ui.community;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CropsGridAdapter extends ArrayAdapter<CropsModel> {
    List<CropsModel> cropsModels;
    List<CropsModel> searchedModels;

    public CropsGridAdapter(@NonNull Context context, List<CropsModel> cropsModels) {
        super(context, 0, cropsModels);
        this.cropsModels = cropsModels;
        searchedModels = new ArrayList<>(cropsModels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_crop_view, parent, false);
        }
        CropsModel cropsModel = getItem(position);
        CircleImageView image = listItemView.findViewById(R.id.image_crop);
        TextView cropName = listItemView.findViewById(R.id.crop_name);
        cropName.setText(cropsModel.getCropName());
        Picasso.get().load(cropsModel.getImageId()).fit().centerCrop().into(image);
        return listItemView;
    }
    public void searchCrop(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        cropsModels.clear();
        if (charText.length() == 0) {
            cropsModels.addAll(searchedModels);
        } else {
            for (CropsModel cropModel : searchedModels) {
                if (cropModel.getCropName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    cropsModels.add(cropModel);
                }
            }
        }
        notifyDataSetChanged();
    }
}


