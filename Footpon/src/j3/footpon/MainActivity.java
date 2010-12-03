package j3.footpon;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_tab);
        
        final TabHost tabHost = getTabHost();
        
        tabHost.addTab(tabHost.newTabSpec("map")
                .setIndicator("Map")
                .setContent(new Intent(this, FootponMapActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("List")
                .setIndicator("List")
                .setContent(new Intent(this, FootponListActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("myFootpon")
                .setIndicator("My Footpon")
                .setContent(new Intent(this, MyFootponActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("account")
                .setIndicator("Account")
                .setContent(new Intent(this, ShowInformation.class)));
    }
}
