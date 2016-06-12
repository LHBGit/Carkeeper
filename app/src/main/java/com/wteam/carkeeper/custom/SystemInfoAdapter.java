package com.wteam.carkeeper.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.SystemInfoVo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lhb on 2016/5/6.
 */
public class SystemInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

    private Context context;
    /*按时间顺序，越靠近当前时间越靠前（时间升序）*/
    private List<Object> full = new ArrayList<Object>();

    private String preDate;

    public SystemInfoAdapter(Context context, List<SystemInfoVo> arrayList) {
        this.context = context;

        if(arrayList == null || arrayList.size() == 0) {
            full.add("暂无系统信息！");
            return;
        }

        full.clear();
        for(SystemInfoVo systemInfoVo:arrayList) {
            String date = systemInfoVo.getDate();
            if(!date.equals(preDate)) {
                full.add(date);
                preDate = date;
            }
            full.add(systemInfoVo);
        }
    }

    @Override
    public int getCount() {
        return full.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return full.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object object = full.get(position);
        if(object instanceof String) {
            TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_header,null);
            view.setText((String)object);
            return view;
        } else {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_system_info_content,null);
            CircleImageView system_msg_image = (CircleImageView) linearLayout.findViewById(R.id.system_msg_image);
            TextView system_msg_title = (TextView) linearLayout.findViewById(R.id.system_msg_title);
            TextView system_msg_time = (TextView) linearLayout.findViewById(R.id.system_msg_time);
            TextView system_msg_content = (TextView) linearLayout.findViewById(R.id.system_msg_content);

            SystemInfoVo systemInfoVo = (SystemInfoVo) full.get(position);
            system_msg_content.setText(systemInfoVo.getContent());
            system_msg_title.setText(systemInfoVo.getTitle());
            system_msg_time.setText(systemInfoVo.getCreateTime().substring(11));

            return linearLayout;
        }
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == 1;
    }


    @Override
    public int getItemViewType(int position) {
        int flag = 0;
        if(full.get(position) instanceof String) {
            flag = 1;
        }
        return flag  ;
    }
}
