package longqing.xfly;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;



public class WaypointActivity extends AppCompatActivity {

    int REQUESTCODE_FROM_ACTIVITY = 1000;
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
}
