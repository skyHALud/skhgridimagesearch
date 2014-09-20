package com.codepath.skhgridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.skhgridimagesearch.R;
import com.codepath.skhgridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	static class Holder {
		ImageView ivImage;
		TextView tvTitle;
	}
	
	public ImageResultsAdapter(Context context, List<ImageResult> objects) {
		super(context, R.layout.item_image_result, objects);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = getItem(position);
		Holder h = null;
		
		if(convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
			
			h = new Holder();
			h.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
			h.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
			
			h.ivImage.setImageResource(0);
			
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}
		
		h.tvTitle.setText(Html.fromHtml(imageInfo.title));
		Picasso.with(getContext()).load(imageInfo.thumbUrl).into(h.ivImage);
		
		return convertView;
	}
}
