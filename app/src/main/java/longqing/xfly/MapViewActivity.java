package longqing.xfly;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.lang.reflect.Field;
import java.util.List;


/**
 * Created by qinglong on 2019/8/21.
 */

public class MapViewActivity extends AppCompatActivity  {

    private long clickTime=0;

    private Button mBtnBack; // decla mBtnXX
    private Button mBtnLoadFile;

    private MapView mMapView ;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;
    private MyLocationStyle myLocationStyle;

    int REQUESTCODE_FROM_ACTIVITY = 1000;

    private Button mBtnMapPattern;
    private Button mBtnFly;
    private Button mBtnOther;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map_view);
        mMapView  = (MapView) findViewById(R.id.map);//获取地图控件引用
        mMapView.onCreate(savedInstanceState);//在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        initMap();

        mBtnBack = (Button) findViewById(R.id.mBtnBack); // 找到 mBtnxx
        mBtnLoadFile = (Button)findViewById(R.id.mBtnLoadFile);


        mBtnMapPattern = (Button) findViewById(R.id.mBtnMapPattern);
        mBtnFly = (Button)findViewById(R.id.mBtnFly);
        mBtnOther = (Button)findViewById(R.id.mBtnOther);


        setListeners(); // 调用监听器方法


    }
    // 监听器方法 所有的按钮都可以放在这里
    private void setListeners() {

        OnClick onClick = new OnClick();
        mBtnBack.setOnClickListener(onClick);
        mBtnLoadFile.setOnClickListener(onClick);

        mBtnMapPattern.setOnClickListener(onClick);
        mBtnFly.setOnClickListener(onClick);
        mBtnOther.setOnClickListener(onClick);

    }

    // 实现监听器接口
    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
            switch (view.getId()) {
                case R.id.mBtnBack: {
                    Intent  intent = new Intent(MapViewActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case R.id.mBtnLoadFile:{
                    showFileSelectDialog();
                    break;
                }
                case R.id.mBtnOther:{
                    // for reserved operation, here we used to test function of button.
                    Intent intent = new Intent(MapViewActivity.this,WaypointActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    private void showPopupMenu(View view) {

        PopupMenu popupMenu = new PopupMenu(MapViewActivity.this, view);
        popupMenu.getMenuInflater().inflate(R.menu.item, popupMenu.getMenu());
        popupMenu.show();

        switch (view.getId()) {
            case R.id.mBtnFly:
                popupMenu.getMenu().findItem(R.id.item_normal).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_satellite).setVisible(false);
                break;
            case R.id.mBtnMapPattern:
                popupMenu.getMenu().findItem(R.id.item_start_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_stop_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_re_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_recover_fly).setVisible(false);
                break;
            case R.id.mBtnBack:
                popupMenu.getMenu().findItem(R.id.item_normal).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_satellite).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_start_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_stop_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_re_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_recover_fly).setVisible(false);
                break;
            case R.id.mBtnLoadFile:
                popupMenu.getMenu().findItem(R.id.item_normal).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_satellite).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_start_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_stop_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_re_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_recover_fly).setVisible(false);
                break;
            case R.id.mBtnOther:
                popupMenu.getMenu().findItem(R.id.item_normal).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_satellite).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_start_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_stop_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_re_fly).setVisible(false);
                popupMenu.getMenu().findItem(R.id.item_recover_fly).setVisible(false);
                break;
            default:
                break;
        }

        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            MenuPopupHelper helper = (MenuPopupHelper) field.get(popupMenu);
            helper.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // 控件每一个item的点击事件
                switch (item.getItemId()) {
                    case R.id.item_normal:
                        setNormalMap();
                        break;
                    case R.id.item_satellite:
                        setSatelliteMap();
                        break;

                    case R.id.item_start_fly:
                        Toast.makeText(MapViewActivity.this, "开始飞行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_stop_fly:
                        Toast.makeText(MapViewActivity.this, "停止飞行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_re_fly:
                        Toast.makeText(MapViewActivity.this, "重新飞行", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item_recover_fly:
                        Toast.makeText(MapViewActivity.this, "恢复飞行", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }

    private void showFileSelectDialog() {
        new LFilePicker()
                .withActivity(MapViewActivity.this) //绑定Activity
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY) //设置请求码
                .withIconStyle(Constant.ICON_STYLE_GREEN)
                //.withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withMutilyMode(false)
                .withMaxNum(1) // 设置只能选取一个文件
                .withChooseMode(true) // 选择文件(默认)模式 (false 为选择文件夹)
                .withFileSize(10240 * 1024)//指定文件大小为10M
                // .withFileFilter(new String[]{".txt", ".llh"})
                .start();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_FROM_ACTIVITY) {

            //如果是文件选择模式，需要获取选择的所有文件的路径集合
            List<String> list = data.getStringArrayListExtra("paths");
            // extract first file path from the list
            String filePath = list.get(0).toString();

            // Do anything
            Toast.makeText(getApplicationContext(), "选中的文件为：" + filePath, Toast.LENGTH_LONG).show();
        }
    }

    // Map related
    private void initMap() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            setUpMap();
        }
    }

    private void setUpMap(){
        myLocationStyle = new MyLocationStyle(); // 初始化定位蓝点样式类
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));//设置定位蓝点的Style
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));
        aMap.setMyLocationEnabled(true); //显示定位蓝点
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  // 设置默认定位按钮是否显示，非必需设置。
        aMap.setMapLanguage(AMap.CHINESE); // 设置地图语言为中文
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
    }

    private void setNormalMap() {
        if (aMap != null) {
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            setUpMap();
        } else{
            aMap = mMapView.getMap();
            setUpMap();
        }
        Toast.makeText(MapViewActivity.this, "标准模式：设置成功", Toast.LENGTH_SHORT).show();
    }

    private void setSatelliteMap(){
        if (aMap != null) {
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            setUpMap();
        } else{
            aMap = mMapView.getMap();
            aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            setUpMap();
        }
        Toast.makeText(MapViewActivity.this, "卫星模式：设置成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 实现点击两次Back键退出应用程序
     */
    @Override
    public void onBackPressed(){
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            onBackPressed();
            return false;
        } else{
            return super.onKeyDown(keyCode, event);
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000){
            Toast.makeText(getApplicationContext(), "再按一次退出应用",
                    Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }

}


