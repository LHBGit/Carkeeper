package com.wteam.carkeeper.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.OrderInfo;

import java.util.ArrayList;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context context;
    /*按时间顺序，越靠近当前时间越靠前（时间降序）*/
    private ArrayList<OrderInfo> arrayList;
    private int[] types;

    public OrderInfoAdapter(Context context, ArrayList<OrderInfo> arrayList) {
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
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_order_management,null);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.gas_station_name + position);
            return relativeLayout;
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
