package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponRepository;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class FootponListActivity extends Activity {
	
	ListView listView;
	ArrayList<Footpon> footpons;
	public void onCreate(Bundle savedInstanceState) {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.footpon_list);
			
			footpons = FootponRepository.getFootponsInArea(1, 0, 0, 100);
			
			listView = (ListView) findViewById(R.id.footponlist);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(new FootponAdapter(this, R.layout.footpon_listitem, footpons));
	
	}
}
