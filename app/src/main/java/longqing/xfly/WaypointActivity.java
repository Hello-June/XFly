package longqing.xfly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;


public class WaypointActivity extends AppCompatActivity {

    protected static final String TAG = "WaypointActivity";
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    private List<Waypoint> waypointList = new ArrayList<>();

    public static WaypointMission.Builder waypointMissionBuilder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waypoint);
        showFileSelectDialog();
    }

    private void showFileSelectDialog() {
        new LFilePicker()
                .withActivity(this) //绑定Activity
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY) //设置请求码
                .withIconStyle(Constant.ICON_STYLE_GREEN)
                //.withStartPath("/storage/emulated/0/Download")//指定初始显示路径
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withMutilyMode(false)
                .withMaxNum(1) // 设置只能选取一个文件
                .withChooseMode(true) // 选择文件(默认)模式 (false 为选择文件夹)
                .withFileSize(10240 * 1024)//指定文件大小为10M
                // .withFileFilter(new String[]{".txt", ".llh"}) // 过滤文件格式
                .start();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_FROM_ACTIVITY) {

            // 如果是文件选择模式，需要获取选择的所有文件的路径集合
            List<String> list = data.getStringArrayListExtra("paths");
            // extract first file path from the list
            String filePath = list.get(0);
            // Do anything
            Toast.makeText(getApplicationContext(), "选中的文件为：" + filePath, Toast.LENGTH_LONG).show();
            readWaypointFile(filePath);
        }
    }

    private void readWaypointFile(String filePath) {

        /*It is necessary to check the file status before config mission with file.
        A file should not be null or has wrong format.*/

        int cnt = getFileLineCount(filePath); // cnt used to define the length of arrays
        int line_num = 0;
        if (cnt > 0) {
            line_num = cnt;
        }else {
            setResultToToast("Something wrong with input file, pls check it.");
            return;
        }
        double[] latitude = new double[line_num];
        double[] longitude = new double[line_num];
        float[] height = new float[line_num];
        File file = new File(filePath);
        if (file.isDirectory()) {
            setResultToToast("Type Error: this is not a file!");
        } else {
            try {
                InputStream inputStream = new FileInputStream(file);
                if (null != inputStream) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String strLine;
                    int i = 0;
                    while ((strLine = bufferedReader.readLine()) != null) {
                        String[] tokens = strLine.split(","); // Split string by comma, and the let the result assigned to tokens
                        latitude[i] = Double.parseDouble(tokens[0]);
                        longitude[i] = Double.parseDouble(tokens[1]);
                        height[i] = Float.parseFloat(tokens[2]);
                        i = i + 1;
                    }
                    setResultToToast("Load file succeed!");
                }
            } catch (java.io.FileNotFoundException e) {
                Log.d(TAG, "The File does not exist.");
            } catch (IOException e) {
                Log.d(TAG, e.getMessage());
            }
        }


        // add waypoint mission
            if (latitude.length == longitude.length && latitude.length == height.length) {
                for (int i = 0; i < latitude.length; i++) {

                    Waypoint mWaypoint = new Waypoint(latitude[i], longitude[i], height[i]);

                    //Add Waypoints to Waypoint arraylist;
                    if (waypointMissionBuilder != null) {
                        waypointList.add(mWaypoint);
                        waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                    } else {
                        waypointMissionBuilder = new WaypointMission.Builder();
                        waypointList.add(mWaypoint);
                        waypointMissionBuilder.waypointList(waypointList).waypointCount(waypointList.size());
                    }
                }
                setResultToToast("Config success!");
            } else {
                Toast.makeText(getApplicationContext(), "The lat, lon or height may have different length", Toast.LENGTH_LONG).show();

            }

    }
    /**
     * To obtain file line counts
     * @param filePath :file path
     * @return cnt
     */
    public static int getFileLineCount(String filePath) {

        int cnt = 0;
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new FileReader(filePath));
            String lineRead = "";
            while ((lineRead = reader.readLine()) != null) {
            }
            cnt = reader.getLineNumber();
        } catch (Exception e) {
            cnt = -1;
            e.printStackTrace();
        } finally {
            try {
                assert reader != null;
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cnt;
    }

    /**
     *
     * @param string string to be printed
     */
    private void setResultToToast(final String string) {
        WaypointActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(WaypointActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
