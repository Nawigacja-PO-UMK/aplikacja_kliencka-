package com;

import com.example.nawigacja_po_umk.Mapa;

import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.modules.TileDownloader;
import org.osmdroid.views.MapView;

public class save_map_ofline {

    CacheManager cacheManager;

   public save_map_ofline(MapView mapView)
    {
        cacheManager=new CacheManager(mapView);
    }

    public void save_file()
    {
        TileDownloader tileDownloader=new TileDownloader();
        ///
    }
}
