package com.fragments;

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

public class ListPlacesFragment extends ProgressFragment {

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

	/**
	 * Cria um ListPlacesFragment passando a latitude e longitude para a lista
	 * já carregue com os pontos atualizados
	 * 
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static ListPlacesFragment newInstance(double lat, double lng) {
		ListPlacesFragment.lat = lat;
		ListPlacesFragment.lng = lng;
		ListPlacesFragment fragment = new ListPlacesFragment();
		return fragment;
	}

	/**
	 * Método que atualiza os pontos da lista apartir dos parametros
	 * 
	 * @param query
	 * @param lat
	 * @param lng
	 */
	public void dataUpdate(String query, double lat, double lng) {
		this.query = query;
		//atualiza a lista
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
			arrayPlaces = rj.requestPlacesJSON(ListPlacesFragment.lat,
					ListPlacesFragment.lng, query, k);

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
