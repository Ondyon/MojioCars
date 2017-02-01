package com.example.mojiocars;


public class DataReadyListener<T> {

	private DataListener<T> dataCallback = null;

	void notify(T trips) {
		if(dataCallback != null) dataCallback.onComes(trips);
	}

	public void subscribe(DataListener<T> cb) {
		dataCallback = cb;
	}


}
