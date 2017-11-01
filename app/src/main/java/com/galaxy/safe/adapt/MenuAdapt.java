package com.galaxy.safe.adapt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.R;

public class MenuAdapt extends BaseAdapter {

    private String[] names;
    private Context mContext;
    private int[] icons;

    public MenuAdapt(Context context, String[] names, int[] icons) {
        // TODO Auto-generated constructor stub
        this.names = names;
        this.icons = icons;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return icons.length;
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
            convertView = View.inflate(mContext, R.layout.item_menu, null);
            holder = new ViewHolder();
            holder.tv_setting = (TextView) convertView.findViewById(R.id.tv_setting);
            holder.iv_setting = (ImageView) convertView.findViewById(R.id.iv_setting);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_setting.setText(names[position]);
        holder.iv_setting.setImageResource(icons[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_setting;
        TextView tv_setting;


    }


}
