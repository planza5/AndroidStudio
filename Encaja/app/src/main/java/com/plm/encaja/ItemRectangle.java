package com.plm.encaja;

import android.content.ClipData;
import android.graphics.Point;

public class ItemRectangle {
    private int units;
    private Dimension dimension;
    private Location location;

    public ItemRectangle(int units, Location location, Dimension dimension) {
        this.units = units;
        this.dimension = dimension;
        this.location=location;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ItemRectangle copy() {
        Location l=new Location(this.location.getX(),this.location.getY());
        Dimension d=new Dimension(this.dimension.getWidth(),this.dimension.getHeight());
        return new ItemRectangle(units,l,d);
    }
}
