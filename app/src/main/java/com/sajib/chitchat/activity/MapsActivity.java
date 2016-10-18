package com.sajib.chitchat.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sajib.chitchat.LocationService;
import com.sajib.chitchat.Myapplication;
import com.sajib.chitchat.R;
import com.sajib.chitchat.Timerview;
import com.sajib.chitchat.model.Location;
import com.sajib.chitchat.viewmodel.ChatViewmodel;
import com.sajib.chitchat.viewmodel.DirectionMap_Viewmodel;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private Mapsnap mapsnap;
    private String child;
    private String sender;
    private String recipient;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private CoordinatorLayout coordinatorLayout;
    private android.location.Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private int time;
    private String sendername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapsnap = new ChatViewmodel();

        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        final int dp= (int) displayMetrics.density;

        child = getIntent().getStringExtra("firebasechild");
        sender = getIntent().getStringExtra("firebasesender");
        recipient = getIntent().getStringExtra("firebaserecipient");
        sendername = getIntent().getStringExtra("sendername");

        final Timerview timerview= (Timerview) findViewById(R.id.timer);

        CardView button = (CardView) findViewById(R.id.take_snap);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timerview.getValue()>0) {
                    time=timerview.getValue();
                    Toast.makeText(MapsActivity.this,"time:"+time,Toast.LENGTH_SHORT).show();

                    CaptureMapScreen(time);
                }
                if(timerview.getValue()<=0) {
                    Toast.makeText(MapsActivity.this,"Please select how much time you want to share location",Toast.LENGTH_SHORT).show();
                }


            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Map_linear);

                    final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) linearLayout.getLayoutParams();

                    layoutParams.height = (height-360*dp);

                    linearLayout.setLayoutParams(layoutParams);
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Map_linear);

                    final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) linearLayout.getLayoutParams();
                    layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, layoutParams.MATCH_PARENT, getResources().getDisplayMetrics());
                    linearLayout.setLayoutParams(layoutParams);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                // React to dragging events
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

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
        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);


        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                mMap.clear();
                LatLng mylocation = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                mMap.addMarker(new MarkerOptions().position(mylocation).draggable(true));
                return false;
            }
        });





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();

    }

    public void CaptureMapScreen(int time) {
        if (mLastLocation != null) {
            String Staticmappic = "https://maps.googleapis.com/maps/api/staticmap?center=" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&zoom=15&size=400x200&maptype=roadmap&markers=color:red%7C" + mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude() + "&key=" + Myapplication.MAP_IMAGE_KEY;
            long timestamp=time*60*1000;
            mapsnap.readySnap(mLastLocation.getLatitude(), mLastLocation.getLongitude(), child, sender, recipient, Staticmappic,sendername,timestamp);

            Intent intent = new Intent(MapsActivity.this, ChatActivity.class);
            intent.putExtra("child", child);
            intent.putExtra("sender", sender);
            intent.putExtra("reciepent", recipient);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.MONTH, 7);
            calendar.set(Calendar.YEAR, 2016);
            calendar.set(Calendar.DAY_OF_MONTH, 6);

            calendar.set(Calendar.HOUR_OF_DAY, 14);
            calendar.set(Calendar.MINUTE, 3);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.AM_PM, Calendar.PM);

            Intent myIntent = new Intent(MapsActivity.this, LocationService.class);
            myIntent.putExtra("Stop", "Stop");
            PendingIntent pendingIntent = PendingIntent.getService(MapsActivity.this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time * 60 * 1000, pendingIntent);

            startService(new Intent(MapsActivity.this, LocationService.class));

            startActivity(intent);


        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            onLocationChanged(mLastLocation);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        if(mMap!=null) {
            mMap.clear();
            this.mLastLocation=location;
            LatLng mylocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mylocation).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }
    }


    public interface Mapsnap{
        void readySnap(Double latitude, Double longitude, String child, String sender, String recipient, String Staticmapimage, String sendername, long timestamp);
    }
}
