package com.example.dingq.typing.Helper;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dingq.typing.Activity.MainActivity;
import com.example.dingq.typing.R;

// 用于帮助设置TypingArea的UI
public class TAHelper implements View.OnClickListener {
    private View view;
    private MainActivity mActivity;
    private Button startBtn;
    private ImageButton startIBtn;

    public TAHelper(View v, MainActivity activity) {
        view = v;
        mActivity = activity;
    }

    // 实现TypingArea中间键 开始trial 的功能
    // 中间键为Button
    public void setStartBtn() {
        startBtn = view.findViewById(R.id.start);
        startBtn.setOnClickListener(this);
    }
    // 中间键为ImageButton
    public void setStartIBtn() {
        startIBtn = view.findViewById(R.id.start);
        startIBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 开始trial
        mActivity.startTrial();
    }
}
