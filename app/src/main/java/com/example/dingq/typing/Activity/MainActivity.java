package com.example.dingq.typing.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingq.typing.Fragment.KBDownFragment;
import com.example.dingq.typing.Fragment.KBLeftFragment;
import com.example.dingq.typing.Fragment.KBRightFragment;
import com.example.dingq.typing.Fragment.KBUpFragment;
import com.example.dingq.typing.Model.ExpDataManager;
import com.example.dingq.typing.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class MainActivity extends AppCompatActivity {
    private ExpDataManager expDataManager;
    private String subId;
    // 事先准备的几套block顺序
    private ArrayList<ArrayList<Fragment>> blockOrders;
    private ArrayList<ArrayList<String>> blockOrdersStr;

    private int order = 0; // 本次实验采用的block顺序代号
    private int blockCnt = 0; // 记录block数
    private int trialCnt = 0; // 记录trial数
    private int targetStlType; // 0英文，1数字，2中文，3符号，4emoji

    private TextView textStlPre;
    private ImageView emojiStlPre;
    private ArrayList<ArrayList<String>> stl; // 事先准备的刺激数据
    private ArrayList<ArrayList> stlTypeOrder; // 本次实验的刺激类型呈现顺序

    private Timer timer;
    private TimerTask timerTask;
    private int rt = 0; // 记录RT，单位为ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("实验中");
        initExp();
    }

    // 初始化实验
    private void initExp() {
        expDataManager = new ExpDataManager(this);
        // 装载数据
        order = getOrder();
        stl = getStlData();
        stlTypeOrder = getStlTypeOrder();
        blockOrders = getBlockOrders();
        // 设置UI
        textStlPre = findViewById(R.id.text_stl_pre);
        emojiStlPre = findViewById(R.id.emoji_str_pre);
        replaceFragment(R.id.my_keyboard, blockOrders.get(order).get(blockCnt));
    }

    // 获得本次实验采用的block顺序代号
    private int getOrder() {
        // 根据被试编号决定采用哪套block顺序
        Intent intent = getIntent();
        subId = intent.getStringExtra("subId");
        int id = Integer.parseInt(subId);
        return (id - 1) % 4;
    }

    // 从excel文件读取刺激数据
    private ArrayList<ArrayList<String>> getStlData() {
        ArrayList<ArrayList<String>> stlData = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open("stl_data.xls"));
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            for (int i = 0; i < cols; i++) {
                ArrayList<String> stl = new ArrayList<>();
                for (int j = 0; j < rows; j++) {
                    stl.add(sheet.getCell(i, j).getContents());
                }
                stlData.add(stl);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return stlData;
    }

    // 随机获得本次实验的刺激类型呈现顺序
    private ArrayList<ArrayList> getStlTypeOrder() {
        // 一个block中5种刺激各出现8次，同类型刺激不能连续出现
        ArrayList<ArrayList> stlTypeOrder = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ArrayList aBlock = new ArrayList<>();
            for (int j = 0; j < 40; j++) {
                aBlock.add(j % 5);
            }
            do {
                Collections.shuffle(aBlock);
            } while (existRepeat(aBlock));
            stlTypeOrder.add(aBlock);
        }
        return stlTypeOrder;
    }

    // 判断同类型刺激是否连续出现
    private boolean existRepeat(ArrayList<Integer> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) == data.get(i + 1)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ArrayList> getStlTyeOrder() {
        return stlTypeOrder;
    }

    // 从excel文件读取几套block顺序
    private ArrayList<ArrayList<Fragment>> getBlockOrders() {
        KBLeftFragment kbLeftFragment = new KBLeftFragment();
        KBRightFragment kbRightFragment = new KBRightFragment();
        KBUpFragment kbUpFragment = new KBUpFragment();
        KBDownFragment kbDownFragment = new KBDownFragment();
        ArrayList<ArrayList<Fragment>> bo = new ArrayList<>();
        blockOrdersStr = new ArrayList<>();
        AssetManager assetManager = getAssets();
        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open("block_orders.xls"));
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            for (int i = 0; i < cols; i++) {
                ArrayList<Fragment> blocks = new ArrayList<>();
                ArrayList<String> blocksStr = new ArrayList<>();
                for (int j = 0; j < rows; j++) {
                    switch (sheet.getCell(i, j).getContents()) {
                        case "上":
                            blocks.add(kbUpFragment);
                            blocksStr.add("U");
                            break;
                        case "下":
                            blocks.add(kbDownFragment);
                            blocksStr.add("D");
                            break;
                        case "左":
                            blocks.add(kbLeftFragment);
                            blocksStr.add("L");
                            break;
                        case "右":
                            blocks.add(kbRightFragment);
                            blocksStr.add("R");
                            break;
                        default:
                            blocks.add(kbUpFragment);
                            blocksStr.add("");
                            break;
                    }
                }
                bo.add(blocks);
                blockOrdersStr.add(blocksStr);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }
        return bo;
    }

    // 获得当前实验的block顺序
    public ArrayList<String> getCurrentBlockOrder() {
        return blockOrdersStr.get(order);
    }

    // 在指定ID的FrameLayout加载指定Fragment
    public void replaceFragment(int layoutId, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId, fragment);
        fragmentTransaction.commit();
    }

    // 开始一个trial
    public void startTrial() {
        if (startTimer()) {
            nextStl();
        }
    }

    private static final int TIMEOUT = 9;
    private static int MY_PERMISSIONS_REQUEST = 21;
    // 结束一个trial
    public void endTrial(int resStlType) {
        // 记录数据
        if (resStlType == TIMEOUT) {
            expDataManager.recordTimeoutTrial(blockCnt);
        } else if (stopTimer()) {
            expDataManager.recordSingleTrial(blockCnt, resStlType == targetStlType, rt);
        } else { // trial未开始点击切换键
            return;
        }
        // 设置UI
        textStlPre.setVisibility(View.GONE);
        emojiStlPre.setVisibility((View.GONE));
        // 处理trial结束逻辑
        trialCnt++;
        // 判断是否完成一个block
        if (trialCnt == 40) { // 1个block：5种刺激，每种呈现8次，共40个trial
            trialCnt = 0;
            blockCnt++;
            // 判断是否完成全部block
            if (blockCnt == 12) { // 共12个block
                // 检查存取权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // 申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
                    // 将实验数据写入excel文件
                    expDataManager.exportDataToExcel(subId);
                }
                // 结束实验
                AlertDialog endDialog = new AlertDialog.Builder(this).create();
                endDialog.setTitle("实验结束");
                endDialog.setMessage("感谢您的参与！请通知主试。\n程序将于5s后自动关闭");
                endDialog.setCancelable(false);
                endDialog.show();
                // 5s后程序自动关闭，预留文件IO时间
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 5000);
            } else {
                // 休息10s后开始下一个block
                final AlertDialog restDialog = new AlertDialog.Builder(this).create();
                restDialog.setTitle("休息时间");
                restDialog.setMessage("请休息10s后再继续实验。");
                restDialog.setCancelable(false);
                restDialog.show();

                final AlertDialog.Builder continueDialog = new AlertDialog.Builder(this);
                continueDialog.setMessage("接下来键盘键位将发生变化，请点击确认继续实验。");
                continueDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        replaceFragment(R.id.my_keyboard, blockOrders.get(order).get(blockCnt));
                    }
                });
                continueDialog.setCancelable(false);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        restDialog.dismiss();
                        // 确认继续实验
                        continueDialog.show();
                    }
                }, 10000);
            }
        }
    }

    // 开始计时
    private boolean startTimer() {
        if (timer == null) {
            rt = 0;
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    rt++;
                    // RT超过5s为反应超时，结束本trial
                    if (rt > 5000) {
                        stopTimer();
                        Message message = new Message();
                        message.what = TIMEOUT;
                        handler.sendMessage(message);
                    }
                }
            };
            timer.schedule(timerTask, 1, 1);
            return true;
        }
        return false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case TIMEOUT:
                    endTrial(TIMEOUT);
                    break;
                default:
                    break;
            }
        }
    };

    // 停止计时
    private boolean stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            timerTask.cancel();
            timerTask = null;
            return true;
        }
        return false;
    }

    // 随机获得并呈现下一个刺激
    private void nextStl() {
        String stl = getRandomStl();
        if (stl.contains("emoji")) {
            textStlPre.setVisibility(View.GONE);
            emojiStlPre.setVisibility((View.VISIBLE));
            switch (stl) {
                case "emoji1":
                    emojiStlPre.setImageResource(R.drawable.emoji1);
                    break;
                case "emoji2":
                    emojiStlPre.setImageResource(R.drawable.emoji2);
                    break;
                case "emoji3":
                    emojiStlPre.setImageResource(R.drawable.emoji3);
                    break;
                case "emoji4":
                    emojiStlPre.setImageResource(R.drawable.emoji4);
                    break;
                case "emoji5":
                    emojiStlPre.setImageResource(R.drawable.emoji51);
                    break;
                case "emoji6":
                    emojiStlPre.setImageResource(R.drawable.emoji6);
                    break;
                case "emoji7":
                    emojiStlPre.setImageResource(R.drawable.emoji7);
                    break;
                case "emoji8":
                    emojiStlPre.setImageResource(R.drawable.emoji8);
                    break;
                case "emoji9":
                    emojiStlPre.setImageResource(R.drawable.emoji9);
                    break;
                default:
                    emojiStlPre.setImageResource(R.drawable.emoji1);
                    break;
            }
        } else {
            textStlPre.setVisibility(View.VISIBLE);
            emojiStlPre.setVisibility((View.GONE));
            textStlPre.setText(stl);
        }
    }

    // 随机获得一个刺激
    private String getRandomStl() {
        Random random = new Random();
        targetStlType = (int) stlTypeOrder.get(blockCnt).get(trialCnt);

        // 将中英文刺激的标志合并为一
        if (targetStlType == 0) {
            targetStlType = 2;
            return stl.get(0).get(random.nextInt(stl.get(0).size()));
        }
        return stl.get(targetStlType).get(random.nextInt(stl.get(0).size()));
    }

    // 请求权限回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                expDataManager.exportDataToExcel(subId);
                Toast.makeText(this, "实验数据已保存", Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, "缺少存取权限", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 双击返回退出
    private long lastBackTime = 0; // 上次按下返回键的系统时间
    private long currentBackTime = 0; // 当前按下返回键的系统时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 捕获返回键按下的事件
        if (keyCode == KeyEvent.KEYCODE_BACK){
            // 获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            // 比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if (currentBackTime - lastBackTime > 2 * 1000){
                Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            } else { // 如果两次按下的时间差小于2秒，则退出程序
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
