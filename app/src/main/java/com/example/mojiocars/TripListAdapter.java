package com.example.mojiocars;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.moj.java.sdk.model.Trip;


public class TripListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final DataListener<String> listener;
	private List<Trip> tracks;

	public TripListAdapter(DataListener<String> c) {
		super();
		listener = c;
	}


	public void updateAdapter(List<Trip> trackList){
		this.tracks = trackList;
		this.notifyDataSetChanged();
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.item_tracklist, parent, false);
		RecycleItem recycleItem = new RecycleItem(v);
		return recycleItem;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

		Trip dataItem = getItem(position);

		((RecycleItem) holder).start.setText(dataItem.getStartLocation().getAddress().getCity());
		((RecycleItem) holder).end.setText(dataItem.getEndLocation().getAddress().getCity());

		String timeStr = new SimpleDateFormat("dd.MM.yyyy").format(new Date(dataItem.getStartTimestamp()));
		((RecycleItem) holder).date.setText(timeStr);

		((RecycleItem) holder).mRootView.setTag(dataItem.getId());
		((RecycleItem) holder).mRootView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onComes((String)v.getTag());
			}
		});

	}



	@Override
	public int getItemCount() {
		if(tracks == null)
			return 0;
		return tracks.size();
	}


	private Trip getItem(int position) {
		return tracks.get(position);
	}



	class RecycleItem extends RecyclerView.ViewHolder {
		final View mRootView;
		final TextView start;
		final TextView end;
		final TextView date;

		public RecycleItem(View view) {
			super(view);
			this.mRootView = view;
			this.start = (TextView) view.findViewById(R.id.item_tracklist_start);
			this.end = (TextView) view.findViewById(R.id.item_tracklist_end);
			this.date = (TextView) view.findViewById(R.id.item_tracklist_date);
		}
	}


}
