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
import io.moj.java.sdk.model.response.ListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*
here is 2 different approaches demonstrated on 2 fragments

1 TripListFragment:
 keep data and all data processing on base activity,
 this allow to keep data persistence and fast UI (on fragments) independent from data loading (in base Activity)
 MainActivity creates tripsReadyListener
 InterfaceHolder provides the access to it for fragment
 TripListFragment get it onAttach() and subscribes to dataLoaded event using it

2 DetailsFragment:
 All OneTrip related data methods (loading and processing) are concentrated in Fragment

1st approach shows better UX
2nd approach provides better architecture, code refactoring etc.

 */


public class MainActivity extends AppCompatActivity implements DataListener<String>, InterfaceHolder {

    App app;
	private MojioClient mojioClient;
	private List<Trip> trips;
	private DataReadyListener tripsReadyListener = new DataReadyListener<List<Trip>>();
	private FragmentManager fm;
	private ProgressSpinner progress;



	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ((App) getApplicationContext());
        mojioClient = app.getMojioClient();

		progress = (ProgressSpinner) findViewById(R.id.progress_spinner);

		Fragment newFragment = TripListFragment.newInstance();

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


		loadTrips(tripsReadyListener);
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
				readyListener.notify(trips);
			}

			@Override
			public void onFailure(Call<ListResponse<Trip>> call, Throwable t) {
				progress.setProgress(false);
				Toast.makeText(getApplicationContext(), "Error fetching trip list", Toast.LENGTH_SHORT).show();
			}
		});
	}


	public void launchDetailsFragment(String tag) {
		Fragment newFragment = DetailsFragment.newInstance(tag);
		fm.beginTransaction()
				.add(R.id.main_fragment_container, newFragment)
				.addToBackStack(null)
				.commit();
		}

	@Override
	public void onComes(String data) {
		launchDetailsFragment(data);
	}

	@Override
	public DataReadyListener<List<Trip>> getTripsReadyListener() {
		return tripsReadyListener;
	}

}
