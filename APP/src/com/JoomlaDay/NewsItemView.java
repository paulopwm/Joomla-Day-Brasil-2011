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

public class NewsItemView extends GDActivity {
    
	private static String newsTitle = "";
	private static String newsAuthor = "";
	private static String newsLink = "";
	private static String newsDescription = "";
	private static String newsImage = "";
	private static String newsDate = "";
	
    private AsyncImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.newsitem);
        addActionBarItem(Type.Share, R.id.action_bar_share);
        addActionBarItem(Type.Eye, R.id.action_bar_view_info);
        setTitle(getString(R.string.app_name));
        
        Bundle extras = getIntent().getExtras(); 
		if(extras != null)
		{
			newsTitle = extras.getString("newsTitle");
			newsAuthor = extras.getString("newsAuthor");
			newsLink = extras.getString("newsLink");
			newsDescription = extras.getString("newsDescription");
			newsImage = extras.getString("newsImage");
			newsDate = extras.getString("newsDate");
			
			TextView title = (TextView) findViewById(R.id.NewsTitle);
			TextView author = (TextView) findViewById(R.id.NewsAuthorDate);
			TextView description = (TextView) findViewById(R.id.NewsDescription);
			
			title.setText(newsTitle);
			author.setText("Por "+newsAuthor+" em "+newsDate);
			description.setText(newsDescription);
			
			mImageView = (AsyncImageView) findViewById(R.id.imagenews);
		    mImageView.setUrl(newsImage);
		}
        
    }
    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_view_info:
            	navegador(newsLink);
                break;
            case R.id.action_bar_share:
            	shareNews(newsTitle, newsAuthor, newsLink);
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
