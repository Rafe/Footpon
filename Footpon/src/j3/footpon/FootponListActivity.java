package j3.footpon;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FootponListActivity extends Activity {
	
//	FootponListActivity footponListActivity = this;
//	ListView listView;
//	ArrayList<Footpon> footpons;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background);
		
//		footpons = FootponRepository.getFootponsInArea(1, 0, 0, 100);
		
		ListView list = (ListView) findViewById(R.id.footponlist);
//		list.setTextFilterEnabled(true);
//		list.setAdapter(new FootponAdapter(this, R.layout.footpon_listitem, footpons));
//		list.setOnItemClickListener(detailsListener);
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        
		HashMap<String, Object> hm1 = new HashMap<String, Object>();
        hm1.put("ItemImage", R.drawable.puma);
        hm1.put("ItemTitle", "Puma");
        hm1.put("LastImage", R.drawable.arrow);
        listItem.add(hm1);
        HashMap<String, Object> hm2 = new HashMap<String, Object>();
        hm2.put("ItemImage", R.drawable.dell);
        hm2.put("ItemTitle", "Dell");
        hm2.put("LastImage", R.drawable.arrow);
        listItem.add(hm2);
        HashMap<String, Object> hm3 = new HashMap<String, Object>();
        hm3.put("ItemImage", R.drawable.macys);
        hm3.put("ItemTitle", "Macy's");
        hm3.put("LastImage", R.drawable.arrow);
        listItem.add(hm3);
        
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_coupon,
            new String[] {"ItemImage", "ItemTitle", "LastImage"},
            new int[] {R.id.ItemImage, R.id.ItemTitle, R.id.last}
        );
        
        list.setAdapter(listItemAdapter); 
        
        list.setOnItemClickListener(new OnItemClickListener() {
        	@Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        //      setTitle("Coupon of " + arg2 + arg3 + " selected");
        		Intent myIntent = new Intent(FootponListActivity.this, FootponMapActivity.class);
           	 	startActivity(myIntent);
           	 	
        // 	 	setTitle("Coupon of " + arg2 + arg3 + " selected");  
            }
        });
        
        list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            @Override  
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
    //       	 Intent myIntent = new Intent(FootponListActivity.this, Coupon.class);
    //       	 startActivity(myIntent);
       //         menu.setHeaderTitle("long push");
       //         menu.add(0, 0, 0, "push 0");  
       //         menu.add(0, 1, 0, "push 1");    
           }
       });
	}
}
