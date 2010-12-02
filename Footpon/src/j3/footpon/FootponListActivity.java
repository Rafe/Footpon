package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class FootponListActivity extends Activity {
	FootponListActivity footponListActivity = this;
	ListView listView;
	EditText filterText = null;
	ArrayList<Footpon> footpons;
	ArrayList<Long> IDs = new ArrayList<Long>();
	FootponAdapter adapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footpon_list);
		
		filterText = (EditText) findViewById(R.id.search_box);
	    filterText.addTextChangedListener(filterTextWatcher);
	    
		footpons = FootponServiceFactory.getService().getMyFootpons();
		
		if(footpons != null) {
			adapter = new FootponAdapter(this, R.layout.footpon_listitem, footpons);
			
			for(int i=0;i<footpons.size();i++) {
				IDs.add(footpons.get(i).getID());
			}
			
			listView = (ListView) findViewById(R.id.footponlist);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(detailsListener);
		}
	}
	
	public OnItemClickListener detailsListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
	    	Intent i = new Intent(footponListActivity, FootponDetailsActivity.class);
	    	Bundle bundle=new Bundle();
	    	bundle.putLong("id", IDs.get(position));
	    	bundle.putBoolean("isRedeemed", true);
	    	i.putExtras(bundle);
	    	startActivity(i);
	    }
	};
	
	private TextWatcher filterTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) {
	    }
	    
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	    	adapter.getFilter().filter(s);
	    }
	};
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}

}
