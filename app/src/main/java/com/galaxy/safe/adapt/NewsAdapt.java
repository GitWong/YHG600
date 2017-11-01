package com.galaxy.safe.adapt;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.galaxy.safe.Bean.News;
import com.galaxy.safe.R;

import java.util.List;

public class NewsAdapt extends BaseAdapter {

    public List<News.DatasBean> mList;
    private Context mContext;


    public NewsAdapt(Context context, List<News.DatasBean> list) {
        // TODO Auto-generated constructor stub
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
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
            convertView = View.inflate(mContext, R.layout.item_news, null);
            holder = new ViewHolder();
            holder.tv_taskname = (TextView) convertView.findViewById(R.id.tv_taskname);
            holder.tv_taskno = (TextView) convertView.findViewById(R.id.tv_taskno);
            holder.tv_people = (TextView) convertView.findViewById(R.id.tv_people);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_taskname.setText("标题:" + mList.get(position).getNews_title()
        );
        holder.tv_taskno.setText(mList.get(position).getNews_content());
        holder.tv_people.setText("发布单位:" + mList.get(position).getPublisher());
        holder.tv_time.setText("日期:" + mList.get(position).getPublish_date());

        return convertView;
    }

    class ViewHolder {

        TextView tv_taskname, tv_taskno, tv_people, tv_time;


    }


}
