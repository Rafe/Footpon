package j3.footpon;

import j3.footpon.model.Footpon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
				.toString((double) point.getLatitudeE6() / 1000000)));
		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double
				.toString((double) point.getLongitudeE6() / 1000000)));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/getSingle.php");
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

			JSONObject json_data = jArray.getJSONObject(0);

			footpon = new Footpon(json_data);
		}

		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		// IFootponService service = FootponServiceFactory.getService();
		// Footpon fp = service.getInstance().get(index);

		TextView description = (TextView) dialog
				.findViewById(R.id.dialog_realDescription);
		TextView stepsRequired = (TextView) dialog
				.findViewById(R.id.dialog_stepsRequired);
		Button detailsButton = (Button) dialog
				.findViewById(R.id.dialog_show_details);

		detailsButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				File sdcard = Environment.getExternalStorageDirectory();
				File file = new File(sdcard, "user.txt");

				if (file.exists()) {
					String text = new String();

					try {
						BufferedReader reader = new BufferedReader(
								new FileReader(file));
						String line;

						line = reader.readLine();
						String[] temp = line.split(" ");
						text = temp[1];

						String result = "";
						InputStream is = null;

						//Data to send.
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						nameValuePairs.add(new BasicNameValuePair("username",
								text));
						nameValuePairs.add(new BasicNameValuePair("id", Long
								.toString(footpon.getID())));

						// http post
						try {
							HttpClient httpclient = new DefaultHttpClient();

							HttpPost httppost = new HttpPost(
									"http://pdc-amd01.poly.edu/~jli15/footpon/redeemCoupon.php");
							httppost.setEntity(new UrlEncodedFormEntity(
									nameValuePairs));

							HttpResponse response = httpclient
									.execute(httppost);

							HttpEntity entity = response.getEntity();

							is = entity.getContent();
						}

						catch (Exception e) {
							Log.e("log_tag",
									"Error in http connection " + e.toString());
						}

						// convert response to string
						try {
							reader = new BufferedReader(new InputStreamReader(
									is, "iso-8859-1"), 8);

							StringBuilder sb = new StringBuilder();

							line = null;

							while ((line = reader.readLine()) != null) {
								sb.append(line + "\n");
							}

							is.close();

							result = sb.toString();
						}

						catch (Exception e) {
							Log.e("log_tag",
									"Error converting result " + e.toString());
						}

						// parse json data
						try {
							JSONArray jArray = new JSONArray(result);
							JSONObject json_data = jArray.getJSONObject(0);

							String success = json_data.getString("success");

							if (sdcard.canWrite()) {
								FileWriter writer = new FileWriter(file, true);
								BufferedWriter out = new BufferedWriter(writer);

								out.append(Long.toString(footpon.getID()));
								out.append("\n\n");
								out.close();
							}

							if (success.equalsIgnoreCase("true")) {
								Intent i = new Intent(mContext,
										FootponDetailsActivity.class);
								i.putExtra("latitude", footpon.getLatitude());
								i.putExtra("longitude", footpon.getLongitude());
								i.putExtra("isRedeemed", true);
								mContext.startActivity(i);
							}

							else {
							}
						}

						catch (JSONException e) {
							Log.e("log_tag",
									"Error parsing data " + e.toString());
						}
					}

					catch (FileNotFoundException e) {
						text = "File not found.\n";
					}

					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else {
					Intent i = new Intent(mContext, ShowInformation.class);
					mContext.startActivity(i);
				}
			}
		});

		description.setText(footpon.getHiddenDescription());
		stepsRequired.setText("Steps:" + footpon.getStepsRequired());

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
