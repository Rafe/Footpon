package j3.footpon;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class FootponItemizedOverlay extends ItemizedOverlay {

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
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  
	  Toast t = Toast.makeText(mContext, item.getTitle() + " \n" + item.getSnippet() , Toast.LENGTH_LONG);
	  t.show();
	  //AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  //dialog.setTitle(item.getTitle());
	  //dialog.setMessage(item.getSnippet());
	  //dialog.show();
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
