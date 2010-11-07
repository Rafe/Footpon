package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponRepository;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FootponDetailsActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_details);
        
        ArrayList<Footpon> footpons = new ArrayList<Footpon>();
        //footpons = FootponRepository.getFootponsInArea(1, 0, 0, 100);
        footpons = FootponRepository.getFootponsInAreaServer(40.75916,-73.984491);
        
        Intent i = getIntent();
        
        if(i != null){
        	int position = i.getExtras().getInt("index");
        	setDetails(footpons.get(position));
        }
	}
	
	public void setDetails(Footpon fp){
		
		TextView storeName = (TextView) findViewById(R.id.details_store_name);
        TextView hiddenDescription = (TextView) findViewById(R.id.details_hiddenDescription);
        TextView realDescription = (TextView) findViewById(R.id.details_realDescription);
        TextView pointsRequired = (TextView) findViewById(R.id.details_pointsRequired);
        
        storeName.setText(fp.getStoreName());
        hiddenDescription.setText(fp.getHiddenDescription());
        realDescription.setText(fp.getRealDescription());
        pointsRequired.setText("Points:" + fp.getPointsRequired());
        
	}
	
}
