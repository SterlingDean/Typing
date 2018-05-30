package com.example.dingq.typing.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dingq.typing.Activity.MainActivity;
import com.example.dingq.typing.Helper.TAHelper;
import com.example.dingq.typing.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmojiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmojiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmojiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmojiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmojiFragment newInstance(String param1, String param2) {
        EmojiFragment fragment = new EmojiFragment();
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

    private View view;
    private MainActivity mActivity;
    private TAHelper TAHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_emoji, container, false);
        mActivity = (MainActivity) getActivity();
        TAHelper = new TAHelper(view, mActivity);

        TAHelper.setStartIBtn();

        return view;
    }
}
