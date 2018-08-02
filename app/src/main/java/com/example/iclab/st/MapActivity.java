package com.example.iclab.st;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

// 지도 액티비티
public class MapActivity extends AppCompatActivity implements MapView.MapViewEventListener {
    Button gpsButton = null;
    Button applyButton = null;
    Button cancelButton = null;
    MapPOIItem marker = null;
    MapPoint clickPoint = null;
    boolean isButtonVisible = false;
    double latitude;
    double longitude;
    static List<MapPOIItem> markerList = new ArrayList<MapPOIItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        final MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("e95ede72416f09346c75c0acb52472ed");
        RelativeLayout container = findViewById(R.id.map);
        container.addView(mapView);

        mapView.setMapViewEventListener(this);

        for (int i = 0; i < markerList.size(); ++i)
        {
            mapView.addPOIItem(markerList.get(i));
        }

        gpsButton = findViewById(R.id.gps);

        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveMapViewCurrentPosition(mapView);
            }
        });

        applyButton = findViewById(R.id.apply);
        cancelButton = findViewById(R.id.cancel);

        // 확인버튼
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, SurveyActivity.class);

                intent.putExtra("latitude", clickPoint.getMapPointGeoCoord().latitude);
                intent.putExtra("longitude", clickPoint.getMapPointGeoCoord().longitude);

                startActivity(intent);

                // 버튼 비활성화
                applyButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);

                isButtonVisible = false;

                finish();
            }
        });

        // 취소버튼
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.INVISIBLE);

                // 마커 삭제
                mapView.removePOIItem(marker);
                markerList.remove(marker);

                isButtonVisible = false;
            }
        });
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {
        Intent intent = getIntent();

        final double beginLatitude = intent.getDoubleExtra("latitude", 36.770598f);
        final double beginLongitude = intent.getDoubleExtra("longitude", 126.931647f);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(beginLatitude, beginLongitude), true);
    }

    public void moveMapViewCurrentPosition(MapView mapView) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(locationListener);

        // 권한이 허용되어있지 않은 경우
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 위치 정보 접근 요청
            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }


    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        if (!isButtonVisible) {
            // 마커 생성
            marker = new MapPOIItem();
            marker.setItemName("AKM");
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            markerList.add(marker);

            mapView.addPOIItem(marker);

            double latitude = mapPoint.getMapPointGeoCoord().latitude;
            double longitude = mapPoint.getMapPointGeoCoord().longitude;

            /***************************************************************************
            *  dongCode가 현재 동의 코드를 나타냅니다.
            *  getDongCode는 동의 코드를 가져오는 소스코드인데,
            *  건물 안의 경우 충청남도와 같이 '도' 단위의 위치가 파악이 안되는 이슈가 있는데
            *  해결의 방안을 모르겠네욤..
            *  getAddress로 국가 도 시 동 ex) 대한민국 충청남도 아산시 신창면 의 형태로
            *  String을 가져올 수 있습니다.
            *  필요하시다면 substring을 이용하여서 대한민국 잘라서 사용하세욥
            ***************************************************************************/
            String dongCode = getDongCode(getAddress(this, latitude, longitude));

            // 버튼 활성화
            applyButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);

            clickPoint = mapPoint;
            isButtonVisible = true;
        }
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            latitude = location.getLatitude();   //위도
            longitude = location.getLongitude(); //경도

            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    // 현재 위치 획득
    private String getAddress(Context context, double lat, double lon) {

        Geocoder geocoder = new Geocoder(context);
        List<Address> location = null;
        try {
            location = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = location.get(0).getAddressLine(0);

        Log.d("address:", address);

        return address;
    }

    // 현재 동의 코드 획득
    private String getDongCode(String address)
    {
        long dongCode = 0;
        String[] region;

        region = address.split("\\s");

        for (int i = 0; i < region.length; ++i)
        {
            Log.d("region: ", region[i]);
        }

        String[] urls = new String[4];
        String jsonText = ".json.txt";

        // 지역 URL
        urls[1] = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
        urls[2] = "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl.";
        urls[3] = "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf.";

        try {

            // 현재 지역의 지역 코드를 검색
            for (int i = 1; i <= urls.length; ++i)
            {
                LinkedHashMap<String, Integer> regionMap = new LinkedHashMap<String, Integer>();
                URL url = new URL(urls[i]);

                URLAsyncTask regionTask = new URLAsyncTask();

                regionTask.execute(url);

                regionMap = regionTask.get();

                if (i == 3)
                {
                    // 동코드: region[3]
                    Log.d("region code: ", regionMap.get(region[i]) + "");
                    dongCode = regionMap.get(region[i]);
                    break;
                }

                urls[i + 1] += String.valueOf(regionMap.get(region[i]));
                urls[i + 1] += jsonText;

            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return String.valueOf(dongCode);
    }
}
