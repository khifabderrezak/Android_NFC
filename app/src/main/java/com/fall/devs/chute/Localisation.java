package com.fall.devs.chute;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;
import java.util.Locale;

public class Localisation extends AppCompatActivity {


    private String adresse;
    private Button idbutton;
    //private LocationManager locationManager;//faut pas toucher
    private LocationListener listener;//faut toucher
    private static String TAG="Localisation";//faut pas toucher




    public  String add(LocationManager locationManager) {


        //locationManager.requestLocationUpdates("gps",5000, 0, listener);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    Log.d("LOCATION CHANGED", location.getLatitude() + "");
                    Log.d("LOCATION CHANGED", location.getLongitude() + "");
                }
                //Récupérer l'adresse à partir des coordonnées (géocodage inverse)
                try{
                    Geocoder geo = new Geocoder(Localisation.this.getApplicationContext(), Locale.getDefault());
                    List<Address> addresses = geo.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                    if (addresses.isEmpty()) {
                        System.out.println("En attente de coordonnées!");//si ça vous signale une erreur vous pouvez remplacer par un System.out.println
                    }
                    else {
                        if (addresses.size() > 0) {
                            //C'est ça la chaine de caractère de l'adresse, il suffit de la récupérer dans un string
                            //Au lieu de t2.append(addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            //Mettez String adresse=addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                            adresse= (addresses.get(0).getFeatureName() + "," + addresses.get(0).getLocality() +"," + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());

                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
        return  adresse;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button(){

        // Demande de permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
    }
}

