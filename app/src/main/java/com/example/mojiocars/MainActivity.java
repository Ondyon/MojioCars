package com.example.mojiocars;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import io.moj.java.sdk.model.User;
import io.moj.java.sdk.model.response.ListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    App app;
	private MojioClient mojioClient;
	private List<Trip> trips;
	private DataReadyListener tripsReadyListener;
	private DataReadyListener detailsReadyListener;
	private FragmentManager fm;
	private ProgressSpinner progress;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        app = ((App) getApplicationContext());
        mojioClient = app.getMojioClient();

		progress = (ProgressSpinner) findViewById(R.id.progress_spinner);


		tripsReadyListener = new DataReadyListener<List<Trip>>();	// Dependency Injection
		Fragment newFragment = TripListFragment.newInstance(tripsReadyListener);

		fm = getSupportFragmentManager();
		fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				getSupportActionBar().setDisplayHomeAsUpEnabled(fm.getBackStackEntryCount() > 0);
			}
		});

		fm.beginTransaction()
				.replace(R.id.main_fragment_container, newFragment)
				.commit();


		mojioClient.login("ondyon@yandex.ru", "MojioMojio").enqueue(new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				//Toast.makeText(getApplicationContext(), "logged as " + response.body().getUserName(), Toast.LENGTH_SHORT).show();
				loadTrips(tripsReadyListener);
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Toast.makeText(getApplicationContext(), "Error Login", Toast.LENGTH_SHORT).show();
			}
		});


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:		// actionBar "BACK" button
				onBackPressed();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}


	private void loadTrips(final DataReadyListener readyListener) {
		progress.setProgress(true);
		mojioClient.rest().getTrips().enqueue(new Callback<ListResponse<Trip>>() {
			@Override
			public void onResponse(Call<ListResponse<Trip>> call, Response<ListResponse<Trip>> response) {
				progress.setProgress(false);
				trips = response.body().getData();
				readyListener.ready(trips);
			}

			@Override
			public void onFailure(Call<ListResponse<Trip>> call, Throwable t) {
				progress.setProgress(false);
				Toast.makeText(getApplicationContext(), "Error fetching trip list", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void loadDetails(String tag, final DataReadyListener<Trip> readyListener) {
		progress.setProgress(true);
		mojioClient.rest().getTrip(tag).enqueue(new Callback<Trip>() {

			@Override
			public void onResponse(Call<Trip> call, Response<Trip> response) {
				progress.setProgress(false);
				Trip trip = response.body();
				readyListener.ready(trip);
			}

			@Override
			public void onFailure(Call<Trip> call, Throwable t) {
				progress.setProgress(false);
				Toast.makeText(getApplicationContext(), "Error fetching trip list", Toast.LENGTH_SHORT).show();
			}
		});
	}


	public void launchDetailsFragment(String tag) {
		detailsReadyListener = new DataReadyListener<Trip>();	// Dependency Injection
		Fragment newFragment = DetailsFragment.newInstance(detailsReadyListener);
		fm.beginTransaction()
				.add(R.id.main_fragment_container, newFragment)
				.addToBackStack(null)
				.commit();

		loadDetails(tag, detailsReadyListener);
	}

}
