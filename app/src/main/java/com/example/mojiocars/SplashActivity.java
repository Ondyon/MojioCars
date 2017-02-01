package com.example.mojiocars;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends Activity {

	private App app;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = ((App) getApplicationContext());
		MojioClient mojioClient = app.getMojioClient();

		// login credentials are hardcoded
		mojioClient.login("ondyon@yandex.ru", "MojioMojio").enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_SHORT).show();
			}
		});

    }
	
}