package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.User;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class FootponListActivity extends Activity {
	FootponListActivity footponListActivity = this;
	ListView listView;
	EditText filterText = null;
	ArrayList<Footpon> footpons = null;
	FootponAdapter adapter;
	String username;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.footpon_list);
		
		filterText = (EditText) findViewById(R.id.search_box);
	    filterText.addTextChangedListener(filterTextWatcher);
	    
		SharedPreferences share = getSharedPreferences(User.SHARE_USER_INF_TAG, 0);
		username = share.getString(User.SHARE_USERNAME, "");
		
	    if(username.equals("")){
	    	Log.e("LIST_ERROR", "NO username");
	    	startActivity(new Intent(footponListActivity,Login.class));
	    	return;
	    }
	    
		getFootponSource(username);
	}
	
	//get Footpon Source and show List
	//using callback is for location information...but getInstance is better
	protected void getFootponSource(String username) {
		footpons = FootponServiceFactory.getService().getInstance();
		showList(footpons);
	}
	
	protected void showList(ArrayList<Footpon> footpons){
		if(footpons != null) {
			adapter = new FootponAdapter(this, R.layout.footpon_listitem, footpons);
			
			listView = (ListView) findViewById(R.id.footponlist);
			listView.setTextFilterEnabled(true);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(detailsListener);
		}
	}
	
	public OnItemClickListener detailsListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
	            int position, long id) {
			SharedPreferences share = getSharedPreferences(User.SHARE_USER_INF_TAG, 0);
			username = share.getString(User.SHARE_USERNAME, "");
			
	    	Intent i = new Intent(footponListActivity, FootponDetailsActivity.class);
	    	Bundle bundle=new Bundle();
	    	bundle.putLong("id", footpons.get(position).getID());
	    	//FootponServiceFactory.getService().redeemFootpon(username, footpons.get(position).getID());
	    	bundle.putBoolean("own",true);
	    	i.putExtras(bundle);
	    	startActivity(i);
	    }
	};
	
	protected TextWatcher filterTextWatcher = new TextWatcher() {
	    public void afterTextChanged(Editable s) {
	    }
	    
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	    	adapter.getFilter().filter(s);
	    	Log.e("log_tag", "changed ");
	    }
	};
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}

}
