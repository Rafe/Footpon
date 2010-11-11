package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FootponListActivity extends Activity {
	
	FootponListActivity footponListActivity = this;
	ListView listView;
	ArrayList<Footpon> footpons;
	
	IFootponService service;
	
	public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.footpon_list);
			
			service = FootponServiceFactory.getService();
			footpons = service.getFootponsInAreaServer(40.75916, -73.984491);
			
			listView = (ListView) findViewById(R.id.footponlist);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(new FootponAdapter(this, R.layout.footpon_listitem, footpons));
			listView.setOnItemClickListener(detailsListener);
	}
	
	public OnItemClickListener detailsListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	    	
	    	//Footpon fp = footpons.get(position);
	    	
	    	Intent i = new Intent(footponListActivity,FootponDetailsActivity.class);
	    	i.putExtra("index",position);
	    	startActivity(i);
	    }
	};
	
}
