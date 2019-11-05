package pers.mrwangx.commons.tool.display.entity;

import pers.mrwangx.commons.tool.display.annotation.Display;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 14:23
 * \* Description:
 **/
public class DisplayInfo implements Comparable<DisplayInfo> {

    private Object typeObject;
    private Display displayAnnotation;

    public DisplayInfo() {
    }

    public DisplayInfo(Object object, Display display) {
        this.typeObject = object;
        this.displayAnnotation = display;
    }

    public Object getTypeObject() {
        return typeObject;
    }

    public void setTypeObject(Object typeObject) {
        this.typeObject = typeObject;
    }

    public Display getDisplayAnnotation() {
        return displayAnnotation;
    }

    public void setDisplayAnnotation(Display displayAnnotation) {
        this.displayAnnotation = displayAnnotation;
    }

    @Override
    public int compareTo(DisplayInfo o) {
        Display displayAnnotaion = o.getDisplayAnnotation();
        if (displayAnnotaion.order() == this.displayAnnotation.order()) {
            return 0;
        } else if (this.displayAnnotation.order() < displayAnnotaion.order()) {
            return -1;
        } else {
            return 1;
        }
    }
}
