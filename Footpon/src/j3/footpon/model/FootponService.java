package j3.footpon.model;

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

import android.util.Log;

public class FootponService implements IFootponService {

	private static ArrayList<Footpon> _instance;

	@Override
	public ArrayList<Footpon> getFootponsInArea(double currentLatitude,
			double currentLongitude) {
		String result = "";
		InputStream is = null;

		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double
				.toString(currentLatitude)));
		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double
				.toString(currentLongitude)));

		ArrayList<Footpon> footpons = new ArrayList<Footpon>();

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php");
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

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				footpons.add(new Footpon(json_data));

			}
		}

		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return footpons;
	}

	@Override
	public ArrayList<Footpon> getInstance() {
		if (_instance != null) {
			return _instance;
		} else {
			return null;
		}
	}

}
