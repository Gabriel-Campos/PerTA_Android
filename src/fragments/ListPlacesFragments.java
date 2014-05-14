package fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.adapters.ListPlacesAdpter;
import com.devspark.progressfragment.ProgressFragment;
import com.perta.R;
import com.services.RequestJSON;

public class ListPlacesFragments extends ProgressFragment {

	/**
	 * Lista de lugares
	 */
	private ArrayList<HashMap<String, String>> arrayPlaces = null;

	private View mContentView;
	private String query = "";
	private int k = 50;
	private RequestJSON rj = new RequestJSON();
	private static double lat, lng;
	private ListView list;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.fragment_list_places, null);

		list = (ListView) mContentView.findViewById(R.id.listPlaces);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setContentView(mContentView);
		setEmptyText(R.string.empty);

		
		new GetPlaces().execute();
	}

	public static ListPlacesFragments newInstance(double lat, double lng) {
		ListPlacesFragments.lat = lat;
		ListPlacesFragments.lng = lng;
		ListPlacesFragments fragment = new ListPlacesFragments();
		return fragment;
	}

	public void dataUpdate(String query, double lat, double lng) {
		this.query = query;
		new GetPlaces().execute();
	}

	/**
	 * <code>GetPlaces</code> pega a lista de pontos e representa no
	 * PlaceListFragment
	 * 
	 * @author Silas
	 * @see AsyncTask
	 */
	private class GetPlaces extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setContentShown(false);
		}

		@Override
		protected Void doInBackground(Void... params) {
			/**/
			// pega a lista de pontos
			arrayPlaces = rj.requestPlacesJSON(ListPlacesFragments.lat,
					ListPlacesFragments.lng, query, k);

			// limpa a palavra da busca
			query = "";

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setContentShown(true);
			if (arrayPlaces != null) {
				ListPlacesAdpter adapter = new ListPlacesAdpter(getActivity(),
						arrayPlaces);
				list.setAdapter(adapter);
				
			} else {
				setEmptyText(R.string.empty);
			}

		}
	}

}
