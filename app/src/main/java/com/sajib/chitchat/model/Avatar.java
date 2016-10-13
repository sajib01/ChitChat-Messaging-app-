package com.sajib.chitchat.model;

/**
 * Created by sajib on 21/9/16.
 */

public class Avatar {
    int code;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    boolean selected;
}
