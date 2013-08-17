package com.example.currency;


import android.content.ClipDescription;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class OnCurrencyDragEventListener implements View.OnDragListener {
	private static final String TAG = "OnCurrencyDragEventListener";
	private View exitedView;
	private float startY;
	private boolean entered = false;

	@Override
	public boolean onDrag(final View view, final DragEvent dragEvent) {
		final int action = dragEvent.getAction();


		switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
					startY = dragEvent.getY();
					return true;
				} else {
					return false;
				}
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d(TAG, "Entered");
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				Log.d(TAG, "Location");
				if (entered) {
					return true;
				}
				Log.d(TAG, "LocationOk");
				entered = true;
				final String draggedViewName = dragEvent.getClipDescription().getLabel().toString();
				final String receivingViewName = (String) view.getTag();
				if (draggedViewName.equals(receivingViewName)) {
					return true;
				}

				float height = view.getHeight();
				float currentY = dragEvent.getY();

				if ((startY - currentY) < 0) {
					height = -height;
				}

				TranslateAnimation animation = new TranslateAnimation(0, 0, 0, height);
				animation.setDuration(400);
				final View finalExitedView = exitedView;
				animation.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						replace(receivingViewName, draggedViewName);
						view.setAlpha(0.0f);
						if (finalExitedView != null) {
							finalExitedView.setAlpha(1.0f);
						}
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}
				});
				view.startAnimation(animation);
				return false;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d(TAG, "Exited");
				exitedView = view;
				entered = false;
				return true;
			case DragEvent.ACTION_DROP:
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				view.setAlpha(1.0f);
				entered = false;
				return true;
			default:
				return false;
		}
	}

	public void replace(String name1, String name2) {}
}
