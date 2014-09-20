package com.codepath.skhgridimagesearch.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageResult implements Serializable {
	private static final long serialVersionUID = -3845529463181327025L;

	public String fullUrl;
	public String thumbUrl;
	public String title;
	public int width;
	public int height;
	
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
			this.title = json.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static List<ImageResult> fromJSONArray(JSONArray array) {
		List<ImageResult> results = new ArrayList<ImageResult>();
		
		for(int i=0;i<array.length();i++) {
			try {
				results.add(new ImageResult(array.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return results;
	}
}
