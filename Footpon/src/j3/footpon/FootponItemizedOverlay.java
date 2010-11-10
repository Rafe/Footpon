package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponRepository;
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

public class FootponItemizedOverlay extends ItemizedOverlay 
{
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	
	public FootponItemizedOverlay(Drawable defaultMarker) 
	{
		  super(boundCenterBottom(defaultMarker));
	}

	Context mContext;
	
	public FootponItemizedOverlay(Drawable defaultMarker, Context context) 
	{
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}

	@Override
	protected boolean onTap(final int index) 
	{
		OverlayItem item = mOverlays.get(index);
	  
		//Toast t = Toast.makeText(mContext, item.getTitle() + " \n" + item.getSnippet() , Toast.LENGTH_LONG);
		//t.show();
//<<<<<<< HEAD
		Dialog dialog = new Dialog(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setContentView(R.layout.footpon_dialog);
//		//Footpon fp = FootponRepository.getFootponsInArea(1, 1, 1, 1).get(index);
//		//This should not be hard coded.
		Footpon fp = FootponRepository.getFootponsInAreaServer(40.75916,-73.984491).get(index);
//=======
		
//		Dialog dialog = new Dialog(mContext);
//		dialog.setTitle(item.getTitle());
//		dialog.setContentView(R.layout.footpon_dialog);
		
//		IFootponService service = FootponServiceFactory.getService();
//		Footpon fp = service.getFootponsInArea(40.75916,-73.984491).get(index);
//>>>>>>> 11fbcc2f52b87cdb1ec0939137cd67e18044d97a
	  
		TextView storeName = (TextView) dialog.findViewById(R.id.dialog_store_name);
		TextView hiddenDescription = (TextView) dialog.findViewById(R.id.dialog_hiddenDescription);
		//TextView realDescription = (TextView) dialog.findViewById(R.id.dialog_realDescription);
		TextView pointsRequired = (TextView) dialog.findViewById(R.id.dialog_pointsRequired);
		Button detailsButton = (Button) dialog.findViewById(R.id.dialog_show_details);
      
		detailsButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//<<<<<<< HEAD
				//This should not be hard coded.
//=======
//>>>>>>> 11fbcc2f52b87cdb1ec0939137cd67e18044d97a
				Intent i = new Intent(mContext,FootponDetailsActivity.class);
				i.putExtra("index", index);
				mContext.startActivity(i);
			}
		}
		);
		
      storeName.setText(fp.getStoreName());
      hiddenDescription.setText(fp.getHiddenDescription());
      //realDescription.setText(fp.getRealDescription());
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
