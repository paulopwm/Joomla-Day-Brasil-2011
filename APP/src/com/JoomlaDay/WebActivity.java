package com.JoomlaDay;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebActivity extends GDActivity {
	WebView mWebView;
	final Activity activity = this;
	String url = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null)
		{
			url = extras.getString("url");
		}
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.web);
        
        // Makes Progress bar Visible
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON); 
        mWebView = (WebView) findViewById(R.id.WebView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view, int progress)
            {
                activity.setTitle("Carregando...");
                activity.setProgress(progress * 100);
 
                if(progress == 100)
                    activity.setTitle(R.string.app_name);
            }
        });
        
        mWebView.setWebViewClient(new WebViewClient() {
        	@Override
        	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        	{
        		Toast.makeText(activity.getBaseContext(), description, 5);
        	}
        	
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url)
        	{
        		view.loadUrl(url);
        		return true;
        	}
        });
        
        mWebView.loadUrl(url);
        addActionBarItem(Type.Share, R.id.action_bar_share);
    }
	@Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_share:
            	Intent intent = new Intent(Intent.ACTION_SEND);
            	intent.setType("text/plain");
            	intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            	intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
            	intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name)+" - "+mWebView.getTitle()+" - "+url+" @pwmpro");
            	startActivity(Intent.createChooser(intent, "Compartilhar"));
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
    		mWebView.goBack();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
    public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu1, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.item01:
	    	finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}
