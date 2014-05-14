package com.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <code>RequestJSON</code> é responsável por fazer todas as requisições JSON do
 * aplicativo
 * 
 * @author Silas
 * @version 1.0
 */
public class RequestJSON {

	/**
	 * URL do pontos no Servidor da UEFES
	 */
	private static final String URL_PLACES = "http://adam.uefs.br/perta/api/geocode/json";

	public static final String PLACE_ID = "id", PLACE_NAME = "name",
			PLACE_CATEGORY = "category", PLACE_SUBCATEGORY = "subcategory";

	/**
	 * Retorna o JSON de pontos em formato String. Esse método recebe como
	 * argumento latitude e longitude do usuário, a palavra chave da busca e
	 * numero de pontos que deve ser retornado
	 * 
	 * @param lat
	 * @param lng
	 * @param query
	 * @param k
	 * @return
	 */
	public ArrayList<HashMap<String, String>> requestPlacesJSON(double lat,
			double lng, String query, int k) {

		// inicializando a resposta
		String response = null;

		/*
		 * Lista de parametros da URL
		 */
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lat", String.valueOf(lat)));
		params.add(new BasicNameValuePair("lgt", String.valueOf(lng)));
		params.add(new BasicNameValuePair("query", query));
		params.add(new BasicNameValuePair("k", String.valueOf(k)));

		try {
			// http client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			// tranforma a lista de parametros em String
			String paramString = URLEncodedUtils.format(params, "utf-8");

			// Cria um objeto de requisição do tipo GET usando a URL e os
			// paramentros
			HttpGet httpGet = new HttpGet(URL_PLACES + "?" + paramString);

			// faz a requisição utilizando o objeto de requisição
			httpResponse = httpClient.execute(httpGet);

			// pega a entidade da requisição
			httpEntity = httpResponse.getEntity();

			// transfoma requisição em String
			response = EntityUtils.toString(httpEntity, "UTF-8");

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// retorna a resposta
		return parsePlaceJSON(response);

	}

	/**
	 * Método responsável por transforma a string da requisição em Json e depois
	 * em um ArrayList de HashMap
	 * 
	 * @param resp
	 * @return
	 */
	private ArrayList<HashMap<String, String>> parsePlaceJSON(String resp) {

		if (resp != null) {
			ArrayList<HashMap<String, String>> arrayPlaces = new ArrayList<>();

			try {
				// transforma o objeto string da requisição em objeto JSON Array
				JSONArray jsonArray = new JSONArray(resp);
				// percorre o jsonArray
				for (int i = 0; i < jsonArray.length(); i++) {
					// pega um ponto no array
					JSONObject p = jsonArray.getJSONObject(i);

					// pega o id do ponto
					String id = p.getString(PLACE_ID);
					// pega o nome do ponto
					String name = p.getString(PLACE_NAME);
					// pega a categoria do ponto
					String category = p.getString(PLACE_CATEGORY);
					// pega a subcateoria do ponto
					String subcategory = p.getString(PLACE_SUBCATEGORY);

					// objeto aux para armazena os dados do pontos
					HashMap<String, String> place = new HashMap<>();
					// adiciona o id do ponto ao objeto aux
					place.put(PLACE_ID, id);

					/*
					 * Essa parte é responsável por definir o titulo e categoria
					 * de acordo se tem categoria, se tem subcategoria ou se tem
					 * nome
					 */

					if (name.length() == 0) {
						place.put(PLACE_NAME, subcategory);
						place.put(PLACE_CATEGORY, category);
					} else {
						place.put(PLACE_NAME, name);
						if (category.length() == 0) {
							place.put(PLACE_CATEGORY, "Sem categoria");
						} else {
							if (subcategory == "") {
								place.put(PLACE_CATEGORY, category);
							} else {
								place.put(PLACE_CATEGORY, subcategory);
							}
						}
					}

					// adiciona o nome na lista de lugares
					arrayPlaces.add(place);

				}

				return arrayPlaces;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return null;
	}

}
