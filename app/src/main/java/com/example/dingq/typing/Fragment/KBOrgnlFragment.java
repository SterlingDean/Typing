package com.example.dingq.typing.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dingq.typing.Activity.MainActivity;
import com.example.dingq.typing.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KBOrgnlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KBOrgnlFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public KBOrgnlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KBOrgnlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KBOrgnlFragment newInstance(String param1, String param2) {
        KBOrgnlFragment fragment = new KBOrgnlFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private MainActivity mActivity;
    private View view;
    private Button numBtn;
    private Button chinEngBtn;
    private boolean isChin;
    private Button signBtn;
    private Button space0Btn;
    private Button zeroBtn;
    private ImageButton voiceBtn;
    private Button space1Btn;
    private ImageButton emojiBtn;
    private SNumFragment sNumFragment;
    private SChinFragment sChinFragment;
    private SEngFragment sEngFragment;
    private SSignFragment sSignFragment;
    private SEmojiFragment sEmojiFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kborgnl, container, false);

        numBtn = view.findViewById(R.id.s_num);
        chinEngBtn = view.findViewById(R.id.s_chin_eng);
        signBtn = view.findViewById(R.id.s_sign);
        emojiBtn = view.findViewById(R.id.s_emoji);
        space0Btn = view.findViewById(R.id.kborgnl_space_0);
        zeroBtn = view.findViewById(R.id.kborgnl_zero);
        space1Btn = view.findViewById(R.id.kborgnl_space_1);
        voiceBtn = view.findViewById(R.id.kborgnl_voice);

        sNumFragment = new SNumFragment();
        sChinFragment = new SChinFragment();
        sEngFragment = new SEngFragment();
        sSignFragment = new SSignFragment();
        sEmojiFragment = new SEmojiFragment();

        View.OnClickListener switchListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.s_num:
                        mActivity.replaceFragment(R.id.typing_area, sNumFragment);
                        setNumKB();
                        break;
                    case R.id.s_chin_eng:
                        if (isChin)
                            mActivity.replaceFragment(R.id.typing_area, sEngFragment);
                        else mActivity.replaceFragment(R.id.typing_area, sChinFragment);
                        isChin = !isChin;
                        restoreKB();
                        break;
                    case R.id.s_sign:
                        mActivity.replaceFragment(R.id.typing_area, sSignFragment);
                        restoreKB();
                        break;
                    case R.id.s_emoji:
                        mActivity.replaceFragment(R.id.typing_area, sEmojiFragment);
                        restoreKB();
                        break;
                }
//                mActivity.stopTimer();
            }
        };
        numBtn.setOnClickListener(switchListener);
        chinEngBtn.setOnClickListener(switchListener);
        signBtn.setOnClickListener(switchListener);
        emojiBtn.setOnClickListener(switchListener);

        mActivity.replaceFragment(R.id.typing_area, sChinFragment);
        isChin = true;

        return view;
    }

    private void setNumKB() {
        space0Btn.setVisibility(View.GONE);
        zeroBtn.setVisibility(View.VISIBLE);
        voiceBtn.setVisibility(View.GONE);
        space1Btn.setVisibility(View.VISIBLE);
    }

    private void restoreKB() {
        space0Btn.setVisibility(View.VISIBLE);
        zeroBtn.setVisibility(View.GONE);
        voiceBtn.setVisibility(View.VISIBLE);
        space1Btn.setVisibility(View.GONE);
    }
}
