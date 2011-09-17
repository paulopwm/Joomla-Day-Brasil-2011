package com.JoomlaDay;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TwitterAdapter extends BaseAdapter {

	private Activity activity;
    private List<Map<String, Object>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader; 
    
	public TwitterAdapter(Activity a, List<Map<String, Object>> tweets) {
		activity = a;
        data = tweets;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int arg0) {		
		return data.get(arg0);
	}

	public long getItemId(int arg0) {
		return data.get(arg0).hashCode();
	}
	
	public static class ViewHolder{
        public TextView TwtText;
		public TextView TwtAuthor;
		public TextView TwtDate;
		public ImageView TwtImg;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
        if(convertView == null){
            vi = inflater.inflate(R.layout.twitter_item, null);
            holder = new ViewHolder();
            holder.TwtText = (TextView)vi.findViewById(R.id.TwtText);
            holder.TwtAuthor = (TextView)vi.findViewById(R.id.TwtAuthor);
            holder.TwtDate = (TextView)vi.findViewById(R.id.TwtDate);
            holder.TwtImg = (ImageView)vi.findViewById(R.id.TwtImg);
            
            vi.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)vi.getTag();
        }
        holder.TwtText.setText(data.get(position).get("text").toString());
        holder.TwtAuthor.setText(data.get(position).get("author").toString());
        holder.TwtDate.setText(data.get(position).get("datetime").toString());
        holder.TwtImg.setTag(data.get(position).get("image").toString());
        imageLoader.DisplayImage(data.get(position).get("image").toString(), activity, holder.TwtImg);
        return vi;
    }

}
