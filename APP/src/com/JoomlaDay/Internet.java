package com.JoomlaDay;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class Internet {
	public boolean checkInternetConnection(Context context) {
    	ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService (Context.CONNECTIVITY_SERVICE);
    	// ARE WE CONNECTED TO THE NET
    	if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
    		return true;
    	} else {
    		Log.v("INTERNET", "Internet Connection Not Present");
    		return false;
    	}
    }
	public String getInternet(Context context, String site){
    	// The data that is retrieved 
    	String result = null;
    	FileCache cache = new FileCache(context);
    	File cacheFile = cache.getFile(site);
    	String fileName = cacheFile.getAbsolutePath();
    	
    	if (this.checkInternetConnection(context)){
    		if (!cache.verifyCache(fileName)){
		    	try {
		    		// This assumes that you have a URL from which the response will come
			    	URL url = new URL(site);
			    	     
			    	// Open a connection to the URL and obtain a buffered input stream
			    	URLConnection connection = url.openConnection();
			    	InputStream inputStream = connection.getInputStream();
			    	BufferedInputStream bufferedInput = new BufferedInputStream(inputStream, 8);
			    	     
			    	// Read the response into a byte array
			    	ByteArrayBuffer byteArray = new ByteArrayBuffer(50);
			    	int current = 0;
			    	while((current = bufferedInput.read()) != -1){
			    		byteArray.append((byte)current);
			    	}
			
			    	// Construct a String object from the byte array containing the response
			    	result = new String(byteArray.toByteArray());
			    	try {
	        			if (cache.saveFile(result)){
	        				Log.i("INTERNET", "Cache salvo!");
	        			}else{
	        				Log.i("INTERNET", "Erro no salvar o cache!");
	        			}
	        		}catch (Exception e) {
	        			result = "seminternet";
	        			Log.e("INTERNET", e.toString());
	    			}
		    	} catch (Exception e) {
		    		result = "seminternet";
		    		Log.e("INTERNET", e.toString());
		    	}
    		}else{
    			try {
        			result = cache.readFile(fileName);
        		}catch (Exception e) {
        			result = "seminternet";
        			Log.e("INTERNET", e.toString());
    			}
    		}
    	}else{
    		try {
    			result = cache.readFile(fileName);
    			if (result.length() == 0){
    				result = "seminternet";
    			}
    		}catch (Exception e) {
    			result = "seminternet";
    			Log.e("INTERNET", e.toString());
			}
    	}
    	Log.i("INTERNET", result);
    	return(result);
    }
	
	public ArrayList<Map<String, String>> JSON(String url, Context context){
		ArrayList<Map<String, String>> lista = new ArrayList<Map<String, String>>();
		
		JSONObject myJSON = new JSONObject();
		JSONArray names = null;
		String restWebServerResponse = getInternet(context, url);
		if (restWebServerResponse != "seminternet"){
			try{  
				JSONArray myJSONArray = new JSONArray(restWebServerResponse);
				for (int i = 0; i < myJSONArray.length(); i++){
					Map<String, String> data = new HashMap<String, String>();
					myJSON = new JSONObject(myJSONArray.getString(i));
					names = myJSON.names();
					for (int j = 0; j < names.length(); j++){
						Log.i("JSON Items", names.getString(j)+", "+myJSON.getString(names.getString(j)));
						data.put(names.getString(j), myJSON.getString(names.getString(j)));
					}
					data.put("internet", "true");
					lista.add(data);
				}
				Log.i("JSON Lista", lista.toString());
			}
			catch (JSONException e) {  
				Log.e("JSON", e.getMessage());
			}
		}else{
			Map<String, String> data = new HashMap<String, String>();
			data.put("internet", "false");
			lista.add(data);
		}
		if (lista.size() > 0){
			return lista;
		}else{
			Map<String, String> data = new HashMap<String, String>();
			data.put("internet", "false");
			lista.add(data);
			return lista;
		}
    }
	
	public List<Map<String, Object>> getTwitter(String url, Context context){
		ArrayList<Map<String, String>> json = this.JSON(url, context);
		List<Map<String, Object>> tweets = new ArrayList<Map<String, Object>>();
		Map<String, Object> data;
		if (json.get(0).get("internet").equals("true")){
			for (int i = 0; i < json.size(); i++){
				try {
					data = new HashMap<String, Object>();
					Log.i("JSON Lista Recebida", i+" -> "+json.get(i).toString());
					Tweet tweet = new Tweet(json.get(i).get("texto"), json.get(i).get("autor"), json.get(i).get("imagem"), json.get(i).get("date"));
					
					data.put("internet", true);
					data.put("text", tweet.getText());
					data.put("author", tweet.getAuthor());
					data.put("image", tweet.getImage());
					data.put("datetime", tweet.getDate());
					
					tweets.add(data);
				} catch (Exception e) {
					Log.e("JSON - Tweets", e.getMessage()+" "+e.getStackTrace());
				}
			}
		}else{
			data = new HashMap<String, Object>();
			data.put("internet", false);
			tweets.add(data);
		}
		return tweets;
	}
	
	public List<Map<String, Object>> getNoticias(String url, Context context){
		ArrayList<Map<String, String>> json = this.JSON(url, context);
		List<Map<String, Object>> noticias = new ArrayList<Map<String, Object>>();
		Map<String, Object> data;
		if (json.get(0).get("internet").equals("true")){
			for (int i = 0; i < json.size(); i++){
				try {
					data = new HashMap<String, Object>();
					Log.i("JSON Lista Recebida", i+" -> "+json.get(i).toString());
					Noticias noticia = new Noticias(json.get(i).get("title"), json.get(i).get("description"), json.get(i).get("author"), json.get(i).get("link"), json.get(i).get("image"), json.get(i).get("datetime"));
					
					data.put("internet", true);
					data.put("title", noticia.getTitle());
					data.put("author", noticia.getAuthor());
					data.put("description", noticia.getDescription());
					data.put("image", noticia.getImage());
					data.put("datetime", noticia.getDate());
					data.put("link", noticia.getLink());
					
					noticias.add(data);
				} catch (Exception e) {
					Log.e("JSON - Noticias", e.getMessage()+" "+e.getStackTrace());
				}
			}
		}else{
			data = new HashMap<String, Object>();
			data.put("internet", false);
			noticias.add(data);
		}
		return noticias;
	}
	
	public List<Map<String, Object>> getProgramacao(String url, Context context){
		ArrayList<Map<String, String>> json = this.JSON(url, context);
		List<Map<String, Object>> programacao = new ArrayList<Map<String, Object>>();
		Map<String, Object> data;
		if (json.get(0).get("internet").equals("true")){
			for (int i = 0; i < json.size(); i++){
				try {
					data = new HashMap<String, Object>();
					Log.i("JSON Lista Recebida", i+" -> "+json.get(i).toString());
					Evento evento = new Evento(json.get(i).get("title"), json.get(i).get("text"), json.get(i).get("local"), json.get(i).get("datetime"));
					
					data.put("internet", true);
					data.put("title", evento.getTitle());
					data.put("local", evento.getLocal());
					data.put("description", evento.getDescription());
					data.put("datetime", evento.getDate());
					
					programacao.add(data);
				} catch (Exception e) {
					Log.e("JSON - Programação", e.getMessage()+" "+e.getStackTrace());
				}
			}
		}else{
			data = new HashMap<String, Object>();
			data.put("internet", false);
			programacao.add(data);
		}
		return programacao;
	}
}
