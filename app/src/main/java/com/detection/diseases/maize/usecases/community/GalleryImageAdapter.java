package com.detection.diseases.maize.usecases.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.usecases.community.model.GalleryImageModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
/**
 * @author Augustine
 *
 *An adapter to load galley images into a grid layout
 */
public class GalleryImageAdapter extends ArrayAdapter<GalleryImageModel> {
    /**
     * A constructor for creating the object
     *
     * @param context Context where the adapater gets initialised
     *
     * @param galleryImageModels List of {@link GalleryImageModel}
     */
    public GalleryImageAdapter(@NonNull Context context, List<GalleryImageModel> galleryImageModels) {
        super(context, 0, galleryImageModels);
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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.gallery_image_view, parent, false);
        }
        GalleryImageModel galleryImageModel = getItem(position);
        RoundedImageView image = listItemView.findViewById(R.id.gallery_image_id);
        Picasso.get().load(galleryImageModel.getImage()).fit().centerCrop().into(image);
        return listItemView;
    }

}