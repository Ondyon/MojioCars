package com.example.mojiocars;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsFragment extends Fragment {

	private static final String PARAM_ID = "paramID";
	private String id;
	private App app;
	private MojioClient mojioClient;
	private TextView tvStart;
	private TextView tvEnd;
	private TextView tvDate;
	private TextView tvSpeed;
	private DataReadyListener<Trip> dataObject;


	public DetailsFragment() {
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment HousingFragment.
	 */
	public static DetailsFragment newInstance(String id) {
		DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_ID, id);
        fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = ((App) getActivity().getApplicationContext());
		mojioClient = app.getMojioClient();
        if (getArguments() != null) {
            id = getArguments().getString(PARAM_ID);
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container, false);

		tvStart = (TextView) view.findViewById(R.id.det_tracklist_start);
		tvEnd = (TextView) view.findViewById(R.id.det_tracklist_end);
		tvDate = (TextView) view.findViewById(R.id.det_tracklist_date);
		tvSpeed = (TextView) view.findViewById(R.id.det_tracklist_speed);

		loadDetails(id);

		return view;
	}


	private void loadDetails(String tag) {
		//progress.setProgress(true);
		mojioClient.rest().getTrip(tag).enqueue(new Callback<Trip>() {

			@Override
			public void onResponse(Call<Trip> call, Response<Trip> response) {
				//progress.setProgress(false);
				Trip trip = response.body();
				showData(trip);
			}

			@Override
			public void onFailure(Call<Trip> call, Throwable t) {
				//progress.setProgress(false);
				Toast.makeText(getActivity().getApplicationContext(), "Error fetching trip list", Toast.LENGTH_SHORT).show();
			}
		});
	}


	private void showData(Trip dataItem) {
		tvStart.setText(dataItem.getStartLocation().getAddress().getCity());
		tvEnd.setText(dataItem.getEndLocation().getAddress().getCity());

		String timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dataItem.getStartTimestamp()));
		tvDate.setText(timeStr);

		tvSpeed.setText(dataItem.getMaxSpeed().getBaseValue() + " " + getUnitText(dataItem.getMaxSpeed().getBaseSpeedUnit().toString()));
	}

	private String getUnitText(String s) {
		switch(s) {
			case "KILOMETERS_PER_HOUR": s = getResources().getString(R.string.unit_km_h);
				break;
			//TODO add all
		}
		return s;
	}


}
