package com.JoomlaDay;

import greendroid.app.ActionBarActivity;
import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class Main extends GDActivity {
	
	GoogleAnalyticsTracker tracker;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setTitle(getString(R.string.app_name));
                
        addActionBarItem(Type.Share, R.id.action_bar_share);
        addActionBarItem(Type.Info, R.id.action_bar_info);
        
        // Analytics
	    tracker = GoogleAnalyticsTracker.getInstance();
		// Start the tracker and dispatch each 20 seconds
	    tracker.start(getString(R.string.analytics), 20, this);
	    // Page Tracker
	    tracker.trackPageView("/Main");
        
	    
	    GridView gridview = (GridView) findViewById(R.id.gridView);
	    gridview.setAdapter(new MenuItensAdapter(this));
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            switch (position) {
				case 0:
					Intent intentNews = new Intent(Main.this, NewsList.class);
					intentNews.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getString(R.string.noticias_title));
	                startActivity(intentNews);
					break;
				case 1:
					Intent intentJogos = new Intent(Main.this, ProgramacaoList.class);
					intentJogos.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getString(R.string.programacao_title));
	                startActivity(intentJogos);
					break;
				case 2:
					Intent intentClassificacao = new Intent(Main.this, OficialList.class);
					intentClassificacao.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getString(R.string.twitteroficial_title));
	                startActivity(intentClassificacao);
					break;
				case 3:
					Intent intentJogadores = new Intent(Main.this, HashtagList.class);
					intentJogadores.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getString(R.string.twitterhashtag_title));
	                startActivity(intentJogadores);
					break;
				case 4:
					Intent intentPreferences = new Intent(Main.this, Preferences.class);
					intentPreferences.putExtra(ActionBarActivity.GD_ACTION_BAR_TITLE, getString(R.string.title_preferences));
	                startActivity(intentPreferences);
					break;
				default:
					break;
				}
	        }
	    });
    }
    
    @Override
    public int createLayout() {
        return R.layout.main;
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_info:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.action_bar_share:
            	share();
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
    
    public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu2, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
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
		Toast.makeText(this, "Obrigado por utilizar o "+this.getString(R.string.app_name), Toast.LENGTH_LONG).show();
		super.onDestroy();
		this.finish();
    }
	public void preferencias(){
		Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
		startActivity(settingsActivity);
	}
	public void share(){
		Intent intent = new Intent(Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
    	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share)+" "+getString(R.string.hashtag)+" "+getString(R.string.app_name)+" "+getString(R.string.url_market)+" @pwmpro");
    	startActivity(Intent.createChooser(intent, "Compartilhar"));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    // Stop the tracker when it is no longer needed.
	    tracker.stop();
	}
}