package com.lokalizator;

import android.location.Location;

public interface Akcje_na_lokacizacji {


     boolean warunek(Location location);
     void Akcja(Location location);
     void Akcje_is_false(Location location);


}
