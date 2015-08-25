package com.example.cinetvbox;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class CustomGridAdapter extends BaseAdapter {

	private ArrayList listData;

	private LayoutInflater layoutInflater;

	public CustomGridAdapter(Context context, ArrayList listData) {
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.upcoming_row, null);
			holder = new ViewHolder();
			holder.headlineView = (TextView) convertView.findViewById(R.id.txtmovietitle);
			holder.thumbrating = (TextView) convertView.findViewById(R.id.txtthumbup);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imgmovie);		

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		
		MovieClass newsItem = (MovieClass) listData.get(position);

		holder.headlineView.setText(newsItem.getHeadline());
		holder.thumbrating.setText(newsItem.getThumb()+" want to see this.");

		if (holder.imageView != null) {
			new ImageDownloaderTask(holder.imageView).execute(newsItem.getUrl());
		}

		return convertView;
	}
	
	static class ViewHolder {
		TextView headlineView;
		TextView thumbrating;
		ImageView imageView;
		ImageView imagethumb;
		ImageView imagethumbfill;

	}
}
