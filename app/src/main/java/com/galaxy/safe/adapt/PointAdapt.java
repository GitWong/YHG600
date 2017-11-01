package com.galaxy.safe.adapt;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.Map;

import lecho.lib.hellocharts.model.PointValue;

public class PointAdapt extends BaseAdapter {


    private Context mContext;
    private ArrayList<PointValue> pList;

    public PointAdapt(Context context, ArrayList<PointValue> pList) {
        // TODO Auto-generated constructor stub
        this.pList = pList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_point, null);
            holder = new ViewHolder();
            holder.tv_no = (TextView) convertView.findViewById(R.id.tv_no);
            holder.tv_x = (TextView) convertView.findViewById(R.id.tv_x);
            holder.tv_y = (TextView) convertView.findViewById(R.id.tv_y);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int p=position+1;
        holder.tv_no.setText("点"+"["+p+"]");
        holder.tv_x.setText("("+pList.get(position).getX()+",");
        holder.tv_y.setText(pList.get(position).getY()+")");
        return convertView;
    }

    class ViewHolder {

        TextView tv_no;
        TextView tv_x;
        TextView tv_y;


    }


}
