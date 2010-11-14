package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class FootponItemizedOverlay extends ItemizedOverlay<OverlayItem> {
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
		OverlayItem item = mOverlays.get(index);

		// Toast t = Toast.makeText(mContext, item.getTitle() + " \n" +
		// item.getSnippet() , Toast.LENGTH_LONG);
		// t.show();
		Dialog dialog = new Dialog(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setContentView(R.layout.footpon_dialog);

		IFootponService service = FootponServiceFactory.getService();
		Footpon fp = service.getInstance().get(index);

		TextView description = (TextView) dialog
				.findViewById(R.id.dialog_realDescription);
		TextView pointsRequired = (TextView) dialog
				.findViewById(R.id.dialog_pointsRequired);
		Button detailsButton = (Button) dialog
				.findViewById(R.id.dialog_show_details);

		detailsButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, FootponDetailsActivity.class);
				i.putExtra("index", index);
				i.putExtra("isRedeemed", true);
				mContext.startActivity(i);
			}
		});

		description.setText(fp.getRealDescription());
		pointsRequired.setText("Points:" + fp.getPointsRequired());

		dialog.show();

		return true;
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
