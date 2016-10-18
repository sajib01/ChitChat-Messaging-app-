package com.sajib.chitchat.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.VectorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.ui.IconGenerator;
import com.sajib.chitchat.Myapplication;
import com.sajib.chitchat.R;
import com.sajib.chitchat.Retrofitservice;
import com.sajib.chitchat.Utility.DecodePoly;
import com.sajib.chitchat.activity.DirectionMap;
import com.sajib.chitchat.model.User;
import com.sajib.chitchat.model.drawmapmodel.DRAWMAP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by sajib on 5/9/16.
 */
public class DirectionMap_Viewmodel implements DirectionMap.Donetworkcall {
    public final ObservableField<String> setTravelTime = new ObservableField<>("Time:");
    public final ObservableField<String> setDistance = new ObservableField<>("Distance");
    private GoogleMap mMap;
    private Context context;
    private DRAWMAP drawmap;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthlistener;
    private Double CursorLat;
    private Double CursorLong;
    private DatabaseReference mRef;
    private PolylineOptions lineOptions = null;
    private PolylineOptions polylineOptions=null;
    private Double Originlatitude;
    private Double Originlongitude;
    private String Sender;
    private String recipient;
    private String senderName;
    private String recipientName;
    private ArrayList<LatLng> points = null;
    private ArrayList<LatLng> Travelpoint=null;
    private boolean fetched=false;

    public DirectionMap_Viewmodel(DirectionMap directionMap, FirebaseAuth firebaseAuth, FirebaseAuth.AuthStateListener mAuthlistener, double latitude, double longitude, String sender, final String recipient) {
        this.context = directionMap;
        this.firebaseAuth = firebaseAuth;
        this.mAuthlistener = mAuthlistener;
        this.Originlatitude = latitude;
        this.Originlongitude = longitude;
        this.Sender = sender;
        this.recipientName=recipient;
        polylineOptions=new PolylineOptions();
        Travelpoint=new ArrayList<>();
        final FirebaseDatabase databas= FirebaseDatabase.getInstance();
        DatabaseReference myRef = databas.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);

