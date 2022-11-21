package com.example.examen2p;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.examen2p.databinding.ActivityMapaBinding;
public class ActivityMapa extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapaBinding binding;
    private Marker marcador;
    private double lat = 0.0;
    private double lng = 0.0;
    private String nombre,numero,foto,entrada,entrada1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        nombre=getIntent().getStringExtra("nombre");
        numero=getIntent().getStringExtra("numero");
        foto=getIntent().getStringExtra("foto");
        entrada=getIntent().getStringExtra("entrada");
        entrada1=getIntent().getStringExtra("entrada1");
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Intent principal = new Intent(getApplicationContext(), MainActivity.class);
                principal.putExtra("nombre",nombre);
                principal.putExtra("numero",numero);
                principal.putExtra("latitud", String.valueOf(lat));
                principal.putExtra("longitud", String.valueOf(lng));
                principal.putExtra("foto",foto);
                principal.putExtra("entrada",entrada);
                principal.putExtra("entrada1",entrada1);
                startActivity(principal);
                return false;
            }
        });
        ubicacion();
    }
    public void marcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miubicacion = CameraUpdateFactory.newLatLngZoom(coordenadas,16);
        if (marcador != null) {
            marcador.remove();
        }
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Ubicacion actual"));
        mMap.animateCamera(miubicacion);
    }
    public void actualizarubicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            marcador(lat, lng);
        }
    }
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            actualizarubicacion(location);
        }
    };
    public void ubicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarubicacion(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}