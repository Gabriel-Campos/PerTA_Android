package com.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * <code>GPSTracker</code> é responsável por todas as interaçãoes com o serviço
 * de localização, tanto por meio do GPS quanto pela rede do dispositivo. Saiba
 * mais em: <a href=
 * "http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/"
 * >android-gps-location-manager-tutorial</a>
 * 
 * @author: Ravi Tamada
 * @version 1.0
 * @see LocationListener
 */

public class GPSTracker extends Service implements LocationListener {

	private final Context mContext;

	// status do GPS
	boolean isGPSEnabled = false;

	// status da rede
	boolean isNetworkEnabled = false;

	// status do uso do gps
	boolean canGetLocation = false;

	Location location; // localização
	double latitude; // latitude
	double longitude; // longitude

	// Distancia minima para atualizar a localização em metros
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metros

	// Tempo minimo para atualizar a localização em milissegundos
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minuto

	// gerenciador de localização
	protected LocationManager locationManager;

	/**
	 * Inicializa o objeto usando o contexto da Atividade
	 * 
	 * @param context
	 * @see Context
	 */
	public GPSTracker(Context context) {
		// inicializa a propriedade mContext
		this.mContext = context;
		// verifica a localização
		getLocation();
	}

	/**
	 * Pega a localização atual do ususario
	 * 
	 * @return
	 */
	public Location getLocation() {
		try {
			// pega o gerenciador de localização do dispositivo
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// pega o status do GPS
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// pega o status da rede
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				// Primeiro pega a localização pela a rede
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.NETWORK_PROVIDER,
							MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}

				// Se o GPS tiver ativado pega a localização por ele
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Para a utilização do GPS na aplicação
	 */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Retorna a latitude da localização do usuário
	 * 
	 * @return
	 */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Retorna a Longitude do usuário
	 * 
	 * @return
	 */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Retorna o status da verificação do gps
	 * 
	 * @return
	 */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}
	
	/**
	 * Retorna o status do GPS
	 * @return
	 */
	public boolean isGPSEnabled() {
		return isGPSEnabled;
	}

	/**
	 * Método que mostra um alerta para mudar a configuração do GPS caso este
	 * não esteja ativado (necessário para melhor experiência)
	 */
	public void showSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("Configurações de GPS");

		// Setting Dialog Message
		alertDialog.setMessage("Para melhor experiência, ative o GPS.");

		// On pressing Settings button
		alertDialog.setPositiveButton("Configuração",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});
		
		// on pressing cancel button
		alertDialog.setNegativeButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}