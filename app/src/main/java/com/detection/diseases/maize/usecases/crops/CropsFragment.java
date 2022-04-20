package com.detection.diseases.maize.usecases.crops;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.detection.diseases.maize.R;
import com.detection.diseases.maize.usecases.camera.CameraActivity;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import lombok.SneakyThrows;

/**
 * @author Augustine
 *
 * A Fragment responsible for handling all the logic in the Crops sections. Allows to open a Camera activity
 * to capture and upload maize leaves
 */
public class CropsFragment extends Fragment implements CropFragmentContract.View {

    /**
     * The presenter of this class responsible for updating the view
     */
    private CropsPresenter cropsPresenter;

    FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvTemperature,
            tvLocationName,
            tvWeatherDescription, tvSunSet, tvCelcisSymbol, tvDegSymbol;
    private ProgressBar progressBar;

    String temp, loc, sunSet, weatherDes;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_crops, container, false);
        /**
         *
         */
        AppCompatButton btnOpenCamera = root.findViewById(R.id.fg_crops_btn_open_camera);
        tvTemperature = root.findViewById(R.id.tv_tempearture);
        tvLocationName = root.findViewById(R.id.tv_location_name_date);
        tvSunSet = root.findViewById(R.id.tv_sun_set);
        tvWeatherDescription = root.findViewById(R.id.tv_description);
        progressBar = root.findViewById(R.id.pg_get_weather_data);
        tvCelcisSymbol = root.findViewById(R.id.tv_Celcius_symbol);
        tvDegSymbol = root.findViewById(R.id.tv_deg_symbol);



        cropsPresenter = new CropsPresenter(this, requireContext());
        btnOpenCamera.setOnClickListener(v -> cropsPresenter.openCameraBtnClicked());
        fusedLocationProviderClient = new FusedLocationProviderClient(requireContext());


        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }else{
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {

              if(location != null){
                  double lat = location.getLatitude();
                  double lan = location.getLongitude();
                  if(savedInstanceState == null){
                      cropsPresenter.getLocation(lat, lan);
                  }
              }

            });

        }


        return root;
    }

    /**
     * Opens a camera a camera activity
     *
     * @see CameraActivity
     */
    @Override
    public void onOpenCamera() {
        Intent intent = new Intent(requireActivity(), CameraActivity.class);
        startActivity(intent);
    }

    /**
     * Invoked when location access is given
     *
     * @param locationProviderClient A fused location privider client. provides a call back that gives access to location as per required
     */
    @Override
    public void onLocationAccess(FusedLocationProviderClient locationProviderClient) {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestLocationAccess();
                return;
            }


    }

    /**
     * Request permission for location acess
     *
     * @return true if permission is granted false otherwise
     */
    public boolean requestLocationAccess() {
        int extstorePermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int torePermission = ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (extstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(torePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), listPermissionsNeeded
                            .toArray(new String[0]),
                    100);
            return false;
        }
        setRetainInstance(true);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){

        }else {
            onLocationAccessDenied();
        }

    }

    /**
     * Fired when access to location is denied
     */
    @Override
    public void onLocationAccessDenied() {
        Toast.makeText(requireContext(), "Location access denied", Toast.LENGTH_SHORT).show();

    }

    /**
     * Fired when get weather data is successful
     *
     * @param response JSONObject containing weather data
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    @SneakyThrows
    public void onGetDataSucess(JSONObject response) {
        JSONObject description = (JSONObject) response.getJSONArray("weather").get(0);

        long seconds = response.getJSONObject("sys").getLong("sunset");
        String locationName = response.getString("name");

        Calendar calendar = Calendar.getInstance();
        String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());




        temp = response.getJSONObject("main").getString("temp");
        weatherDes =  description.getString("description");
        loc = locationName + ", "+  calendar.get(Calendar.DAY_OF_MONTH)+" "+month;

        Instant u = Instant.ofEpochSecond(seconds);
        int h = u.atOffset(ZoneOffset.UTC).getHour()+2;
        int m = u.atOffset(ZoneOffset.UTC).getMinute();
        sunSet = "Sun sets at: "+h+":"+m;
        tvSunSet.setText(sunSet);
        tvWeatherDescription.setText(weatherDes);
        tvLocationName.setText(loc);
        tvTemperature.setText(temp);

        tvCelcisSymbol.setVisibility(View.VISIBLE);
        tvDegSymbol.setVisibility(View.VISIBLE);

    }

    /**
     * Invoked when get data request is not sucessful
     *
     * @param error A Volley object
     */
    @Override
    public void onDataError(VolleyError error) {
        Toast.makeText(requireContext(), "Error "+error, Toast.LENGTH_SHORT).show();

    }

    /**
     * Show loading for getting weather data request
     */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hide loading for getting weather data request
     */
    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("temp", temp);
        outState.putString("sunSet", sunSet);
        outState.putString("weather", weatherDes);
        outState.putString("loc", loc);
    }

    
}