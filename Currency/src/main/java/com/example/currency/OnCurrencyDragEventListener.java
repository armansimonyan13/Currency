package com.example.currency;


import android.content.ClipData;
import android.content.ClipDescription;
import android.view.DragEvent;
import android.view.View;

public class OnCurrencyDragEventListener implements View.OnDragListener {

	@Override
	public boolean onDrag(View view, DragEvent dragEvent) {
		final int action = dragEvent.getAction();

		switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
					return true;
				} else {
					return false;
				}
			case DragEvent.ACTION_DRAG_ENTERED:
				view.setAlpha(0.5f);
				String receivingViewName = (String) view.getTag();
				String draggedViewName = dragEvent.getClipDescription().getLabel().toString();
				replace(receivingViewName, draggedViewName);
				return true;
			case DragEvent.ACTION_DRAG_LOCATION:
				return true;
			case DragEvent.ACTION_DRAG_EXITED:
				view.setAlpha(1.0f);
				return true;
			case DragEvent.ACTION_DROP:
				return true;
			case DragEvent.ACTION_DRAG_ENDED:
				return true;
			default:
				return false;
		}
	}

	public void replace(String name1, String name2) {}
}
