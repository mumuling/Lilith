package com.youloft.lilith.topic.widget;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 *
 */

public class Rotate3dAnimation extends Animation {
    private float fromDegree;
    private float toDegree;
    private float centerX;//centerX,centerY旋转围绕的中心点
    private float centerY;
    private Camera camera;
    private final static String TAG = "Rotate3dAnimation";

    public Rotate3dAnimation(float fromDegree, float toDegree,
                             float centerX, float centerY) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public Rotate3dAnimation(int fromDegree, int toDegree) {
        this.fromDegree = fromDegree;
        this.toDegree = toDegree;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
        this.centerX = width / 2;
        this.centerY = height / 2;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float degree = fromDegree + (toDegree - fromDegree) * interpolatedTime;
        final Matrix matrix = t.getMatrix();
        camera.save();//保存当前的画布状态,在save()之后对canvas的任何操作都会在restore()之后无效，但是画布上的内容还在
        camera.rotateY(degree);
        camera.getMatrix(matrix);// 根据camera动作产生一个matrix，赋给Transformation的matrix，以用来设置动画效果
        camera.restore();//将画布状态恢复到save之前的状态
        matrix.preTranslate(-centerX, -centerY);//在动画开始前，将View的中心移动到原点0,0
        matrix.postTranslate(centerX, centerY);//在动画结束后,将View移回原位
    }
}

