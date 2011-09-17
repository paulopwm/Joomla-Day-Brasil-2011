package com.JoomlaDay;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramacaoAdapter extends BaseAdapter {

	private Activity activity;
    private List<Map<String, Object>> data;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader; 
    
	public ProgramacaoAdapter(Activity a, List<Map<String, Object>> eventos) {
		activity = a;
        data = eventos;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        public TextView ProgTitle;
		public TextView ProgLocal;
		public TextView ProgDate;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
        if(convertView == null){
            vi = inflater.inflate(R.layout.programacao_item, null);
            holder = new ViewHolder();
            holder.ProgTitle = (TextView)vi.findViewById(R.id.ProgTitle);
            holder.ProgLocal = (TextView)vi.findViewById(R.id.ProgLocal);
            holder.ProgDate = (TextView)vi.findViewById(R.id.ProgDate);
            
            vi.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)vi.getTag();
        }
        Log.i("INIT", data.get(position).get("title").toString());
        Log.i("INIT", data.get(position).get("local").toString());
        Log.i("INIT", data.get(position).get("datetime").toString());
        
        holder.ProgTitle.setText(data.get(position).get("title").toString());
        holder.ProgLocal.setText(data.get(position).get("local").toString());
        holder.ProgDate.setText(data.get(position).get("datetime").toString());
        return vi;
    }

}
