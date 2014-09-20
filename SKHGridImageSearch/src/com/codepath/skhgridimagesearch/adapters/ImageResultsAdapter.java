package com.codepath.skhgridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.skhgridimagesearch.models.ImageResult;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context, List<ImageResult> objects) {
		super(context, android.R.layout.simple_list_item_1, objects);
	}

}
