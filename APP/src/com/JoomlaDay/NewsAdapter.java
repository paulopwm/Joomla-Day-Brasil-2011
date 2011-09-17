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

public class NewsAdapter extends BaseAdapter {

	private Activity activity;
    private List<Map<String, Object>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader; 
    
	public NewsAdapter(Activity a, List<Map<String, Object>> partidas) {
		activity = a;
        data = partidas;
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
        public TextView NewsTitle;
		public TextView NewsAuthor;
		public TextView NewsDate;
		public ImageView NewsImg;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
        if(convertView == null){
            vi = inflater.inflate(R.layout.news_item, null);
            holder = new ViewHolder();
            holder.NewsTitle = (TextView)vi.findViewById(R.id.NewsTitle);
            holder.NewsAuthor = (TextView)vi.findViewById(R.id.NewsAuthor);
            holder.NewsDate = (TextView)vi.findViewById(R.id.NewsDate);
            holder.NewsImg = (ImageView)vi.findViewById(R.id.NewsImg);
            
            vi.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)vi.getTag();
        }
        holder.NewsTitle.setText(data.get(position).get("title").toString());
        holder.NewsAuthor.setText(data.get(position).get("author").toString());
        holder.NewsDate.setText(data.get(position).get("datetime").toString());
        holder.NewsImg.setTag(data.get(position).get("image").toString());
        imageLoader.DisplayImage(data.get(position).get("image").toString(), activity, holder.NewsImg);
        return vi;
    }

}
