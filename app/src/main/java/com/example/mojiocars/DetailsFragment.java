package com.example.mojiocars;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.moj.java.sdk.MojioClient;
import io.moj.java.sdk.model.Trip;


public class DetailsFragment extends Fragment {

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
	 * @param l
	 */
	public static DetailsFragment newInstance(DataReadyListener<Trip> l) {
		DetailsFragment fragment = new DetailsFragment();
		fragment.dataObject = l;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = ((App) getActivity().getApplicationContext());
		mojioClient = app.getMojioClient();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_details, container, false);

		tvStart = (TextView) view.findViewById(R.id.det_tracklist_start);
		tvEnd = (TextView) view.findViewById(R.id.det_tracklist_end);
		tvDate = (TextView) view.findViewById(R.id.det_tracklist_date);
		tvSpeed = (TextView) view.findViewById(R.id.det_tracklist_speed);

		return view;
	}

	private void showData(Trip dataItem) {
		tvStart.setText(dataItem.getStartLocation().getAddress().getCity());
		tvEnd.setText(dataItem.getEndLocation().getAddress().getCity());

		String timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dataItem.getStartTimestamp()));
		tvDate.setText(timeStr);

		tvSpeed.setText(dataItem.getMaxSpeed().getBaseValue() + " " + getUnitText(dataItem.getMaxSpeed().getBaseSpeedUnit().toString()));
	}

	private String getUnitText(String s) {
		String out = s;
		switch(s) {
			case "KILOMETERS_PER_HOUR": s = getResources().getString(R.string.unit_km_h);
				break;
			//TODO add all
		}
		return s;
	}


	DataListener<Trip> dataCallback = new DataListener<Trip>() {
		@Override
		public void onComes(Trip data) {
			showData(data);
		}
	};


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if(dataObject != null)
			dataObject.subscribe(dataCallback);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if(dataObject != null)
			dataObject.subscribe(null);
	}
}
