package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class FootponDetailsActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.footpon_details);
        
        ArrayList<Footpon> footpons =FootponServiceFactory.getService().getInstance(); 
        
        TextView storeName = (TextView) findViewById(R.id.details_store_name);
        TextView description = (TextView) findViewById(R.id.details_realDescription);
        TextView pointsRequired = (TextView) findViewById(R.id.details_pointsRequired);
        
        Intent i = getIntent();
        if(i != null){
        	int position = i.getExtras().getInt("index");
        	Footpon fp = footpons.get(position);
        	storeName.setText(fp.getStoreName());
            description.setText(fp.getRealDescription());
            pointsRequired.setText("Points:" + fp.getPointsRequired());
        }
	}
	
}
