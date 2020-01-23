package com.project.ksih_article.ui.others;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.ksih_article.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OthersFragment extends Fragment {

    public OthersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_others, container, false);
        return root;
    }

}
