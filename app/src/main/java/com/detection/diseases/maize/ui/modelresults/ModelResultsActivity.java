package com.detection.diseases.maize.ui.modelresults;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.detection.diseases.maize.R;
import com.detection.diseases.maize.helpers.AppConstants;
import com.detection.diseases.maize.ui.modelresults.model.ModelResults;
import com.google.android.material.chip.Chip;
import com.google.gson.Gson;

public class ModelResultsActivity extends AppCompatActivity {
    private ListView lvRecommendations, lvSymptoms;
    private Chip cpBackArrow;
    private TextView tvDiseaseName, tvRecommendations, tvSymptoms;
    private ImageView ivMoreMenu;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_results_actity);
        initViews();
        Gson gson = new Gson();
        ModelResults results = gson.fromJson(getIntent().getStringExtra(AppConstants.MODEL_RESULTS), ModelResults.class);
        tvDiseaseName.setText(results.getDiseaseName());
        if(results.getDiseaseName().equals("Health")){
            tvRecommendations.setText("Keeping The Crop Healthy");
            tvSymptoms.setText("Signs for a healthy crop");
        }
        ArrayAdapter<String> recommendationAdapter = new ArrayAdapter<>(this,
                R.layout.disease_recommendations_row, results.getPrescriptions());
        ArrayAdapter<String> symptomsAdapter = new ArrayAdapter<>(this,
                R.layout.disease_symptoms_row, results.getSymptoms());

        //set adapters
        lvSymptoms.setAdapter(symptomsAdapter);
        lvRecommendations.setAdapter(recommendationAdapter);

        cpBackArrow.setOnClickListener(v->onBackPressed());
    }

    private void initViews(){
        lvRecommendations = findViewById(R.id.model_results_lv_recommendations);
        lvSymptoms = findViewById(R.id.model_results_lv_symptoms);
        cpBackArrow  = findViewById(R.id.model_results_cp_back);
        tvDiseaseName = findViewById(R.id.model_results_activity_tv_disease_name);
        ivMoreMenu = findViewById(R.id.model_results_iv_more_menu);
        tvRecommendations = findViewById(R.id.model_results_tv_recommendations);
        tvSymptoms = findViewById(R.id.model_results_tv_symptoms);
    }
}