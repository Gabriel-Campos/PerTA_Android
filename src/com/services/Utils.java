package com.services;

import java.util.HashMap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <code>Utils</code> cont�m v�rios m�todos uteis para o desenvolvimento da
 * aplica��o
 * 
 * @author Silas
 * 
 */
public class Utils {

	/**
	 * Retorna <code>true</code> dispositivo tiver conex�o de internet ativa
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
	 * Lista de categorias com suas tradu��es certas
	 */
	private static HashMap<String, String> translation = new HashMap<String, String>() {

		private static final long serialVersionUID = 1L;

		{
			put("amenity", "Eventos");
			put("natural", "Natureza");
			put("shop", "Lojas e Servi�o");
			put("tourism", "tourism");
		}
	};

	/**
	 * Verifica se a <code>String</code> passada como argumento est� lista de
	 * tradu��o, se tiver retorna sua tradu��o
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
