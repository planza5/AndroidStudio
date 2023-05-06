package com.plm.encaja;

public class NoSpaceException extends Throwable {
    private final ItemRectangle rectangle;

    public NoSpaceException(ItemRectangle rectangle){
        super("No space exception");
        this.rectangle=rectangle;
    }

    public ItemRectangle getRectangle() {
        return rectangle;
    }
}
