package com.perta;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.fragments.ListPlacesFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.services.GPSTracker;
import com.services.Utils;

/**
 * Atividade responsável pela tela inicial
 * 
 * @author Silas
 * @see Activity
 * @see OnQueryTextListener
 */
public class ListPlacesActivity extends Activity implements OnQueryTextListener {

	private MenuItem search;
	private Fragment listFagment;
	private GoogleMap mMap;
	private LatLng myLatLng;
	private GPSTracker gps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);

		// cria um objeto de manipulação de GPS
		gps = new GPSTracker(this);

		if (savedInstanceState == null) {
			// iniciar activity
			init();
		}

	}

	/**
	 * Inicializa o layout principal
	 */
	private void init() {
		if (Utils.isConnected(this)) {
			setContentView(R.layout.activity_list_places);
			setUpMap();
		}
	}

	/**
	 * coloca o mapa na activity
	 */
	private void setUpMap() {
		// verifica se tem algum mapa
		if (mMap == null) {
			// Tenta obter o SupportMapFragment
			mMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();
			// checa se consegiu obter o mapa
			if (mMap != null) {
				// deixa visivel o botão de localização do user
				mMap.setMyLocationEnabled(true);
				setUpCenter();
			}
		}
	}

	/**
	 * Configura o centro do mapa
	 */
	private void setUpCenter() {
		// define um ponto
		myLatLng = new LatLng(gps.getLatitude(), gps.getLongitude());
		// muda o centro do mapa
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 14));
		// executa a lista
		onExecuteList();
	}

	/**
	 * Este método atualiza o a lista de lugares
	 */
	private void onExecuteList() {
		// cria o fragmento da lista
		listFagment = ListPlacesFragment.newInstance(gps.getLatitude(),
				gps.getLongitude());

		getFragmentManager().beginTransaction()
				.add(R.id.listContainer, listFagment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/* definição do menu */
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		/* definição do Search Widget */

		search = menu.findItem(R.id.action_search);

		// pega a ação do botão e a torna um search view
		SearchView searchView = (SearchView) search.getActionView();
		// define o visivel o icone do campo de busca
		searchView.setIconifiedByDefault(true);
		// define a dica do campo de busca
		searchView.setQueryHint(getResources().getString(R.string.search_hint));
		// deine a classe responsavel pelos os eventos do searchview
		searchView.setOnQueryTextListener(this);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.action_refresh:
			onRefreshList();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// fecha o layout widget
		MenuItemCompat.collapseActionView(search);
		onRefreshList(query);
		return true;
	}

	/**
	 * Atualiza a lista, sem buscar por lugar
	 */
	private void onRefreshList() {
		onRefreshList("");
	}

	/**
	 * Atualiza a lista, buscando por lugar
	 * 
	 * @param query
	 */
	private void onRefreshList(String query) {
		// atualiza a localização
		gps.getLocation();
		// atualiza a lista
		((ListPlacesFragment) listFagment).dataUpdate(query, gps.getLatitude(),
				gps.getLongitude());
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onBackPressed() {
		// verifica se o search widget está visivel
		if (MenuItemCompat.isActionViewExpanded(search))
			// fecha ele
			MenuItemCompat.collapseActionView(search);
		else
			// deixa o evento padrão
			super.onBackPressed();
	}

}
