package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.User;
import j3.footpon.pedometer.StepService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class FootponItemizedOverlay extends ItemizedOverlay<OverlayItem> {
	
	Footpon footpon = null;
	IFootponService service;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public FootponItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	Context mContext;

	public FootponItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected boolean onTap(final int index) {
		
		OverlayItem item = getItem(index);
		GeoPoint point = item.getPoint();

		Dialog dialog = new Dialog(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setContentView(R.layout.footpon_dialog);
		if(service == null){
			service = FootponServiceFactory.getService();
		}
		footpon = service.getFootponByLocation((double) point.getLongitudeE6(),
				(double) point.getLatitudeE6());
		
		TextView description = (TextView) dialog
				.findViewById(R.id.dialog_realDescription);
		TextView stepsRequired = (TextView) dialog
				.findViewById(R.id.dialog_stepsRequired);
		Button detailsButton = (Button) dialog
				.findViewById(R.id.dialog_show_details);
		
		// set redeem button on dialog
		detailsButton.setOnClickListener(redeemListener);

		description.setText(footpon.getHiddenDescription());
		stepsRequired.setText("Steps:" + footpon.getStepsRequired());

		dialog.show();

		return true;
	}
	
	private Button.OnClickListener redeemListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			SharedPreferences share = mContext.getSharedPreferences(User.SHARE_USER_INF_TAG, 0);
			String username = share.getString(User.SHARE_USERNAME, "");
			long steps = 0;
			
			if(username!=null) {
				File sdcard=Environment.getExternalStorageDirectory();
				File file=new File(sdcard, "steps.txt");
				
				// get steps
				try {
					BufferedReader reader = new BufferedReader(new FileReader(file));
					try {
						String line = reader.readLine();
						steps = Long.parseLong(line);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				Intent i = new Intent(mContext, FootponDetailsActivity.class);
				
				steps += StepService.steps;
				Log.e("log_tag", "result: "+steps);
				if(steps > footpon.getStepsRequired()) {
					service = FootponServiceFactory.getService();
					service.redeemFootpon(username, footpon.getID());
					steps -= footpon.getStepsRequired();
					i.putExtra("own", true);
				}
				else
					i.putExtra("own", false);

				i.putExtra("steps", steps);
				i.putExtra("id", footpon.getID());
				mContext.startActivity(i);
			}
			else {
				Intent i = new Intent(mContext, Login.class);
				mContext.startActivity(i);
			}
		}
	};
	
	
	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
}
