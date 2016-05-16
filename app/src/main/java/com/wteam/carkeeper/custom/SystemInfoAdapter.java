package com.wteam.carkeeper.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.SystemInfo;

import java.util.ArrayList;

/**
 * Created by lhb on 2016/5/6.
 */
public class SystemInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private Context context;
    /*按时间顺序，越靠近当前时间越靠前（时间降序）*/
    private ArrayList<SystemInfo> arrayList;
    private int[] types;

    public SystemInfoAdapter(Context context, ArrayList<SystemInfo> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.types = new int[arrayList.size()];
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0 || position ==7 || position == 15) {
            return LayoutInflater.from(context).inflate(R.layout.item_header,null);
        } else {

            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_system_info_content,null);
            TextView t = (TextView)linearLayout.findViewById(R.id.system_msg_content);
            t.setSelected(true);
            ((TextView)linearLayout.findViewById(R.id.system_msg_title)).setText("【" + arrayList.get(position).getTitle() +"】");
            return linearLayout;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }


    @Override
    public int getItemViewType(int position) {
        if(position == 15 || position ==7 || position==0) {
            return 1;
        }else {
            return 0;
        }
    }
}
