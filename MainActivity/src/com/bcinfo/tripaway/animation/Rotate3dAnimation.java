package com.bcinfo.tripaway.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/** 
 * An animation that rotates the view on the Y axis between two specified angles. 
 * This animation also adds a translation on the Z axis (depth) to improve the effect. 
 * 
 */
/*
 * Rotate3dAnimation是android api中提供的一个用于3D旋转动画的类  这个类是用Camera来实现的
 */
/**
 * 3D旋转立体动画
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年3月21日 10:16:22
 * 
 */
public class Rotate3dAnimation extends Animation {

	private final float mFromDegrees; // 旋转起始角度
	private final float mToDegrees; // 旋转最终角度
	private final float mCenterX; // 旋转时的x轴中心坐标
	private final float mCenterY; // y轴中心坐标
	private final float mDepthZ;
	private final boolean mReverse; // 是否可以反转

	private Camera mCamera;

	/**
	 * Creates a new 3D rotation on the Y axis. The rotation is defined by its
	 * start angle and its end angle. Both angles are in degrees. The rotation
	 * is performed around a center point on the 2D space, definied by a pair of
	 * X and Y coordinates, called centerX and centerY. When the animation
	 * starts, a translation on the Z axis (depth) is performed. The length of
	 * the translation can be specified, as well as whether the translation
	 * should be reversed in time.
	 * 
	 * 
	 * @param fromDegrees
	 *            the start angle of the 3D rotation
	 *            参数fromDegrees表示了3D旋转开始时的起始角度
	 * @param toDegrees
	 *            the end angle of the 3D rotation 参数toDegrees表示3D旋转的最终角度
	 * @param centerX
	 *            the X center of the 3D rotation 参数 centerX表示3D旋转时的x轴的中心旋转坐标
	 *            即绕着视图的坐标旋转点的x坐标旋转
	 * @param centerY
	 *            the Y center of the 3D rotation 参数centerY表示3D旋转时的y轴的旋转坐标
	 * @param reverse
	 *            true if the translation should be reversed, false otherwise
	 *            参数reverse为true 表示这个3D旋转变换是否应该反转 为true表示可以反转；false表示不能反转
	 */
	/*
	 * 自定义 Rotate3dAnimation 类的构造函数
	 */
	public Rotate3dAnimation(float fromDegrees, float toDegrees, float centerX,
			float centerY, float depthZ, boolean reverse) {
		mFromDegrees = fromDegrees;
		mToDegrees = toDegrees;
		mCenterX = centerX;
		mCenterY = centerY;
		mDepthZ = depthZ;
		mReverse = reverse;
	}

	@Override
	// 初始化3D旋转动画的尺寸的初始化方法
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		// 定义一个Camera对象
		mCamera = new Camera();
	}

	@Override
	// 下面的这个 applyTransformation()方法表示应用这个转换
	// 子类必须重写父类Animation的applyTransformation()方法
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		final float fromDegrees = mFromDegrees;
		float degrees = fromDegrees
				+ ((mToDegrees - fromDegrees) * interpolatedTime);

		final float centerX = mCenterX;
		final float centerY = mCenterY;
		final Camera camera = mCamera;

		final Matrix matrix = t.getMatrix();

		camera.save();
		if (mReverse) {
			camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
		} else {
			camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
		}
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();

		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}

}
