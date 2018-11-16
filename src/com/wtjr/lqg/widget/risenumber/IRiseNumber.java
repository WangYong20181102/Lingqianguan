package com.wtjr.lqg.widget.risenumber;

public interface IRiseNumber {
	public void start();

	public RiseNumberTextView withNumber(String startNumber, String endNumber);

	public void setOnEndListener(RiseNumberTextView.EndListener callback);
}
