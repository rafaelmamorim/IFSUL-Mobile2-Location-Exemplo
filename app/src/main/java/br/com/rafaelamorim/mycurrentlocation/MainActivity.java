package br.com.rafaelamorim.mycurrentlocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private LocationManager locationManager;
    private String TAG = "MyCurrentLocation.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            setupLocationServices();
        }
        latitudeTextView = findViewById(R.id.txtViewLatitude);
        longitudeTextView = findViewById(R.id.txtViewLongitude);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitudeTextView.setText(convertLatitudeToDMS(location.getLatitude()));
        longitudeTextView.setText(convertLongitudeToDMS(location.getLongitude()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocationServices();
            } else {
                Toast.makeText(this, "IMPOSSÍVEL DE CONTINUAR. O usuário não forneceu permissão de acesso a localização", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setupLocationServices() {

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (Exception exception){
            Log.e(TAG,"Ocorreu um erro ao executar a função setupLocationServices. O erro foi: " + exception.getMessage());
        }
    }

    //Código gerado pelo Gemini. A culpa é dele se estiver errado :-)
    public static String convertLatitudeToDMS(double latitude) {
        // Extract degrees
        int degrees = (int) Math.floor(latitude);

        // Calculate minutes
        double fractionalPart = latitude - degrees;
        double minutes = fractionalPart * 60;
        int minutesInt = (int) Math.floor(minutes);

        // Calculate seconds
        fractionalPart = minutes - minutesInt;
        double seconds = fractionalPart * 60;
        String secondsStr = String.format("%.2f", seconds); // Format seconds to two decimal places

        // Construct DMS string with cardinal direction (N or S)
        String cardinalDirection = latitude >= 0 ? "N" : "S";
        return String.format("%d° %d' %s\"", degrees, minutesInt, secondsStr, cardinalDirection);
    }

    //Código gerado pelo Gemini. A culpa é dele se estiver errado :-)
    public static String convertLongitudeToDMS(double longitude) {
        // Extract degrees
        int degrees = (int) Math.floor(longitude);

        // Calculate minutes
        double fractionalPart = longitude - degrees;
        double minutes = fractionalPart * 60;
        int minutesInt = (int) Math.floor(minutes);

        // Calculate seconds
        fractionalPart = minutes - minutesInt;
        double seconds = fractionalPart * 60;
        String secondsStr = String.format("%.2f", seconds); // Format seconds to two decimal places

        // Construct DMS string with cardinal direction (E or W)
        String cardinalDirection = longitude >= 0 ? "E" : "W";
        return String.format("%d° %d' %s\"", degrees, minutesInt, secondsStr, cardinalDirection);
    }

}