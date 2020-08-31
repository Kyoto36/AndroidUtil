package com.ls.comm_util_library;

import android.graphics.Path;

/**
 * @ClassName: PathUtils
 * @Description:
 * @Author: ls
 * @Date: 2020/8/31 15:40
 */
public class PathFactory {
    public static Path getCircle(float radius,float borderWidth){
        Path path = new Path();
        path.addCircle(radius,radius,radius - (borderWidth / 2),Path.Direction.CW);
        return path;
    }

    public static Path getSquare(int num, float radius, float padding){
        Path path = new Path();
        float x = 0F;
        float y = 0F;
        for (int i = 0; i < num; i++) {
            x = radius + radius * cos(360 / num * i);
            y = radius + radius * sin(360 / num * i);
            if(x < radius) x += padding; else x -= padding;
            if(y < radius) y += padding; else y -= padding;
            if (i == 0) {
                path.moveTo(x,y); //绘制起点
            } else {
                path.lineTo(x,y);
            }
        }
        path.close();
        return path;
    }

    private static float sin(int num) {
        return (float) Math.sin(num * Math.PI / 180);
    }

    private static float cos(int num) {
        return (float) Math.cos(num * Math.PI / 180);
    }
}
