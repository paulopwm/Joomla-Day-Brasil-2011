package com.JoomlaDay;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBarItem;
import greendroid.widget.ActionBarItem.Type;
import greendroid.widget.AsyncImageView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ProgramacaoItemView extends GDActivity {
    
	private static String progTitle = "";
	private static String progLocal = "";
	private static String progDescription = "";
	private static String progDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.programacao_item_view);
        addActionBarItem(Type.Share, R.id.action_bar_share);
        setTitle(getString(R.string.app_name));
        
        Bundle extras = getIntent().getExtras(); 
		if(extras != null)
		{
			progTitle = extras.getString("progTitle");
			progLocal = extras.getString("progLocal");
			progDescription = extras.getString("progDescription");
			progDate = extras.getString("progDate");
			
			TextView title = (TextView) findViewById(R.id.ProgTitle);
			TextView local = (TextView) findViewById(R.id.ProgLocalDate);
			TextView description = (TextView) findViewById(R.id.ProgDescription);
			
			title.setText(progTitle);
			local.setText(progLocal+" em "+progDate);
			description.setText(progDescription);
		}
        
    }
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_share:
            	shareEvento(progTitle, progDate);
                break;

            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }
    public void navegador(String url){
		Intent WebActivity = new Intent(getBaseContext(), WebActivity.class);
		WebActivity.putExtra("url", url);
		startActivity(WebActivity);
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
