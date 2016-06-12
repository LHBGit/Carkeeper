package com.wteam.carkeeper.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.GasOrderVo;
import com.wteam.carkeeper.network.UrlManagement;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderInfoAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
    private Context context;
    /*按时间顺序，越靠近当前时间越靠前（时间升序）*/
    private List<Object> full = new ArrayList<Object>();

    private String preDate;

    public OrderInfoAdapter(Context context, List<GasOrderVo> arrayList) {
        this.context = context;

        if(arrayList == null || arrayList.size() == 0) {
            full.add("暂无订单信息！");
            return;
        }

        full.clear();
        for(GasOrderVo gasOrderVo:arrayList) {
            String date = gasOrderVo.getCreateTime();
            if(!date.equals(preDate)) {
                full.add(date);
                preDate = date;
            }
            full.add(gasOrderVo);
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
            TextView view = (TextView)LayoutInflater.from(context).inflate(R.layout.item_header,null);
            view.setText((String)object);
            return view;
        } else {
            RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_order_management,null);
            TextView order_num = (TextView) relativeLayout.findViewById(R.id.order_num);
            TextView reservation_time = (TextView) relativeLayout.findViewById(R.id.reservation_time);
            TextView gas_station_name = (TextView) relativeLayout.findViewById(R.id.gas_station_name);
            TextView gas_num = (TextView) relativeLayout.findViewById(R.id.gas_num);
            ImageView qr_image = (ImageView) relativeLayout.findViewById(R.id.qr_image);

            GasOrderVo gasOrderVo = (GasOrderVo) full.get(position);

            String orderNum = gasOrderVo.getOrderNum();
            orderNum = orderNum.substring(0,8) + " **** " + orderNum.substring(orderNum.length()-8,orderNum.length());
            order_num.setText(orderNum);
            reservation_time.setText(gasOrderVo.getReserveTime());
            gas_station_name.setText(gasOrderVo.getGasStationName() + position);
            gas_num.setText(gasOrderVo.getAmountOfGasoline() + "元");


            String qrCodeString = UrlManagement.GET_GAS_ORDER_BY_ORDER_ID + "?gasOrderVo={\"gasOrderId\":\"" + gasOrderVo.getGasOrderId() + "\"}";
            Bitmap bitmap = EncodingUtils.createQRCode(qrCodeString,200,200,null);
            qr_image.setImageBitmap(bitmap);
            return  relativeLayout;
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
