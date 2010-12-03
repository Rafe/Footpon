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
                .setIndicator("Map", getResources().getDrawable(R.drawable.map))
                .setContent(new Intent(this, FootponMapActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("List")
                .setIndicator("List", getResources().getDrawable(R.drawable.list))
                .setContent(new Intent(this, FootponListActivity.class)));

        tabHost.addTab(tabHost.newTabSpec("myFootpon")
                .setIndicator("My Footpon", getResources().getDrawable(R.drawable.foot))
                .setContent(new Intent(this, MyFootponActivity.class)));
        
        tabHost.addTab(tabHost.newTabSpec("account")
                .setIndicator("Account", getResources().getDrawable(R.drawable.account))
                .setContent(new Intent(this, ShowInformation.class)));
    }
}
