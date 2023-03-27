package com.loader_Map_Building.Style;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ScaledMarker extends Marker {

    private double mScale;

    public ScaledMarker(MapView mapView, IGeoPoint geoPoint, Drawable drawable) {
        super(mapView);
        setPosition((GeoPoint) geoPoint);
        setIcon(drawable);
        mScale = 1.0f;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(mapView.getZoomLevelDouble() > 20.4){

            // Obliczamy szerokość i wysokość markera w pikselach
            Drawable icon = getIcon();
            int iconWidth = icon.getIntrinsicWidth();
            int iconHeight = icon.getIntrinsicHeight();

            //rozmiary zostaly ustalone stale (jak na razie)
            double scaledWidth = 95;
            double scaledHeight = 95;

            double widthRatio = (double) scaledWidth / (double) iconWidth;
            double heightRatio = (double) scaledHeight / (double) iconHeight;
            mScale = Math.min(widthRatio, heightRatio);

            // Rysujemy marker w odpowiedniej skali
            Point screenPosition = mapView.getProjection().toPixels(getPosition(), null);
            int scaledIconWidth = (int) (iconWidth * mScale);
            int scaledIconHeight = (int) (iconHeight * mScale);
            Rect scaledIconBounds = new Rect(
                    screenPosition.x - scaledIconWidth / 2,
                    screenPosition.y - scaledIconHeight / 2,
                    screenPosition.x + scaledIconWidth / 2,
                    screenPosition.y + scaledIconHeight / 2
            );
            icon.setBounds(scaledIconBounds);

            icon.draw(canvas);
        }
    }

}