                    if(snapshot.getKey().equals(Sender)) {
                        senderName= user.getmName();

                    }
                    if(snapshot.getKey().equals(recipient)) {
                        recipientName= user.getmName();

                    }

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPolyline(final double Destinationlatitude, final double Destinationlongitude) {
        Map<String, String> mapdata = new HashMap<>();
        mapdata.put("origin", this.Originlatitude + "," + this.Originlongitude);
        mapdata.put("destination", Destinationlatitude + "," + Destinationlongitude);
        mapdata.put("mode", "driving");
        mapdata.put("key", Myapplication.MAP_KEY);
        Retrofitservice retrofitservice = Retrofitservice.Factory.create();
        Subscription subscription = retrofitservice.getMapdata(mapdata)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DRAWMAP>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DRAWMAP drawmap) {
                        DirectionMap_Viewmodel.this.drawmap = drawmap;
                        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
                        /** Traversing all steps */
                        for (int i = 0; i < drawmap.getRoutes().size(); i++) {
                            List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();
                            /** Traversing all legs */
                            for (int j = 0; j < drawmap.getRoutes().get(i).getLegs().size(); j++) {

                                /** Traversing all steps */
                                for (int k = 0; k < drawmap.getRoutes().get(i).getLegs().get(j).getSteps().size(); k++) {
                                    String polyline = "";
                                    polyline = drawmap.getRoutes().get(i).getLegs().get(j).getSteps().get(k).getPolyline().getPoints();
                                    List<LatLng> list = new DecodePoly().decodePoly(polyline);
                                    setTravelTime.set("Time: " + drawmap.getRoutes().get(i).getLegs().get(j).getDuration().getText());
                                    setDistance.set("Distance: " + drawmap.getRoutes().get(i).getLegs().get(j).getDistance().getText());
                                    /** Traversing all points */
                                    for (int l = 0; l < list.size(); l++) {
                                        HashMap<String, String> hm = new HashMap<String, String>();
                                        hm.put("lat", Double.toString(((LatLng) list.get(l)).latitude));
                                        hm.put("lng", Double.toString(((LatLng) list.get(l)).longitude));
                                        path.add(hm);
                                    }
                                }
                                routes.add(path);
                            }
                        }
                        for (int i = 0; i < routes.size(); i++) {
                            points = new ArrayList<LatLng>();
                            lineOptions = new PolylineOptions();
                            // Fetching i-th route
                            List<HashMap<String, String>> path = routes.get(i);
                            // Fetching all the points in i-th route
                            for (int j = 0; j < path.size(); j++) {
                                HashMap<String, String> point = path.get(j);

                                double lat = Double.parseDouble(point.get("lat"));
                                double lng = Double.parseDouble(point.get("lng"));
                                LatLng position = new LatLng(lat, lng);
                                points.add(position);
                            }
                            // Adding all the points in the route to LineOptions
                            lineOptions.addAll(points);
                            lineOptions.width(15);
                            lineOptions.clickable(true);
                            lineOptions.color(Color.BLUE);


                        }
                        LatLng location = new LatLng(Destinationlatitude, Destinationlongitude);
                        // Drawing polyline in the Google Map for the i-th route
                        mMap.addPolyline(lineOptions);
                        IconGenerator iconGenerator=new IconGenerator(context);
                        iconGenerator.setStyle(IconGenerator.STYLE_ORANGE);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Destinationlatitude,Destinationlongitude))
                                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(recipientName))));

                    }
                });

    }

    public void getLocationUpdateFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Location");
        mRef.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                com.sajib.chitchat.model.Location location = dataSnapshot.getValue(com.sajib.chitchat.model.Location.class);
                if (dataSnapshot.getKey().equals(Sender)) {

                    CursorLat = location.getmLat();
                    CursorLong = location.getmLong();
                    mMap.clear();
                    if (lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }
                    LatLng addedlocation = new LatLng(CursorLat, CursorLong);
                    Travelpoint.add(addedlocation);
                    IconGenerator iconGenerator=new IconGenerator(context);
                    iconGenerator.setStyle(IconGenerator.STYLE_BLUE);
                    mMap.addMarker(new MarkerOptions()
                            .position(addedlocation)
                            .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(senderName))));

                }

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                com.sajib.chitchat.model.Location location = dataSnapshot.getValue(com.sajib.chitchat.model.Location.class);
                if (dataSnapshot.getKey().equals(Sender)) {
                    CursorLat = location.getmLat();
                    CursorLong = location.getmLong();
                    mMap.clear();
                    if (lineOptions != null) {
                        mMap.addPolyline(lineOptions);
                    }

                    LatLng changelocation = new LatLng(CursorLat, CursorLong);
                    Travelpoint.add(changelocation);
                    redrawLine(changelocation,location);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void Calling(Location location) {
        if(!fetched) {
            getPolyline(location.getLatitude(), location.getLongitude());
            getLocationUpdateFromFirebase();
            fetched=true;
        }
    }

    @Override
    public void MapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void redrawLine(LatLng changelocation, com.sajib.chitchat.model.Location location){

        mMap.clear();  //clears all Markers and Polylines

        PolylineOptions options = new PolylineOptions().width(8).color(Color.GRAY).geodesic(true).clickable(true);
        for (int i = 0; i < Travelpoint.size(); i++) {
            LatLng point = Travelpoint.get(i);
            options.add(point);
        }
        mMap.addMarker(new MarkerOptions()
                .position(changelocation)
                .title(senderName)
                .rotation(location.getMbearing().floatValue())
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmap((VectorDrawable) ContextCompat.getDrawable(context,R.drawable.compass)))));
        polylineOptions.addAll(Travelpoint);
        mMap.addPolyline(options); //add Polyline
    }


}
