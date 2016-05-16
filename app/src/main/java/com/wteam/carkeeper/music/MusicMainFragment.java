package com.wteam.carkeeper.music;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wteam.carkeeper.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lhb on 2016/4/26.
 */
public class MusicMainFragment extends Fragment {

    private CircleImageView aaa;
    private CircleImageView bbb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_main,container,false);
        return view;
    }
}
