package ir.parsijoo.map.parsijoomapviewer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.NetworkResponse;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import ir.parsijoo.map.android.Viewer;

public class MainActivity extends AppCompatActivity {
    public Viewer viewer;
    public Marker first_location_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewer = findViewById(R.id.mapview);
        MapView mapView = viewer.getMapView();
        ArrayList<HashMap<String, Double>> middle_points = new ArrayList<HashMap<String, Double>>();
        HashMap<String,Double> point = new HashMap<>();
        point.put("lat",31.90862139692772);
        point.put("lon",54.36522600097642);
        middle_points.add(point);
        viewer.getDirection(31.870725879312808,54.397498339843594,31.936595766279076,54.32059404296901,middle_points,new Viewer.CallBackDirection() {
            @Override
            public void onResponse(String wkt, float totalDistance, int totalTime, ArrayList<HashMap<String, String>> instructionList) {
                System.out.println(instructionList);
            }
            @Override
            public void onError(NetworkResponse networkResponse) {
                try {
                    String body;
                    body = new String(networkResponse.data,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
