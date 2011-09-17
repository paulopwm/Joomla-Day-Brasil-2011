package com.JoomlaDay;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuItensAdapter extends BaseAdapter{
	private Activity mActivity;
	public static final int ACTIVITY_CREATE = 10;

    public MenuItensAdapter(Activity a) {
    	mActivity = a;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if(convertView==null){
			LayoutInflater li = mActivity.getLayoutInflater();
			v = li.inflate(R.layout.main_icons, null);
			
			TextView tv = (TextView)v.findViewById(R.id.icon_text);
			tv.setText(mActivity.getString(mTitles[position]));
			
			ImageView iv = (ImageView)v.findViewById(R.id.icon_image);
			iv.setImageResource(mThumbIds[position]);
		}
		else
		{
			v = convertView;
		}
		return v;
	}

    // references to our images
    private Integer[] mThumbIds = {
		R.drawable.noticias,
        R.drawable.programacao,
        R.drawable.twitter,
        R.drawable.hashtag,
        R.drawable.config
    };
    private Integer[] mTitles = {
    	R.string.noticias_title,
    	R.string.programacao_title,
    	R.string.twitteroficial_title,
    	R.string.twitterhashtag_title,
    	R.string.title_preferences
    };
}
