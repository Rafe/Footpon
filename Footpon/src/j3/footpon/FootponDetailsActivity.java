package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FootponDetailsActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.footpon_details);
        
        ArrayList<Footpon> footpons =FootponServiceFactory.getService().getInstance(); 
        
        TextView storeName = (TextView) findViewById(R.id.detail_store_name);
        TextView description = (TextView) findViewById(R.id.detail_real_description);
        TextView pointsRequired = (TextView) findViewById(R.id.detail_required_points);
        Button button = (Button) findViewById(R.id.Use);
        
        Intent i = getIntent();
        if(i != null){
        	int position = i.getExtras().getInt("index");
        	Footpon fp = footpons.get(position);
        	storeName.setText(fp.getStoreName());
            description.setText(fp.getRealDescription());
            pointsRequired.setText(fp.getPointsRequired()+" Points");
            
            if(i.getExtras().getBoolean("isRedeemed") == false){
            	button.setText("Redeem Now!");
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
            	    	startActivity(
            	    			new Intent(FootponDetailsActivity.this, Login.class));
                    }
                });
            }else{
            	
            }
            
        }
	}
	
}
