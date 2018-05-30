package com.example.dingq.typing.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dingq.typing.R;

public class InstructionActivity extends AppCompatActivity {
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        setTitle("实验说明");

        startBtn = findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructionActivity.this, MainActivity.class);
                intent.putExtra("subId", getIntent().getStringExtra("subId"));
                startActivity(intent);
                finish();
            }
        });
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
