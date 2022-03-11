package com.detection.diseases.maize.ui.modelresults;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.helpers.EDiseases;
import com.detection.diseases.maize.ui.community.CreateAnIssueActivity;
import com.detection.diseases.maize.ui.modelresults.model.HighConfidenceDisease;
import com.detection.diseases.maize.ui.modelresults.model.ImageIdsHolder;
import com.detection.diseases.maize.ui.modelresults.model.LowConfidenceDisease;
import com.detection.diseases.maize.ui.modelresults.model.ModelResponse;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelResultHelper extends AppCompatActivity {

    private ViewPager2 vpClass1, vpClass2;
    private Button btnOpenCreateIssue;
    private Chip cpSelectClass1, cpSelectClass2;
    private Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_result_helper);
        initViews();
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

        ModelResponse modelResponse = gson.fromJson(
                getIntent().getStringExtra(AppConstants.MODEL_RESPONSE_RESULTS),
                ModelResponse.class);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer((page, position) -> {
            page.setTranslationX(-100 * position);
        });

        //class 1 viewpager
        vpClass1.setClipToPadding(false);
        vpClass1.setClipChildren(false);
        vpClass1.setPageTransformer(compositePageTransformer);
        vpClass1.setOffscreenPageLimit(3);
        SliderAdapter sliderAdapter1 = null;

        HighConfidenceDisease highConfidenceDisease = modelResponse.getFirstDisease();

        if (highConfidenceDisease.getDiseaseName().equals(EDiseases.COMMON_RUST.label)) {
            sliderAdapter1 = new SliderAdapter(commonRust, vpClass1);
        }
        if (highConfidenceDisease.getDiseaseName().equals(EDiseases.GRAY_LEAF_SPOT.label)) {
            sliderAdapter1 = new SliderAdapter(gray, vpClass1);
        }
        if (highConfidenceDisease.getDiseaseName().equals(EDiseases.NORTHERN_LEAF_BLIGHT.label)) {
            sliderAdapter1 = new SliderAdapter(nothernLeafBlight, vpClass1);
        }
        if (highConfidenceDisease.getDiseaseName().equals(EDiseases.HEALTH.label)) {
            sliderAdapter1 = new SliderAdapter(health, vpClass1);
        }
        vpClass1.setAdapter(sliderAdapter1);



        //class 2 view pagers
        vpClass2.setClipToPadding(false);
        vpClass2.setClipChildren(false);
        vpClass2.setPageTransformer(compositePageTransformer);
        vpClass2.setOffscreenPageLimit(3);
        SliderAdapter sliderAdapter2 = null;

        LowConfidenceDisease lowConfidenceDisease = modelResponse.getSecondDisease();

        if (lowConfidenceDisease.getDiseaseName().equals(EDiseases.COMMON_RUST.label)) {
            sliderAdapter2 = new SliderAdapter(commonRust, vpClass2);
        }
        if (lowConfidenceDisease.getDiseaseName().equals(EDiseases.GRAY_LEAF_SPOT.label)) {
            sliderAdapter2 = new SliderAdapter(gray, vpClass2);
        }
        if (lowConfidenceDisease.getDiseaseName().equals(EDiseases.NORTHERN_LEAF_BLIGHT.label)) {
            sliderAdapter2 = new SliderAdapter(nothernLeafBlight, vpClass2);
        }
        if (lowConfidenceDisease.getDiseaseName().equals(EDiseases.HEALTH.label)) {
            sliderAdapter2 = new SliderAdapter(health, vpClass2);
        }
        vpClass2.setAdapter(sliderAdapter2);


        cpSelectClass1.setOnClickListener(v->{
            Intent intent = new Intent(this, ModelResultsActivity.class);
            intent.putExtra(AppConstants.MODEL_RESULTS, highConfidenceDisease.toString());
            intent.putExtra(AppConstants.ACTIVITY_SOURCE, AppConstants.FIRST_DISEASE_SELECTION_HELPER);
            Toast.makeText(this, "Thanks for your help", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });

        cpSelectClass2.setOnClickListener(v->{
            Intent intent = new Intent(this, ModelResultsActivity.class);
            intent.putExtra(AppConstants.MODEL_RESULTS, lowConfidenceDisease.toString());
            intent.putExtra(AppConstants.ACTIVITY_SOURCE, AppConstants.SECOND_DISEASE_SELECTION_HELPER);
            Toast.makeText(this, "Thanks for your help", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            finish();
        });

        btnOpenCreateIssue.setOnClickListener(v->{
            Intent i = new Intent(this, CreateAnIssueActivity.class);
            startActivity(i);
            finish();
        });


    }



    private void initViews(){
        vpClass1 = findViewById(R.id.model_helper_view_pager_class_1);
        vpClass2 = findViewById(R.id.model_helper_view_pager_class_2);
        btnOpenCreateIssue = findViewById(R.id.btn_model_results_helper_open_create_issue);
        cpSelectClass1 = findViewById(R.id.model_helper_view_pager_chip_selecte_class_1);
        cpSelectClass2 = findViewById(R.id.model_helper_view_pager_chip_selecte_class_2);

    }
}