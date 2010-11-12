package j3.footpon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity {

	private EditText view_userName;
	private EditText view_password;
	private EditText view_passwordConfirm;
	private Button view_submit;
	private Button view_clearAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		findViews();
		setListener();
	}

	private void findViews() {
		view_userName = (EditText) findViewById(R.id.registerUserName);
		view_password = (EditText) findViewById(R.id.registerPassword);
		view_passwordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
		view_submit = (Button) findViewById(R.id.registerSubmit);
		view_clearAll = (Button) findViewById(R.id.registerClear);
	}

	private void setListener() {
		view_submit.setOnClickListener(submitListener);
		view_clearAll.setOnClickListener(clearListener);
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String userName = view_userName.getText().toString();
			String password = view_password.getText().toString();
			String passwordConfirm = view_passwordConfirm.getText().toString();
			validateForm(userName, password, passwordConfirm);
		}
	};

	private OnClickListener clearListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearForm();
		}
	};

	private void validateForm(String userName, String password, String password2) {
		StringBuilder suggest = new StringBuilder();
		Log.d(this.toString(), "validate");
		if (userName.length() < 1) {
			suggest.append(getText(R.string.suggust_userName) + "\n");
		}
		if (password.length() < 1 || password2.length() < 1) {
			suggest.append(getText(R.string.suggust_passwordNotEmpty) + "\n");
		}
		if (!password.equals(password2)) {
			suggest.append(getText(R.string.suggest_passwordNotSame));
		}
		if (suggest.length() > 0) {
			Toast.makeText(this, suggest.subSequence(0, suggest.length() - 1),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void clearForm() {
		view_userName.setText("");
		view_password.setText("");
		view_passwordConfirm.setText("");

		view_userName.requestFocus();
	}
}
