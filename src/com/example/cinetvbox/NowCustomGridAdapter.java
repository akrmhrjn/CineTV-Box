package com.example.cinetvbox;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class NowCustomGridAdapter extends BaseAdapter {

	private ArrayList listData;

	private LayoutInflater layoutInflater;

	public NowCustomGridAdapter(Context context, ArrayList listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.now_row, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.txtmovietitle);
			holder.ratingView = (TextView) convertView.findViewById(R.id.txtratingnum);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imgmovie);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		MovieClass newsItem = (MovieClass) listData.get(position);

		holder.headlineView.setText(newsItem.getHeadline());
		
		holder.ratingView.setText(newsItem.getRate());

		if (holder.imageView != null) {
			new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
		}

		return convertView;
	}

	static class ViewHolder {
		TextView headlineView;
		TextView ratingView;
		ImageView imageView;
	}
}
