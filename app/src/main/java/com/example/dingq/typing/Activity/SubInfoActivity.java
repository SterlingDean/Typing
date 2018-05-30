package com.example.dingq.typing.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dingq.typing.R;

public class SubInfoActivity extends AppCompatActivity {

    private EditText subId;
    private Button confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_info);

        setTitle("被试信息录入");

        subId = findViewById(R.id.sub_id);
        confirmBtn = findViewById(R.id.confirm);

        // 录入被试信息并进入实验
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subIdStr = subId.getText().toString();

                if (subIdStr.equals("")) { // 未输入被试编号
                    Toast.makeText(getApplicationContext(), "请输入被试编号", Toast.LENGTH_SHORT).show();
                } else if (subIdStr.startsWith("0")) { // 被试编号以0开头
                    Toast.makeText(getApplicationContext(), "被试编号不能以0开头，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    // 存储被试信息
                    // 进入实验
                    Intent intent = new Intent(SubInfoActivity.this, InstructionActivity.class);
                    intent.putExtra("subId", subIdStr);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
