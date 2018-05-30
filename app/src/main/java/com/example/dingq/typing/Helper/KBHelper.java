package com.example.dingq.typing.Helper;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dingq.typing.Activity.MainActivity;
import com.example.dingq.typing.Fragment.ChinFragment;
import com.example.dingq.typing.Fragment.EmojiFragment;
import com.example.dingq.typing.Fragment.EngFragment;
import com.example.dingq.typing.Fragment.NumFragment;
import com.example.dingq.typing.Fragment.SChinFragment;
import com.example.dingq.typing.Fragment.SEmojiFragment;
import com.example.dingq.typing.Fragment.SEngFragment;
import com.example.dingq.typing.Fragment.SNumFragment;
import com.example.dingq.typing.Fragment.SSignFragment;
import com.example.dingq.typing.Fragment.SignFragment;
import com.example.dingq.typing.R;

// 用于帮助设置Keyboard的UI
public class KBHelper implements View.OnClickListener {
    private View view;
    private MainActivity mActivity;
    private Button numBtn;
    private Button chinEngBtn;
    private boolean isChin;
    private Button signBtn;
    private Button spaceBtn;
    private Button zeroBtn;
    private ImageButton emojiBtn;
    private NumFragment numFragment;
    private ChinFragment chinFragment;
    private EngFragment engFragment;
    private SignFragment signFragment;
    private EmojiFragment emojiFragment;
    private SNumFragment sNumFragment;
    private SChinFragment sChinFragment;
    private SEngFragment sEngFragment;
    private SSignFragment sSignFragment;
    private SEmojiFragment sEmojiFragment;

    public KBHelper(View v, MainActivity activity) {
        view = v;
        mActivity = activity;
    }

    // 加载Keyboard的UI
    // 加载常规键盘（U、L、R）的UI
    public void setRegKBUI() {
        numFragment = new NumFragment();
        chinFragment = new ChinFragment();
        engFragment = new EngFragment();
        signFragment = new SignFragment();
        emojiFragment = new EmojiFragment();

        numBtn = view.findViewById(R.id.num);
        chinEngBtn = view.findViewById(R.id.chin_eng);
        signBtn = view.findViewById(R.id.sign);
        emojiBtn = view.findViewById(R.id.emoji);

        numBtn.setOnClickListener(this);
        chinEngBtn.setOnClickListener(this);
        signBtn.setOnClickListener(this);
        emojiBtn.setOnClickListener(this);

        mActivity.replaceFragment(R.id.typing_area, chinFragment);
        isChin = true;
    }
    // 加载特殊键盘（D）的UI
    public void setSKBUI() {
        sNumFragment = new SNumFragment();
        sChinFragment = new SChinFragment();
        sEngFragment = new SEngFragment();
        sSignFragment = new SSignFragment();
        sEmojiFragment = new SEmojiFragment();

        numBtn = view.findViewById(R.id.s_num);
        chinEngBtn = view.findViewById(R.id.s_chin_eng);
        signBtn = view.findViewById(R.id.s_sign);
        spaceBtn = view.findViewById(R.id.s_space);
        zeroBtn = view.findViewById(R.id.s_zero);
        emojiBtn = view.findViewById(R.id.s_emoji);

        numBtn.setOnClickListener(this);
        chinEngBtn.setOnClickListener(this);
        signBtn.setOnClickListener(this);
        emojiBtn.setOnClickListener(this);

        mActivity.replaceFragment(R.id.typing_area, sChinFragment);
        isChin = true;
    }

    // 实现Keyboard中切换键的功能
    @Override
    public void onClick(View v) {
        // 作出反应，结束trial
        int resStlType; // 0英文，1数字，2中文，3符号，4emoji
        switch (v.getId()) {
            case R.id.num:
                resStlType = 1;
                mActivity.replaceFragment(R.id.typing_area, numFragment);
                break;
            case R.id.chin_eng:
                resStlType = 2;
                if (isChin) {
                    mActivity.replaceFragment(R.id.typing_area, engFragment);
                } else {
                    mActivity.replaceFragment(R.id.typing_area, chinFragment);
                }
                isChin = !isChin;
                break;
            case R.id.sign:
                resStlType = 3;
                mActivity.replaceFragment(R.id.typing_area, signFragment);
                break;
            case R.id.emoji:
                resStlType = 4;
                mActivity.replaceFragment(R.id.typing_area, emojiFragment);
                break;

            case R.id.s_num:
                resStlType = 1;
                mActivity.replaceFragment(R.id.typing_area, sNumFragment);
                setNumKB();
                break;
            case R.id.s_chin_eng:
                resStlType = 2;
                if (isChin) {
                    mActivity.replaceFragment(R.id.typing_area, sEngFragment);
                } else {
                    mActivity.replaceFragment(R.id.typing_area, sChinFragment);
                }
                isChin = !isChin;
                restoreKB();
                break;
            case R.id.s_sign:
                resStlType = 3;
                mActivity.replaceFragment(R.id.typing_area, sSignFragment);
                restoreKB();
                break;
            case R.id.s_emoji:
                resStlType = 4;
                mActivity.replaceFragment(R.id.typing_area, sEmojiFragment);
                restoreKB();
                break;
            default:
                resStlType = 0;
                break;
        }
        mActivity.endTrial(resStlType);
    }

    // 实现特殊键盘的底部键位变化
    private void setNumKB() {
        spaceBtn.setVisibility(View.GONE);
        zeroBtn.setVisibility(View.VISIBLE);
    }

    private void restoreKB() {
        spaceBtn.setVisibility(View.VISIBLE);
        zeroBtn.setVisibility(View.GONE);
    }
}
