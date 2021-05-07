package com.ls.custom_view_library.verticaltextview;

public class LineText implements Cloneable{
    private StringBuilder text;
    private WidthAndHeight widthAndHeight;

    public String getText() {
        return text == null ? "" : text.toString();
    }

    public synchronized void addChar(char c){
        if(text == null){
            text = new StringBuilder();
        }
        text.append(c);
    }

    public synchronized void removeLast(){
        if(text == null || text.length() <= 0){
            return;
        }
        text.deleteCharAt(text.length() - 1);
    }

    public synchronized void setWidthAndHeight(float width,float height){
        if (widthAndHeight == null) {
            widthAndHeight = new WidthAndHeight(width, height);
        } else {
            widthAndHeight.setWidthAndHeight(width,height);
        }
    }

    public synchronized void setWidth(float width) {
        if (widthAndHeight == null) {
            widthAndHeight = new WidthAndHeight(width, 0);
        } else {
            widthAndHeight.setWidth(width);
        }
    }

    public float getWidth() {
        return widthAndHeight == null ? 0 : widthAndHeight.getWidth();
    }

    public synchronized void setHeight(float height) {
        if (widthAndHeight == null) {
            widthAndHeight = new WidthAndHeight(0, height);
        } else {
            widthAndHeight.setHeight(height);
        }
    }

    public float getHeight() {
        return widthAndHeight == null ? 0 : widthAndHeight.getHeight();
    }

    public String reverse(){
        if(text != null) {
             return new StringBuilder(text).reverse().toString();
        }
        return "";
    }

    public LineText clear(){
        if(text != null) {
            text.delete(0, text.length());
        }
        if(widthAndHeight != null){
            widthAndHeight.setWidthAndHeight(0,0);
        }
        return this;
    }

    @Override
    public LineText clone() throws CloneNotSupportedException {
        LineText line = new LineText();
        if(text != null) {
            line.text = new StringBuilder(text);
        }
        if(widthAndHeight != null) {
            line.widthAndHeight = new WidthAndHeight(widthAndHeight.getWidth(), widthAndHeight.getHeight());
        }
        return line;
    }

    @Override
    public String toString() {
        return "LineText{" +
                "text=" + text +
                ", widthAndHeight=" + widthAndHeight +
                '}';
    }
}
