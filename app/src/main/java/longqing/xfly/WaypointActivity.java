package longqing.xfly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointMission;


public class WaypointActivity extends AppCompatActivity {

    protected static final String TAG = "WaypointActivity";
    int REQUESTCODE_FROM_ACTIVITY = 1000;
    private List<Waypoint> waypointList = new ArrayList<>();
    private List<String> jsonList = new ArrayList<>();

    public static WaypointMission.Builder waypointMissionBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_waypoint);

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
                .withFileFilter(new String[]{".txt", ".JSON"}) // 过滤文件格式
                .start();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_FROM_ACTIVITY) {

            // 如果是文件选择模式，需要获取选择的所有文件的路径集合
            List<String> list = data.getStringArrayListExtra("paths");
            // extract first file path from the list
            String fileName = list.get(0);
            // Do anything
            Toast.makeText(getApplicationContext(), "选中的文件为：" + fileName, Toast.LENGTH_LONG).show();

            JsonArray jsonArray = parseNoHeaderJArray(fileName);
            Gson gson = new Gson();
            ArrayList<TbBean> beanList = new ArrayList<>();

            //加强for循环遍历JsonArray
            for (JsonElement bean : jsonArray){
                TbBean beanTemp = gson.fromJson(bean, TbBean.class);
                beanList.add(beanTemp);
            }
            setResultToToast(beanList.get(1).XMC);// 测试JSON解析是否成功
            returnWaypoint();
        }
    }

    /**
     * setting Bean according json file
     */
    private class TbBean {

        // 字段必须和json文件一致
        private boolean Check;
        private String ID;
        private String TBYBH;
        private String XZQDM;
        private String XMC;
        private String TBMJ;
        private String XZB;
        private String YZB;
        private String XZBF;
        private String YZBF;
        private String QSDWMC;
        private String QSXZ;
        private String DLBM;
        private String YPDL;
        private String NYBZ;
        private String PZD;
        private String DLYZX;
        private String WYRDDL;
        private String SFJZ;
        private String SFXZ;
        private String WJZLX;
        private String JZSM;
        private String BZ;
        private String JZRY;

        private String TBFW;
        private String TBFWF;

        public boolean isCheck() {
            return Check;
        }

        public void setCheck(boolean check) {
            Check = check;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getTBYBH() {
            return TBYBH;
        }

        public void setTBYBH(String TBYBH) {
            this.TBYBH = TBYBH;
        }

        public String getXZQDM() {
            return XZQDM;
        }

        public void setXZQDM(String XZQDM) {
            this.XZQDM = XZQDM;
        }

        public String getXMC() {
            return XMC;
        }

        public void setXMC(String XMC) {
            this.XMC = XMC;
        }

        public String getTBMJ() {
            return TBMJ;
        }

        public void setTBMJ(String TBMJ) {
            this.TBMJ = TBMJ;
        }

        public String getXZB() {
            return XZB;
        }

        public void setXZB(String XZB) {
            this.XZB = XZB;
        }

        public String getYZB() {
            return YZB;
        }

        public void setYZB(String YZB) {
            this.YZB = YZB;
        }

        public String getXZBF() {
            return XZBF;
        }

        public void setXZBF(String XZBF) {
            this.XZBF = XZBF;
        }

        public String getYZBF() {
            return YZBF;
        }

        public void setYZBF(String YZBF) {
            this.YZBF = YZBF;
        }

        public String getQSDWMC() {
            return QSDWMC;
        }

        public void setQSDWMC(String QSDWMC) {
            this.QSDWMC = QSDWMC;
        }

        public String getQSXZ() {
            return QSXZ;
        }

        public void setQSXZ(String QSXZ) {
            this.QSXZ = QSXZ;
        }

        public String getDLBM() {
            return DLBM;
        }

        public void setDLBM(String DLBM) {
            this.DLBM = DLBM;
        }

        public String getYPDL() {
            return YPDL;
        }

        public void setYPDL(String YPDL) {
            this.YPDL = YPDL;
        }

        public String getNYBZ() {
            return NYBZ;
        }

        public void setNYBZ(String NYBZ) {
            this.NYBZ = NYBZ;
        }

        public String getPZD() {
            return PZD;
        }

        public void setPZD(String PZD) {
            this.PZD = PZD;
        }

        public String getDLYZX() {
            return DLYZX;
        }

        public void setDLYZX(String DLYZX) {
            this.DLYZX = DLYZX;
        }

        public String getWYRDDL() {
            return WYRDDL;
        }

        public void setWYRDDL(String WYRDDL) {
            this.WYRDDL = WYRDDL;
        }

        public String getSFJZ() {
            return SFJZ;
        }

        public void setSFJZ(String SFJZ) {
            this.SFJZ = SFJZ;
        }

        public String getSFXZ() {
            return SFXZ;
        }

        public void setSFXZ(String SFXZ) {
            this.SFXZ = SFXZ;
        }

        public String getWJZLX() {
            return WJZLX;
        }

        public void setWJZLX(String WJZLX) {
            this.WJZLX = WJZLX;
        }

        public String getJZSM() {
            return JZSM;
        }

        public void setJZSM(String JZSM) {
            this.JZSM = JZSM;
        }

        public String getBZ() {
            return BZ;
        }

        public void setBZ(String BZ) {
            this.BZ = BZ;
        }

        public String getJZRY() {
            return JZRY;
        }

        public void setJZRY(String JZRY) {
            this.JZRY = JZRY;
        }

        public String getTBFW() {
            return TBFW;
        }

        public void setTBFW(String TBFW) {
            this.TBFW = TBFW;
        }

        public String getTBFWF() {
            return TBFWF;
        }

        public void setTBFWF(String TBFWF) {
            this.TBFWF = TBFWF;
        }
    }

    /**
     * 解析Json 数组
     */
    private JsonArray parseNoHeaderJArray(String fileName) {

        //读取JSON文件 并转成String
        String strByJson = readJsonFile(fileName);
        //Json的解析类对象
        JsonParser jsonParser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象，并返回
        return jsonParser.parse(strByJson).getAsJsonArray();

        // JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        // return jsonArray;
    }

    /**
     * 读取json文件，返回json字符串
     * @param fileName filepath
     * @return String
     */
    private String readJsonFile(String fileName) {

        String jsonStr = "";
        try {
            FileReader fileReader = new FileReader(fileName);
            Reader reader = new InputStreamReader(new FileInputStream(fileName),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            setResultToToast("readJsonFile：解析失败！");
            return null;
        }
    }

    /**
     * 从sd卡获取文件内容，和readJsonFile类似
     * @param filePath, 文件路径
     * @return String, 文件内容字符串
     */
    private String getFileFromSD(String filePath) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(filePath);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * return MapViewActivity
     */
    private void returnWaypoint(){
        Intent intent = new Intent(WaypointActivity.this,MapViewActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     *弹出提示
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
