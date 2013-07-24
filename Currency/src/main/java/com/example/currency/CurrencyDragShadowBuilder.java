package com.example.currency;

import android.graphics.Point;
import android.util.Log;
import android.view.View;

public class CurrencyDragShadowBuilder extends View.DragShadowBuilder {
	private static final String TAG = CurrencyDragShadowBuilder.class.getName();

	public CurrencyDragShadowBuilder(View view) {
		super(view);
	}
	@Override
	public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
		Log.d(TAG, "X: " + shadowTouchPoint.x + ", Y: " + shadowTouchPoint.y);
		super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
		Log.d(TAG, "X: " + shadowTouchPoint.x + ", Y: " + shadowTouchPoint.y);
	}
}
