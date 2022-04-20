package com.detection.diseases.maize.usecases.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.usecases.community.model.CropsModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Augustine
 *
 * A adapter class responsible for adding crops nmes and images
 * to a pop dialog so that users can just select the crops
 */
public class CropsGridAdapter extends ArrayAdapter<CropsModel> {
    /**
     * Actual crops models
     */
    List<CropsModel> cropsModels;
    /**
     * Crops model that get left after filtering based on search criteria
     */
    List<CropsModel> searchedModels;

    /**
     * A constuctor for creating objects
     *
     * @param context the context where the adapter gets initialised
     *
     * @param cropsModels List of {@link CropsModel}
     */
    public CropsGridAdapter(@NonNull Context context, List<CropsModel> cropsModels) {
        super(context, 0, cropsModels);
        this.cropsModels = cropsModels;
        searchedModels = new ArrayList<>(cropsModels);
    }

    /**
     * Inflate a single item view of the grid
     *
     * @param position The position of a single item. Helps to set selection listeners
     *
     * @param convertView The view that is returned after inflating it with the xml layout
     *
     * @param parent The parent view group
     *
     * @return View
     */
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

    /**
     * Search the crops model based on text. Filters out those that dont meet the criteteria
     *
     * @param charText Text to be search. Search by crops or A user who created the issue
     */
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


