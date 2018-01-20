package ir.parsijoo.map.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.osmdroid.config.Configuration;
import org.osmdroid.config.DefaultConfigurationProvider;
import org.osmdroid.config.IConfigurationProvider;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.parsijoo.map.viewer.R;

/**
 * Created by mhkyazd on 12/30/2017.
 */

public class Viewer extends RelativeLayout {
    private Context context;
    private MapView mapView;
    private TilesOverlay hibridTileOverlay = null;
    public MapTileProviderBasic mapTileProviderBasicParsijoo;
    public ItemizedIconOverlay<OverlayItem> itemizedIconOverlay;
    private String api_key;
    private CallBackAddress callBackAddress;
    private CallBackSearch callBackSearch;
    private CallBackDirection callBackDirection;

    public Viewer(Context context) {
        super(context);
        this.context = context;
        initializer(context,null,0);
    }

    public Viewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializer(context,attrs,0);
    }

    public Viewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializer(context,attrs,defStyleAttr);
    }

    private void initializer(Context context,AttributeSet attrs, int defStyleAttr) {
        View view= inflate(context, R.layout.viewer,this);
        mapView = view.findViewById(R.id.mapviewosm);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomViewer, defStyleAttr, 0);
        this.api_key = a.getString(R.styleable.CustomViewer_api_key);
        mapView.setTilesScaledToDpi(true);
        Configuration.getInstance().setMapViewHardwareAccelerated(true);
        Configuration.getInstance().setUserAgentValue(context.getPackageName());
        setConfigZoom(false);
        mapView.getController().setCenter(new GeoPoint(31.8974, 54.3569));
        mapView.getController().setZoom(4);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(3);
        setConfigMultiTouch(true);
        //parsijooHibridTileOverllay();
        /*mapView.setMaxZoomLevel(19);
        hibridTileOverlay.setEnabled(false);*/
        parsijooTileProvider();
        addPointLayer();

    }

    public MapView getMapView(){
        return mapView;
    }

    public void setConfigZoom(Boolean bool) {
        this.mapView.setBuiltInZoomControls(bool);
    }

    public void setConfigMultiTouch(Boolean bool) {
        this.mapView.setMultiTouchControls(bool);
    }

    public void parsijooHibridTileOverllay() {
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(context);
        //tileProvider.setTileRequestCompleteHandler(new HibridOverlaySimpleInvalidationHandler(mapView,this));
        ITileSource tileSource = new XYTileSource("Hibrid", 3, 17, 256, "",
                new String[]{
                        context.getString(R.string.base_tile_url_a)+"GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_b)+"GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_c)+"GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_d)+"GetHibrid?imageID=p_",
                        context.getString(R.string.base_tile_url_e)+"GetHibrid?imageID=p_",
                }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                return getBaseUrl() + aTile.getX() + "_" + aTile.getY() + "_" + aTile.getZoomLevel();
            }
        };
        tileProvider.setTileSource(tileSource);
        hibridTileOverlay = new TilesOverlay(tileProvider, context);
        hibridTileOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mapView.getOverlays().add(hibridTileOverlay);
        mapView.invalidate();
    }
    public void parsijooTileProvider() {
        mapTileProviderBasicParsijoo = new MapTileProviderBasic(context);
        mapTileProviderBasicParsijoo.setTileRequestCompleteHandler(new SimpleInvalidationHandler(mapView));
        Configuration.getInstance().getAdditionalHttpRequestProperties().put("api-key",api_key);
        Configuration.getInstance().setDebugTileProviders(true);
        OnlineTileSourceBase parsijoo = new OnlineTileSourceBase("Road", 3, 19, 256, "",
                new String[]{
                        "http://developers.parsijoo.ir/web-service/v1/map/?type=tile"
                }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                //System.out.println("sysosout " +getBaseUrl() + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel()+"&api-key="+api_key);
                return getBaseUrl() + "&x=" + aTile.getX() + "&y=" + aTile.getY() + "&z=" + aTile.getZoomLevel();
            }
        };
        mapTileProviderBasicParsijoo.setTileSource(parsijoo);
        mapView.setTileProvider(mapTileProviderBasicParsijoo);
    }
    public Marker addMerker(double y,double x){
        Marker marker = new Marker(mapView);
        marker.setPosition( new GeoPoint(y, x));
        marker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        mapView.getOverlays().add(marker);
        return marker;
    }
    public void removeMarker(Marker marker){
        mapView.getOverlays().remove(marker);
        mapView.invalidate();
    }
    private void addPointLayer() {
        ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
        itemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(context, overlayArray, null);
        mapView.getOverlays().add(itemizedIconOverlay);
    }
    public void clearAllMarkers(){
        itemizedIconOverlay.removeAllItems();
    }
    public void getAddress(final double y, final double x, final CallBackAddress callBackAddress){
        this.callBackAddress = callBackAddress;
        String url = "http://developers.parsijoo.ir/web-service/v1/map/?type=address&x="+x+"&y="+y;
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        JsonObject resultObj = rootObj.getAsJsonObject("result");
                        resultObj = resultObj.getAsJsonObject();
                        HashMap<String,String> result = new HashMap<>();
                        result.put("state",resultObj.get("state").getAsString());
                        result.put("county",resultObj.get("county").getAsString());
                        result.put("city",resultObj.get("city").getAsString());
                        result.put("region",resultObj.get("region").getAsString());
                        result.put("zone",resultObj.get("zone").getAsString());
                        result.put("district",resultObj.get("district").getAsString());
                        result.put("village",resultObj.get("village").getAsString());
                        result.put("other",resultObj.get("other").getAsString());
                        result.put("ways",resultObj.get("ways").getAsString());
                        callBackAddress.onResponse(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                callBackAddress.onError(error.networkResponse);
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    System.out.println("sysosout " +body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("x",String.valueOf(x));
                params.put("y",String.valueOf(y));
                System.out.println("sysosout " +params);
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                    params.put("api-key",api_key);
                return params;
            };
        };
        // Add the request to the RequestQueue.

        queue.add(stringRequest);
    }

    public static interface CallBackAddress{
        public void onResponse(HashMap<String, String> resultAddress);
        public void onError(NetworkResponse networkResponse);
    }

    public void getSearch(String q, final CallBackSearch callBackSearch){
        doSearch(q,null,0,0,callBackSearch);
    }

    public void getSearch(String q,String city,int page, final CallBackSearch callBackSearch){
        doSearch(q,city,page,0,callBackSearch);
    }

    public void getSearch(String q,String city,int page, int nrpp , final CallBackSearch callBackSearch){
        doSearch(q,city,page,nrpp,callBackSearch);
    }

    public void doSearch(String q,String city,int page, int nrpp , final CallBackSearch callBackSearch){
        this.callBackSearch = callBackSearch;
        String query = "";
        try {
            query = URLEncoder.encode(q, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "http://developers.parsijoo.ir/web-service/v1/map/?type=search&q="+query;
        url = (city != null) ? url+"&city="+city:url;
        url = (page > 0) ? url+"&page="+page:url;
        url = (nrpp > 0) ? url+"&nrpp="+nrpp:url;
        System.out.println("sysosout url " +url);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        JsonObject resultObj = rootObj.getAsJsonObject("result");
                        int resultNumber = resultObj.get("resultNumber").getAsInt();
                        JsonArray itemsJsonArray = resultObj.getAsJsonArray("items");
                        ArrayList<HashMap<String, String>> itemsArray = new ArrayList<HashMap<String, String>>();
                        for (int i = itemsJsonArray.size() - 1; i >= 0; i--) {
                            JsonElement itemJsonElement = itemsJsonArray.get(i);
                            JsonObject itemJsonObject = itemJsonElement.getAsJsonObject();
                            HashMap<String,String> result = new HashMap<>();
                            result.put("title",itemJsonObject.get("title").getAsString());
                            result.put("longitude",itemJsonObject.get("longitude").getAsString());
                            result.put("latitude",itemJsonObject.get("latitude").getAsString());
                            result.put("zoom",itemJsonObject.get("zoom").getAsString());
                            result.put("type",itemJsonObject.get("type").getAsString());
                            result.put("name",itemJsonObject.get("name").getAsString());
                            result.put("address",itemJsonObject.get("address").getAsString());
                            itemsArray.add(result);
                        }
                        callBackSearch.onResponse(resultNumber,itemsArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                callBackSearch.onError(error.networkResponse);
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    System.out.println("sysosout " +body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("api-key",api_key);
                return params;
            };
        };
        // Add the request to the RequestQueue.

        queue.add(stringRequest);
    }

    public static interface CallBackSearch{
        public void onResponse(int resultNumber, ArrayList<HashMap<String, String>> arrayListItems);
        public void onError(NetworkResponse networkResponse);
    }

    public void getDirection(double lat1, double lon1, double lat2, double lon2, List<HashMap<String,Double >> middle_points, final CallBackDirection callBackDirection){

        this.callBackDirection = callBackDirection;
        String p ;
        Map params = new HashMap<>();
        params.put("lat1",lat1+"");
        params.put("lon1",lon1+"");
        params.put("lat2",lat2+"");
        params.put("lon2",lon2+"");
        List<Map> temp_middle_points_array = new ArrayList<>();
        for (int i = 0; i <middle_points.size() ; i++) {
            Map temp_middle_points = new HashMap<>();
            temp_middle_points.put("lat",middle_points.get(i).get("lat")+"");
            temp_middle_points.put("lon",middle_points.get(i).get("lon")+"");
            temp_middle_points_array.add(temp_middle_points);
        }
        params.put("points",temp_middle_points_array);
        Gson gson = new Gson();
        p = gson.toJson(params);
        try {
            p = URLEncoder.encode(p, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "http://developers.parsijoo.ir/web-service/v1/map/?type=direction&p="+p;
        System.out.println("sysosout url " +url);
        RequestQueue queue = Volley.newRequestQueue(context);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String wkt = "";
                        float totalDistance = (float) 0.0;
                        int totalTime = 0;
                        ArrayList<HashMap<String, String>> instructionList = new ArrayList<>();
                        JsonParser parser = new JsonParser();
                        JsonObject rootObj = parser.parse(response).getAsJsonObject();
                        if(rootObj.getAsJsonArray("result").size() > 0){
                            JsonObject resultObj = (JsonObject) rootObj.getAsJsonArray("result").get(0);
                            wkt = resultObj.get("wkt").getAsString();
                            totalDistance = resultObj.get("totalDistance").getAsFloat();
                            totalTime = resultObj.get("totalTime").getAsInt();
                            JsonArray instructionListArray = (JsonArray) resultObj.getAsJsonArray("instructionList");
                            for (int i = 0; i <instructionListArray.size() ; i++) {
                                HashMap<String,String > instruction = new HashMap<>();
                                instruction.put("name",instructionListArray.get(i).getAsJsonObject().get("name").getAsString());
                                instruction.put("distance",instructionListArray.get(i).getAsJsonObject().get("distance").getAsString());
                                instruction.put("time",instructionListArray.get(i).getAsJsonObject().get("time").getAsString());
                                instruction.put("text",instructionListArray.get(i).getAsJsonObject().get("text").getAsString());
                                instructionList.add(instruction);
                            }
                        }
                        callBackDirection.onResponse(wkt,totalDistance,totalTime,instructionList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error == null || error.networkResponse == null) {
                    return;
                }
                System.out.println("sysosout " +error);
                callBackDirection.onError(error.networkResponse);
                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");
                    System.out.println("sysosout " +body);
                } catch (UnsupportedEncodingException e) {
                    // exception
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("api-key",api_key);
                return params;
            };
        };
        queue.add(stringRequest);
    }

    public static interface CallBackDirection{
        public void onResponse(String wkt, float totalDistance, int totalTime, ArrayList<HashMap<String, String>> instructionList);
        public void onError(NetworkResponse networkResponse);
    }
}
