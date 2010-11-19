package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class FootponItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private MapView map=null;

	public FootponItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	Context mContext;

	public FootponItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

/*	@Override
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
	}*/
	
	
	@Override
	protected boolean onTap(final int index) {
/*	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;*/
	  	  
		Footpon footpon = null;
		
		  OverlayItem item=getItem(index);
		  GeoPoint point=item.getPoint();
	  
	  
		//OverlayItem item = mOverlays.get(index);

		// Toast t = Toast.makeText(mContext, item.getTitle() + " \n" +
		// item.getSnippet() , Toast.LENGTH_LONG);
		// t.show();
		Dialog dialog = new Dialog(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setContentView(R.layout.footpon_dialog);

		
		String result = "";
		InputStream is = null;

		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double
				.toString((double)point.getLatitudeE6()/1000000)));
		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double
				.toString((double)point.getLongitudeE6()/1000000)));



		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"http://pdc-amd01.poly.edu/~jli15/footpon/getSingle.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			is = entity.getContent();
		}

		catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();

			result = sb.toString();
		}

		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);

			//for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(0);

				
				//int longitude=(int) (json_data.getDouble("longitude")*1000000);
				//int latitude=(int) (json_data.getDouble("latitude")*1000000);
				//String storeName=json_data.getString("storeName");
				//String hiddenDescription=json_data.getString("hiddenDescription");
				//String realDescription=json_data.getString("realDescription");
				//int pointsRequired =(int) json_data.getInt("pointsRequired");
//				double latitude, double longtitude,int pointsRequired,long code){
				footpon=new Footpon(json_data.getString("storeName"), json_data.getString("category"), json_data.getString("hiddenDescription"), json_data.getString("realDescription"), (json_data.getDouble("latitude")*1000000), (json_data.getDouble("longitude")*1000000), (int) json_data.getInt("pointsRequired"), (long) json_data.getInt("code"));

			//}
		}

		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		
		
		//IFootponService service = FootponServiceFactory.getService();
		//Footpon fp = service.getInstance().get(index);

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

		description.setText(footpon.getHiddenDescription());
		pointsRequired.setText("Points:" + footpon.getPointsRequired());

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
