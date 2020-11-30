package com.example.mytravellerapp.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mytravellerapp.R;
import com.example.mytravellerapp.ui.utils.BaseBackPressedListener;
import com.squareup.picasso.Picasso;


public class FullScreenImageFragment extends Fragment implements BaseBackPressedListener.OnBackPressedListener{

    private String imageUrl;
    private ImageButton btn;
    private ImageView imageView;
    private Fragment mFragment;

    public static FullScreenImageFragment newInstance(String name, Fragment parentFragment) {
        FullScreenImageFragment fragment = new FullScreenImageFragment();
        fragment.mFragment = parentFragment;
        if (name != null) fragment.imageUrl = name;
        return fragment;
    }
//    public static FullScreenImageFragment getFragment(String name, Context context, Fragment parentFragment) {
//        FullScreenImageFragment fragment = new FullScreenImageFragment(name, context, parentFragment);
//        return fragment;
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_fullscreen_image, container, false);
        imageView = rootView.findViewById(R.id.image);
        btn = rootView.findViewById(R.id.btn_close);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragment != null) mFragment.getFragmentManager().popBackStack();
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                fm.popBackStack();
            }
        });
        Picasso.with(getContext()).load(imageUrl).into(imageView);

        return rootView;
    }


    @Override
    public void doBack() {
        if (mFragment != null) mFragment.getFragmentManager().popBackStack();
    }
}
