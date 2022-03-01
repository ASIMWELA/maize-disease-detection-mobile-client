package com.detection.diseases.maize.ui.modelresults;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.EDiseases;
import com.detection.diseases.maize.ui.modelresults.model.ImageIdsHolder;
import com.detection.diseases.maize.ui.modelresults.model.ModelResults;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelResultsActivity extends AppCompatActivity {
    private ListView lvRecommendations, lvSymptoms;
    private Chip cpBackArrow;
    private TextView tvDiseaseName, tvRecommendations, tvSymptoms;
    private ImageView ivMoreMenu;
    private ViewPager2 viewPager2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_results_actity);
        initViews();
        Gson gson = new Gson();
        ModelResults results = gson.fromJson(getIntent().getStringExtra(AppConstants.MODEL_RESULTS), ModelResults.class);
        tvDiseaseName.setText(results.getDiseaseName());
        if (results.getDiseaseName().equals("Health")) {
            tvRecommendations.setText("Keeping The Crop Healthy");
            tvSymptoms.setText("Signs for a healthy crop");
        }
        ArrayAdapter<String> recommendationAdapter = new ArrayAdapter<>(this,
                R.layout.disease_recommendations_row, results.getPrescriptions());
        ArrayAdapter<String> symptomsAdapter = new ArrayAdapter<>(this,
                R.layout.disease_symptoms_row, results.getSymptoms());

        List<ImageIdsHolder> commonRust = Stream.of(
                new ImageIdsHolder(R.drawable.common_rust_1),
                new ImageIdsHolder(R.drawable.common_rsut_2),
                new ImageIdsHolder(R.drawable.common_rust_3)).collect(Collectors.toList());


        List<ImageIdsHolder> nothernLeafBlight = Stream.of(
                new ImageIdsHolder(R.drawable.blight_1),
                new ImageIdsHolder(R.drawable.blight_2),
                new ImageIdsHolder(R.drawable.blight_3)).collect(Collectors.toList());


        List<ImageIdsHolder> health = Stream.of(
                new ImageIdsHolder(R.drawable.corn_health_1),
                new ImageIdsHolder(R.drawable.corn_health_2),
                new ImageIdsHolder(R.drawable.corn_health_3)).collect(Collectors.toList());


        List<ImageIdsHolder> gray = Stream.of(
                new ImageIdsHolder(R.drawable.gray_leaf_1),
                new ImageIdsHolder(R.drawable.gray_leaf_2),
                new ImageIdsHolder(R.drawable.gray_leaf_3)).collect(Collectors.toList());

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer((page, position) -> {
            page.setTranslationX(-100 * position);
        });

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setOffscreenPageLimit(3);


        SliderAdapter sliderAdapter = null;

        lvSymptoms.setAdapter(symptomsAdapter);
        lvRecommendations.setAdapter(recommendationAdapter);


        if (results.getDiseaseName().equals(EDiseases.COMMON_RUST.label)) {
            sliderAdapter = new SliderAdapter(commonRust, viewPager2);
        }
        if (results.getDiseaseName().equals(EDiseases.GRAY_LEAF_SPOT.label)) {
            sliderAdapter = new SliderAdapter(gray, viewPager2);
        }
        if (results.getDiseaseName().equals(EDiseases.NORTHERN_LEAF_BLIGHT.label)) {
            sliderAdapter = new SliderAdapter(nothernLeafBlight, viewPager2);
        }
        if (results.getDiseaseName().equals(EDiseases.HEALTH.label)) {
            sliderAdapter = new SliderAdapter(health, viewPager2);
        }
        viewPager2.setAdapter(sliderAdapter);
        cpBackArrow.setOnClickListener(v -> onBackPressed());
    }

    private void initViews() {
        lvRecommendations = findViewById(R.id.model_results_lv_recommendations);
        lvSymptoms = findViewById(R.id.model_results_lv_symptoms);
        cpBackArrow = findViewById(R.id.model_results_cp_back);
        tvDiseaseName = findViewById(R.id.model_results_activity_tv_disease_name);
        ivMoreMenu = findViewById(R.id.model_results_iv_more_menu);
        tvRecommendations = findViewById(R.id.model_results_tv_recommendations);
        tvSymptoms = findViewById(R.id.model_results_tv_symptoms);
        viewPager2 = findViewById(R.id.images_preview_pager);
    }

}