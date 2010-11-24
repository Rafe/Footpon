package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FootponListActivity extends Activity {
	FootponListActivity footponListActivity = this;
	ListView listView;
	ArrayList<Footpon> footpons;
	ArrayList<Long> IDs = new ArrayList<Long>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footpon_list);
		
		footpons = FootponServiceFactory.getService().getMyFootpons();
		
		if(footpons != null) {
			for(int i=0;i<footpons.size();i++) {
				IDs.add(footpons.get(i).getID());
			}
			
			listView = (ListView) findViewById(R.id.footponlist);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(new FootponAdapter(this, R.layout.footpon_listitem, footpons));
			listView.setOnItemClickListener(detailsListener);
		}
	}
	
	public OnItemClickListener detailsListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	    	Intent i = new Intent(footponListActivity, FootponDetailsActivity.class);
	    	Bundle bundle=new Bundle();
	    	bundle.putLong("id", IDs.get(position));
	    	i.putExtras(bundle);
	    	startActivity(i);
	    }
	};
}
