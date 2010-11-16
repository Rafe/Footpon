package j3.footpon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Coupon extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_inf);
        
        final Button button = (Button) findViewById(R.id.Use);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });
    }
}
