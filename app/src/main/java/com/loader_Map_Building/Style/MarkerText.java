package com.loader_Map_Building.Style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

public class MarkerText extends Overlay {

    private final String text;
    private final IGeoPoint location;
    private final Paint paint;
    private final Rect bounds;
    private final float iconHeight;


    public MarkerText(String text, IGeoPoint location, float iconHeight) {
        this.text = text;
        this.location = location;
        this.paint = new Paint();
        this.bounds = new Rect();
        this.iconHeight = iconHeight;

        //paint.setColor(0xFF000000); // Color czarny
        paint.setTextSize(39);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(mapView.getZoomLevelDouble() > 21.5){
            Point screenCoords = new Point();
            mapView.getProjection().toPixels(location,screenCoords);

            paint.getTextBounds(text, 0, text.length(), bounds);

            canvas.drawText(text, screenCoords.x - (float)bounds.width() / 2, (float)(screenCoords.y + bounds.height() / 2  + iconHeight), paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event, MapView mapView) {
        // Do nothing on touch events
        return false;
    }
}