package com.example.dingq.typing.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dingq.typing.Activity.MainActivity;
import com.example.dingq.typing.Helper.KBHelper;
import com.example.dingq.typing.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KBDownFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KBDownFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KBDownFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KBDownFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KBDownFragment newInstance(String param1, String param2) {
        KBDownFragment fragment = new KBDownFragment();
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
    private KBHelper kbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kbdown, container, false);
        mActivity = (MainActivity) getActivity();

        kbHelper = new KBHelper(view, mActivity);
        kbHelper.setSKBUI();

        return view;
    }
}
