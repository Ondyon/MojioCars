package com.example.mojiocars;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.moj.java.sdk.model.Trip;


public class TripListFragment extends Fragment {

	private RecyclerView rvTripList;
	private TextView labelEmpty;
	private TripListAdapter tripListAdapter;
	private DataReadyListener<List<Trip>> dataObject;

	public DataListener<String> eventInterface = new DataListener<String>() {
		@Override
		public void onComes(String data) {
			((MainActivity)getActivity()).launchDetailsFragment(data);
		}
	};
	private DataListener<String> listener;


	public TripListFragment() {
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment HousingFragment.
	 */
	public static TripListFragment newInstance() {
		TripListFragment fragment = new TripListFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tracklist, container, false);

		labelEmpty = (TextView) view.findViewById(R.id.label_empty_list);

		rvTripList = (RecyclerView) view.findViewById(R.id.rv_trips_list);
		tripListAdapter = new TripListAdapter(listener);
		rvTripList.setAdapter(tripListAdapter);
		rvTripList.setLayoutManager(new LinearLayoutManager(getActivity()));

		return view;
	}


	DataListener<List<Trip>> dataCallback = new DataListener<List<Trip>>() {
		@Override
		public void onComes(List<Trip> data) {
			tripListAdapter.updateAdapter(data);
			labelEmpty.setVisibility(data==null || data.size()==0 ? View.VISIBLE : View.GONE);
		}
	};


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		listener = (DataListener<String>)context;
		InterfaceHolder dataObj = (InterfaceHolder) context;
		dataObject = dataObj.getTripsReadyListener();
		if(dataObject != null)
			dataObject.subscribe(dataCallback);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if(dataObject != null)
			dataObject.subscribe(null);
		listener = null;
	}


}
