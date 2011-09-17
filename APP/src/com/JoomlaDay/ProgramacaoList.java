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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class ProgramacaoList extends GDListActivity {
	
	private ProgressDialog progressDialog;
	private final Handler mHandler = new Handler();
	private Internet internet = new Internet();
	private List<Map<String, Object>> eventos = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> newEventos = new ArrayList<Map<String, Object>>();
	private ProgramacaoAdapter programacao = null;
	private LoaderActionBarItem loaderItem = null;
	private QuickActionWidget mBar;
	private int selectedItem = -1;
	
	GoogleAnalyticsTracker tracker;
	
	final Runnable mUpdateProgramacaoList = new Runnable() {
        public void run() {
        	try {
        		if(newEventos.size() > 0){
        			if ((Boolean) newEventos.get(0).get("internet")){
        				eventos.clear();
        				eventos.addAll(newEventos);
        			}else{
        				showAlert();
        			}
        		}
        		programacao.notifyDataSetChanged();
        		progressDialog.dismiss();
        		if (loaderItem != null){
        			loaderItem.setLoading(false);
        		}
    	    } catch (Exception e) {
    	    	Log.e("Dialog", e.toString());
    	    }
        }
    };
    private void getProgramacao(){
    	try {
			progressDialog = ProgressDialog.show(ProgramacaoList.this, null, this.getString(R.string.loading), false, true);
	    } catch (Exception e) {
	    	Log.e("Dialog", e.toString());
	    }
	    Thread t = new Thread() {
            public void run() {
            	String url = getString(R.string.url_programacao);
            	newEventos = internet.getProgramacao(url, ProgramacaoList.this);
				mHandler.post(mUpdateProgramacaoList);
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
	    tracker.trackPageView("/ProgramacaoList");
	    
	    prepareQuickActionBar();
	    
		initAll();
	}
	
	@Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
            	loaderItem = (LoaderActionBarItem) item;
            	if (internet.checkInternetConnection(ProgramacaoList.this)){
    	    		FileCache.clear();
    	    	}
            	getProgramacao();
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
        return R.layout.twitter;
    }
	
	public void initAll(){
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				onShowBar(arg1);
				selectedItem = arg2;
			}
		});
		programacao = new ProgramacaoAdapter(this, eventos);
		setListAdapter(programacao);
		getProgramacao();
	}
	public void showAlert(){
		AlertDialog.Builder adb = new AlertDialog.Builder(ProgramacaoList.this);
		adb.setTitle(this.getString(R.string.app_name) + " Erro - Sem conexão de dados");
        adb.setMessage("Não foi possível acessar a internet.\n Verifique sua conexão de dados.\n Sem conexão de dados o aplicativo não poderá baixar as informações necessárias.");
        adb.setPositiveButton("Tentar novamente", new OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				if (internet.checkInternetConnection(ProgramacaoList.this)){
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
	                "Programacao", // Label
	                1);       // Value
	    	if (internet.checkInternetConnection(ProgramacaoList.this)){
	    		FileCache.clear();
	    	}
	    	getProgramacao();
	        return true;
	    case R.id.item02:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Exit",  // Action
	                "Programacao", // Label
	                2);       // Value
	        terminate();
	        return true;
	    case R.id.item03:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Preferences",  // Action
	                "Programacao", // Label
	                3);       // Value
	    	preferencias();
	    	return true;
	    case R.id.item04:
	    	tracker.trackEvent(
	                "Clicks",  // Category
	                "Share",  // Action
	                "Programacao", // Label
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
	public void showEvento(String title, String description, String local, String date){
		try{
			Intent EventoItemActivity = new Intent(getBaseContext(), ProgramacaoItemView.class);
			EventoItemActivity.putExtra("progTitle", title);
			EventoItemActivity.putExtra("progLocal", local);
			EventoItemActivity.putExtra("progDescription", description);
			EventoItemActivity.putExtra("progDate", date);
			startActivity(EventoItemActivity);
		}catch (Exception e) {
			Log.e("INIT", e.toString());
		}
	}
	public void share(){
		Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share)+" "+getString(R.string.hashtag)+" "+getString(R.string.app_name)+" "+getString(R.string.url_market)+" @pwmpro");
    	startActivity(Intent.createChooser(intent, "Compartilhar"));
	}
	public void shareEvento(String title, String date) {
		Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
    	
    	String msg = "Confira "+title+" em "+date+" "+getString(R.string.hashtag)+" @pwmpro";
    	if (msg.length() > 140)
    	{
	    	msg = "Confira"+title+" em "+date+" @pwmpro";
    	}
    	intent.putExtra(Intent.EXTRA_TEXT, msg);
    	startActivity(Intent.createChooser(intent, "Compartilhar"));
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
				if (selectedItem > -1){
					try{
						showEvento(
								eventos.get(selectedItem).get("title").toString(),
								eventos.get(selectedItem).get("description").toString(),
								eventos.get(selectedItem).get("local").toString(),
								eventos.get(selectedItem).get("datetime").toString()
						);
					}catch (Exception e) {
						Log.e("INIT", e.toString());
					}
				}
				break;
			case 1:
				if (selectedItem > -1){
					shareEvento(eventos.get(selectedItem).get("title").toString(), eventos.get(selectedItem).get("datetime").toString());
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