package com.services;

import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <code>Utils</code> contém vários métodos uteis para o desenvolvimento da
 * aplicação
 * 
 * @author Silas
 * 
 */
public class Utils {

	/**
	 * Retorna <code>true</code> dispositivo tiver conexão de internet ativa
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isConnected(Context c) {
		
		ConnectivityManager connMgr = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Lista de categorias com suas traduções certas
	 */
	private static HashMap<String, String> translation = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{
			put("amenity", "Eventos");
			put("natural", "Natureza");
			put("shop", "Lojas e Serviço");
			put("tourism", "tourism");
		}
	};

	/**
	 * Verifica se a <code>String</code> passada como argumento está lista de
	 * tradução, se tiver retorna sua tradução
	 * 
	 * @param word
	 * @return
	 */
	public static String traslate(String word) {

		if (translation.containsKey(word)) {
			return translation.get(word);
		}

		return word;
	}

}
