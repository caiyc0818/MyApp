package com.bcinfo.tripaway.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ServiceRotate3dAnimation extends Animation
{
    private float mFromDegree;
    private float mToDegree;
    private float mCenterX;
    private float mCenterY;
    private Camera mCamera;

    public ServiceRotate3dAnimation(float fromDegree, float toDegree, float centerX, float centerY)
    {
        this.mFromDegree = fromDegree;
        this.mToDegree = toDegree;
        this.mCenterX = centerX;
        this.mCenterY = centerY;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight)
    {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        final float FromDegree = mFromDegree;
        float degrees = FromDegree + (mToDegree - mFromDegree) * interpolatedTime;
        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Matrix matrix = t.getMatrix();
        if (degrees <= -76.0f)
        {
            degrees = -90.0f;
            mCamera.save();
            mCamera.rotateY(degrees);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        }
        else if (degrees >= 76.0f)
        {
            degrees = 90.0f;
            mCamera.save();
            mCamera.rotateY(degrees);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        }
        else
        {
            mCamera.save();
            // 这里很重要哦。
            mCamera.translate(0, 0, centerX);
            mCamera.rotateY(degrees);
            mCamera.translate(0, 0, -centerX);
            mCamera.getMatrix(matrix);
            mCamera.restore();
        }
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }
}