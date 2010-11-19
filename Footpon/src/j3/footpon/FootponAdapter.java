package j3.footpon;

import j3.footpon.model.Footpon;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FootponAdapter extends ArrayAdapter<Footpon>{
	
	private ArrayList<Footpon> footpons;
	
	public FootponAdapter(Context context,int textViewResourceId,
			ArrayList<Footpon> footpons){
		super(context,textViewResourceId,footpons);
		this.footpons = footpons;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.footpon_listitem, null);
        }
        Footpon f = footpons.get(position);
        if (f != null) {
            TextView storeName = (TextView) v.findViewById(R.id.store_name);
            TextView description = (TextView) v.findViewById(R.id.realDescription);
            TextView points = (TextView) v.findViewById(R.id.points_required);
            if (storeName != null) {
             	storeName.setText(f.getStoreName());                            
            }
            if (description != null) {
               	description.setText(f.getRealDescription());
            }
            if (points != null) {
               	points.setText(String.valueOf(f.getPointsRequired()));
            }
        }
        
        return v;
	}
}
