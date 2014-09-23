package com.codepath.skhgridimagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.skhgridimagesearch.R;
import com.codepath.skhgridimagesearch.models.SearchSettings;

public class SearchFilterActivity extends Activity {

	private SearchSettings settings;
	private Spinner spnImageSize;
	private Spinner spnImageColor;
	private Spinner spnImageType;
	private EditText etSite;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_filter);
		
		spnImageSize = (Spinner) findViewById(R.id.spnImageSize);
		spnImageColor = (Spinner) findViewById(R.id.spnImageColor);
		spnImageType = (Spinner) findViewById(R.id.spnImageType);
		etSite = (EditText) findViewById(R.id.etSite);
		
		settings = (SearchSettings) getIntent().getSerializableExtra(SearchActivity.SEARCH_SETTINGS_EXTRA);
		displaySearchSettings();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_filter, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_save_filter) {
			Intent i = new Intent();
			i.putExtra(SearchActivity.SEARCH_SETTINGS_EXTRA, settings = extractSearchSettings());
			
			setResult(RESULT_OK, i);
			Log.d(getClass().getName(), "Returning settings: " + settings);
			
			finish();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private SearchSettings extractSearchSettings() {
		SearchSettings settings = new SearchSettings();
		
		settings.imageSize = (String) spnImageSize.getSelectedItem();
		settings.imageColor = (String) spnImageColor.getSelectedItem();
		settings.imageType = (String) spnImageType.getSelectedItem();
		settings.site = etSite.getText().toString();
		
		return settings;
	}
	
	private void displaySearchSettings() {
		if(settings != null) {
			spnImageSize.setSelection(getPositionInSpinner(spnImageSize, settings.imageSize));
			spnImageColor.setSelection(getPositionInSpinner(spnImageColor, settings.imageColor));
			spnImageType.setSelection(getPositionInSpinner(spnImageType, settings.imageType));
			
			if(settings.site != null) {
				etSite.setText(settings.site);
			}
		}
	}

	public static int getPositionInSpinner(Spinner spinner, String value) {
		Adapter spinnerAdapter = spinner.getAdapter();
		
		if(spinnerAdapter instanceof ArrayAdapter) {
			return ((ArrayAdapter)spinner.getAdapter()).getPosition(value);
		} else {
			for (int index = 0, count = spinnerAdapter.getCount(); index < count; ++index)
		    {
		        if (spinnerAdapter.getItem(index).equals(value))
		        {
		            return index;
		        }
		    }
			
			return -1;
		}
	}
}
