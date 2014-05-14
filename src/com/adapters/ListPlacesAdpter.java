package com.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.perta.R;
import com.services.Utils;

/**
 * Lista personalizada do PerTA
 * @author Silas
 *
 */
public class ListPlacesAdpter extends BaseAdapter {

	private ArrayList<HashMap<String, String>> arrayPlaces;
	private static LayoutInflater inflater = null;
	private final String PLACE_ID = "id", PLACE_NAME = "name",
			PLACE_CATEGORY = "category";
	private Typeface tf;
	
	
	public ListPlacesAdpter(Activity a, ArrayList<HashMap<String, String>> array) {
		arrayPlaces = array;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		tf = Typeface.createFromAsset(a.getAssets(), "fonts/OpenSans-Light.ttf");
	}

	@Override
	public int getCount() {
		return arrayPlaces.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayPlaces.get(position);
	}

	@Override
	public long getItemId(int position) {
		HashMap<String, String> place = new HashMap<>();
		place = arrayPlaces.get(position);
		return Long.parseLong(place.get(PLACE_ID));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_row, null);
		
		TextView title = (TextView) vi.findViewById(R.id.title); // title
		TextView category = (TextView) vi.findViewById(R.id.category); // artist
																		// name
		
		HashMap<String, String> place = new HashMap<>();
		place = arrayPlaces.get(position);

		// Setting all values in listview
		title.setText(Utils.traslate(place.get(PLACE_NAME)));
		title.setTypeface(tf);
		category.setText(Utils.traslate(place.get(PLACE_CATEGORY)));
		category.setTypeface(tf);
		
		return vi;
	}

}
