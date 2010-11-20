package j3.footpon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CouponRedeemActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_inf);
        
        final Button button = (Button) findViewById(R.id.Use);
        button.setText("Redeem Now!");
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
    	    	startActivity(
    	    		new Intent(CouponRedeemActivity.this, Login.class));
            }
        });
    }
}
