package com.plm.createwalls;

import com.plm.createwalls.model.Wall;

public interface UserActions {
    public void addWall(Wall from, Float width, Float height, Float thickness);
    public void modifiyWall(Wall wall, Float width, Float height, Float thickness);
    public void addWindow(Wall wall, Float witdh, Float height, Float dx, Float dy);
    public void modifyWindow(Wall wall, Float witdh, Float height, Float dx, Float dy);
}
