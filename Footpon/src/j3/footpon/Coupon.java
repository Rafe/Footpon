package j3.footpon;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Coupon extends Activity {
    private final String SHARE_LOGIN_TAG = "LOGIN_TAG";
    private String SHARE_FIRSTNAME = "FOOTPON_FIRSTNAME";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_inf);
        
        final Button button = (Button) findViewById(R.id.Use);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            }
        });
        
        SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
        String firstName = share.getString(SHARE_FIRSTNAME, "");
        TextView welcomeView = (TextView) findViewById(R.id.welcome);
        welcomeView.setText("Welcome, "+firstName);
    }
}
