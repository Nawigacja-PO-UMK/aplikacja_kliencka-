package com.search_location;

import java.io.Serializable;
import java.util.Locale;

public class Address extends android.location.Address implements Serializable {
    /**
     * Constructs a new Address object set to the given Locale and with all
     * other fields initialized to null or false.
     *
     * @param locale
     */
    String facebook;
    String open_closs;
    public double Altitude;
    public Address(Locale locale) {
        super(locale);
        facebook=null;
        open_closs=null;
    }

    public double getAltitude()
    {
        return this.Altitude;
    }

    public void  setAltitude(double Altitude)
    {
        this.Altitude=Altitude;
    }
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    public void setOpen_closs(String open_closs)
    {
        this.open_closs=open_closs;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getOpen_closs() {
        return open_closs;
    }
}
