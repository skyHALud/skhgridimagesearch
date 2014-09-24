package com.codepath.skhgridimagesearch.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.codepath.skhgridimagesearch.R;
import com.codepath.skhgridimagesearch.adapters.ImageResultsAdapter;
import com.codepath.skhgridimagesearch.models.ImageResult;
import com.codepath.skhgridimagesearch.models.SearchSettings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class SearchActivity extends Activity {

	private static final String RESPONSE_DATA_KEY = "responseData";
	public static final int REQUEST_CODE_SEARCHFILTER = 666;
	public static final String ANY_CHOICE = "any"; // This needs to match the arrays from string.xml
	protected static String query;
	
	private GridView gvResults;
	private List<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;

	private SearchSettings settings;

	static final String SEARCH_SETTINGS_EXTRA = "search_settings";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupViews();
        imageResults = new ArrayList<ImageResult>();
        aImageResults = new ImageResultsAdapter(this, imageResults);
        gvResults.setAdapter(aImageResults);
        
        gvResults.setOnScrollListener(new EndlessScrollListener() {
    	    @Override
    	    public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
    	        loadMoreImages(page); 
    	    }
            });
        
        
    }

    private void setupViews() {
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	gvResults.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// Launch the imagedisplayactivity
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				
				// get the image result to display
				ImageResult result = imageResults.get(position);
				
				// pass image result into intent
				i.putExtra("result", result);
				
				// launch the new activity
				startActivity(i);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
        	   SearchActivity.query = query;
        	   doImageSearch();
               return true;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	Intent i = new Intent(SearchActivity.this, SearchFilterActivity.class);
        	
        	if(settings != null) i.putExtra(SEARCH_SETTINGS_EXTRA, settings);
        	startActivityForResult(i, REQUEST_CODE_SEARCHFILTER);
        	
            return true;
        }
        
        return false;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_SEARCHFILTER) {
    		this.settings = (SearchSettings) data.getSerializableExtra(SEARCH_SETTINGS_EXTRA);
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }
    
    // Bound to android:onclick
    public void doImageSearch() {
    	Toast.makeText(this, "Search for: " + query, Toast.LENGTH_SHORT).show();
    	
    	loadMoreImages(); 	
    }

    public void loadMoreImages() {
    	loadMoreImages(0);
    }
    
	public void loadMoreImages(final int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&start=" + (page * 8) + buildQueryFromSettings() +"&q=" + query;
        client.get(searchUrl, new JsonHttpResponseHandler() {
        	@Override
        	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        		JSONArray imageResultsJson = null;
        		
        		Log.i("INFO", "Loading page " + page);
        		
        		try {
        			if(page == 0) {
        				aImageResults.clear();
        			}
        			
					if(response.has(RESPONSE_DATA_KEY) && !response.get(RESPONSE_DATA_KEY).equals(JSONObject.NULL)) {
						imageResultsJson = response.getJSONObject(RESPONSE_DATA_KEY).getJSONArray("results");
						aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJson));
					} else {
						if(page == 0) {
							Toast.makeText(SearchActivity.this, "No results found!", Toast.LENGTH_LONG).show();
						}
					}
				} catch (JSONException e) {
					Log.e(getClass().getName(), e.getMessage(), e);
				}
        		
        		Log.i("INFO", imageResults.toString());
        	}
        });
	}

	private String buildQueryFromSettings() {
		if(settings != null) {
			String query = StringUtils.EMPTY;
			
			query += appendIfSet("imgsz", settings.imageSize);
			query += appendIfSet("imgcolor", settings.imageColor);
			query += appendIfSet("imgtype", settings.imageType);
			
			if(!StringUtils.isEmpty(settings.site)) {
				query += "as_sitesearch=" + settings.site;
			}
			
			return query;
		} else {
			return StringUtils.EMPTY;
		}
	}
	
	private String appendIfSet(String paramName, String paramValue) {
		return isAnyChoice(paramValue) ? StringUtils.EMPTY : "&" + paramName + "=" + paramValue;
	}

	private static final boolean isAnyChoice(String choice) {return choice.equals(ANY_CHOICE);}
}
