package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class FootponItemizedOverlay extends ItemizedOverlay {
	Footpon footpon = null;
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

		IFootponService service = FootponServiceFactory.getService();
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

			File sdcard = Environment.getExternalStorageDirectory();
			File file = new File(sdcard, "user.txt");
			boolean isSuccess = false;
			
			if (!file.exists()) {
				Intent i = new Intent(mContext, ShowInformation.class);
				mContext.startActivity(i);
			}

			String userName = getUserName(file);
			if(userName != null){
				IFootponService service = FootponServiceFactory.getService();
				isSuccess = service.redeemFootpon(userName,
					(long) footpon.getID());
			}
			
			if (isSuccess){
				save(sdcard, file);
				startDetailActivity(footpon,true);
			}else{
				startDetailActivity(footpon,false);
			}
		}
		
		private void startDetailActivity(Footpon fp,boolean isRedeemed){
			Intent i = new Intent(mContext,
					FootponDetailsActivity.class);
			i.putExtra("latitude", fp.getLatitude());
			i.putExtra("longitude", fp.getLongitude());
			i.putExtra("id", fp.getID());
			i.putExtra("isRedeemed", isRedeemed);
			mContext.startActivity(i);
		}
		
		private void save(File sdcard, File file) {
			try {
				if (sdcard.canWrite()) {
					FileWriter writer = new FileWriter(file, true);
					BufferedWriter out = new BufferedWriter(writer);

					out.append(Long.toString(footpon.getID()));
					out.append("\n\n");
					out.close();
				}

			} catch (FileNotFoundException e) {
				Log.e("log_tag", "File not found.\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	private String getUserName(File file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;

			line = reader.readLine();
			String[] temp = line.split(" ");
			return temp[1];
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		return null;
	}

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
