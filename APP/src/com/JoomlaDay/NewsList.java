package com.JoomlaDay;

import greendroid.app.GDListActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.QuickActionBar;
import greendroid.widget.QuickActionWidget;
import greendroid.widget.QuickActionWidget.OnQuickActionClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class NewsList extends GDListActivity {
	
	private ProgressDialog progressDialog;
	private final Handler mHandler = new Handler();
	private Internet internet = new Internet();
	private List<Map<String, Object>> noticias = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newNoticias = new ArrayList<Map<String, Object>>();
	private NewsAdapter news = null;
	private SharedPreferences prefs = null;
	private LoaderActionBarItem loaderItem = null;
	private QuickActionWidget mBar;
	private int selectedItem = -1;
	
	GoogleAnalyticsTracker tracker;
	
	final Runnable mUpdateNewsList = new Runnable() {
        public void run() {
        	try {
        		if(newNoticias.size() > 0){
        			Log.i("NEWS", newNoticias.get(0)+"");
        			if ((Boolean) newNoticias.get(0).get("internet")){
    					noticias.clear();
    					noticias.addAll(newNoticias);
        			}else{
        				showAlert();
        			}
        		}
        		news.notifyDataSetChanged();
        		progressDialog.dismiss();
        		if (loaderItem != null){
        			loaderItem.setLoading(false);
        		}
    	    } catch (Exception e) {
    	    	Log.e("Dialog", e.toString());
    	    }
        }
    };
    private void getNoticias(){
    	try {
			progressDialog = ProgressDialog.show(NewsList.this, null, this.getString(R.string.loading), false, true);
	    } catch (Exception e) {
	    	Log.e("Dialog", e.toString());
	    }
	    Thread t = new Thread() {
            public void run() {
            	WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            	String qtd_news = "10";
            	if (wifiManager.isWifiEnabled()){
            		qtd_news = prefs.getString("qtd_news_wifi", "15");
            	}else{
            		qtd_news = prefs.getString("qtd_news", "15");
            	}
            	String url = getString(R.string.url_noticias)+"?qtd="+qtd_news;
            	Log.i("URL_NEWS", url);

				newNoticias = internet.getNoticias(url, NewsList.this);
				
				Log.i("Refresh News List", "Comando executados com exito! "+newNoticias.size());
				mHandler.post(mUpdateNewsList);
            }
        };
        t.start();
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addActionBarItem(Type.Refresh, R.id.action_bar_refresh);
		addActionBarItem(Type.Share, R.id.action_bar_share);
		
		// Analytics
	    tracker = GoogleAnalyticsTracker.getInstance();
		// Start the tracker and dispatch each 20 seconds
	    tracker.start(getString(R.string.analytics), 20, this);
	    // Page Tracker
	    tracker.trackPageView("/NewsList");
	    
	    prepareQuickActionBar();
	    
		initAll();
	}
	
	@Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
            	loaderItem = (LoaderActionBarItem) item;
            	if (internet.checkInternetConnection(NewsList.this)){
    	    		FileCache.clear();
    	    	}
    	    	getNoticias();
                break;
            case R.id.action_bar_share:
            	share();
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
	
	@Override
    public int createLayout() {
        return R.layout.news;
    }
	
	public void initAll(){
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//showCustomDialog(noticias.get(arg2));
				onShowBar(arg1);
				selectedItem = arg2;
			}
		});
		news = new NewsAdapter(this, noticias);
		setListAdapter(news);
		getNoticias();
	}
	public void showAlert(){
		AlertDialog.Builder adb = new AlertDialog.Builder(NewsList.this);
		adb.setTitle(this.getString(R.string.app_name) + " Erro - Sem conexão de dados");
        adb.setMessage("Não foi possível acessar a internet.\n Verifique sua conexão de dados.\n Sem conexão de dados o aplicativo não poderá baixar as informações necessárias.");
        adb.setPositiveButton("Tentar novamente", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				if (internet.checkInternetConnection(NewsList.this)){
					initAll();
				}else{
					showAlert();
				}
			}
		});
        adb.setNegativeButton("Cancelar", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				finish();
			}
		});
        adb.show();
	}
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.item01:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Update",  // Action
	                "noticias", // Label
	                1);       // Value
	    	if (internet.checkInternetConnection(NewsList.this)){
	    		FileCache.clear();
	    	}
	    	getNoticias();
	        return true;
	    case R.id.item02:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Exit",  // Action
	                "noticias", // Label
	                2);       // Value
	        terminate();
	        return true;
	    case R.id.item03:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Preferences",  // Action
	                "noticias", // Label
	                3);       // Value
	    	preferencias();
	    	return true;
	    case R.id.item04:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Share",  // Action
	                "noticias", // Label
	                4);       // Value
	    	share();
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	public void terminate()
    {
		this.finish();
    }
	public void preferencias(){
		Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
		startActivity(settingsActivity);
	}
	public void navegador(String url){
		Intent WebActivity = new Intent(getBaseContext(), WebActivity.class);
		WebActivity.putExtra("url", url);
		startActivity(WebActivity);
	}
	public void showNews(String title, String description, String author, String link, String image, String date){
		Intent NewsItemActivity = new Intent(getBaseContext(), NewsItemView.class);
		NewsItemActivity.putExtra("newsTitle", title);
		NewsItemActivity.putExtra("newsAuthor", author);
		NewsItemActivity.putExtra("newsLink", link);
		NewsItemActivity.putExtra("newsDescription", description);
		NewsItemActivity.putExtra("newsImage", image);
		NewsItemActivity.putExtra("newsDate", date);
		startActivity(NewsItemActivity);
	}
	public void share(){
		Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share)+" "+getString(R.string.hashtag)+" "+getString(R.string.app_name)+" "+getString(R.string.url_market)+" @pwmpro");
    	startActivity(Intent.createChooser(intent, "Compartilhar"));
	}
	public void shareNews(String title, String author, String link) {
		Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
    	
    	String msg = "Veja esta notícia: "+title+" "+link+" por "+author+" "+getString(R.string.hashtag)+" @pwmpro";
    	if (msg.length() > 140)
    	{
	    	msg = "Veja esta notícia: "+title+" "+link+" "+getString(R.string.hashtag)+" @pwmpro";
	    	if (msg.length() > 140)
	    	{
	    		msg = title+" "+link+" "+getString(R.string.hashtag)+" @pwmpro";
	    	}    	
    	}
    	intent.putExtra(Intent.EXTRA_TEXT, msg);
    	startActivity(Intent.createChooser(intent, "Compartilhar"));
	}
	public void showCustomDialog(Map<String, Object> map){
		final Dialog dialog = new Dialog(this);
		
    	dialog.setContentView(R.layout.detalhes);//carregando o layout do dialog do xml

    	Button ok = (Button) dialog.findViewById(R.id.bt_ok);//se atentem ao dialog.
    	Button share = (Button) dialog.findViewById(R.id.bt_share);
    	
    	TextView titleNews = (TextView) dialog.findViewById(R.id.title_news);
    	titleNews.setText(map.get("title").toString());
    	TextView descriptionNews = (TextView) dialog.findViewById(R.id.description_news);
    	descriptionNews.setText(map.get("description").toString());
    	TextView linkNews = (TextView) dialog.findViewById(R.id.link_news);
    	linkNews.setText(map.get("link").toString());
    	TextView dateNews = (TextView) dialog.findViewById(R.id.date_news);
    	dateNews.setText(map.get("datetime").toString());
    	    	
    	final String title = map.get("title").toString();
    	final String author = map.get("author").toString();
    	final String link = map.get("link").toString();
    	
    	ok.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			dialog.dismiss();
    		}
    	});
    	share.setOnClickListener(new View.OnClickListener() {
    		public void onClick(View v) {
    			shareNews(title, author, link);
    		}
    	});
    	dialog.show();//mostra o dialog
	}
	
	private void prepareQuickActionBar() {
        mBar = new QuickActionBar(this);
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_eye, R.string.browser_text));
        mBar.addQuickAction(new MyQuickAction(this, R.drawable.gd_action_bar_share, R.string.share_text));
        
        mBar.setOnQuickActionClickListener(mActionListener);
    }
	public void onShowBar(View v) {
        mBar.show(v);
    }
	private OnQuickActionClickListener mActionListener = new OnQuickActionClickListener() {
        public void onQuickActionClicked(QuickActionWidget widget, int position) {
        	switch (position) {
			case 0:
				try{
					if (selectedItem > -1){						
						showNews(
							noticias.get(selectedItem).get("title").toString(), 
							noticias.get(selectedItem).get("description").toString(), 
							noticias.get(selectedItem).get("author").toString(),
							noticias.get(selectedItem).get("link").toString(),
							noticias.get(selectedItem).get("image").toString(),
							noticias.get(selectedItem).get("datetime").toString()
						);
					}
				}catch (Exception e) {
					Log.e("INIT", e.toString());
				}
				break;
			case 1:
				if (selectedItem > -1){
					shareNews(noticias.get(selectedItem).get("title").toString(), noticias.get(selectedItem).get("author").toString(), noticias.get(selectedItem).get("link").toString());
				}else{
					share();
				}
			default:
				break;
			}
        }
    };
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	}
}