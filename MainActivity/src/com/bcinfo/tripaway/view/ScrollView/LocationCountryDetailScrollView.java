package com.bcinfo.tripaway.view.ScrollView;

import com.bcinfo.tripaway.R;
import com.bcinfo.tripaway.listener.PersonalScrollViewListener;
import com.bcinfo.tripaway.utils.LogUtil;

import com.bcinfo.tripaway.view.ScrollView.PersonalNewScrollView.onTurnListener;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ScrollView;

/**
 * 目的地-城市详情 自定义scrollview 实现图片动态下拉的显示效果
 * 
 * @function
 * @author caihelin
 * @version 1.0 2015年4月15日 20:30:11
 */
public class LocationCountryDetailScrollView extends ScrollView {

	/*
	 * 实现ScrollView滑倒底部回弹
	 * 
	 */
	private static final String TAG = "ElasticScrollView";

	// 移动因子, 是一个百分比, 比如手指移动了100px, 那么View就只移动50px
	// 目的是达到一个延迟的效果
	private static final float MOVE_FACTOR = 0.3f;

	// 松开手指后, 界面回到正常位置需要的动画时间
	private static final int ANIM_TIME = 100;

	// ScrollView的子View， 也是ScrollView的唯一一个子View
	private View contentView;

	// 手指按下时的Y值, 用于在移动时计算移动距离
	// 如果按下时不能上拉和下拉， 会在手指移动时更新为当前手指的Y值
	private float startY;

	// 用于记录正常的布局位置
	private Rect originalRect = new Rect();

	// 手指按下时记录是否可以继续下拉
	private boolean canPullDown = false;

	// 手指按下时记录是否可以继续上拉
	private boolean canPullUp = false;

	// 在手指滑动的过程中记录是否移动了布局
	private boolean isMoved = false;

	private PersonalScrollViewListener mScrollListener;

	public LocationCountryDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

	}

	public LocationCountryDetailScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public LocationCountryDetailScrollView(Context context) {
		super(context);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		return super.onInterceptTouchEvent(ev);
	}

	/******************************* jiangchangshan ****************************************/
	private View inner;// 孩子View
	private float touchY;// 点击时Y坐标
	private float deltaY;// Y轴滑动的距离
	private float initTouchY;// 首次点击的Y坐标
	private boolean shutTouch = false;// 是否关闭ScrollView的滑动.
	private Rect normal = new Rect();// 矩形(这里只是个形式，只是用于判断是否需要动画.)
	private boolean isMoveing = false;// 是否开始移动.
	private ImageView imageView;// 背景图控件.
	// private View line_up;// 上线
	// private int line_up_top;// 上线的top
	// private int line_up_bottom;// 上线的bottom
	private int initTop, initBottom;// 初始高度
	private int current_Top, current_Bottom;// 拖动时时高度。
	private int current_img_move = 0;// 当前inner移动距离
	private int current_inner_move = 0;// 当前inner移动距离
	private int img_move_d = 0;// 恢复时图片一次移动距离
	private int inner_move_d = 0;// 恢复时inner一次移动距离
	private boolean isBottomFlag;// boolean变量 表示自定义下拉的底端界限
	// private int lineUp_current_Top, lineUp_current_Bottom;// 上线
	private onTurnListener turnListener;

	private boolean isBack = false;// 是否开始移动
	
	// private GridView infoMain;
	// 状态：上部，下部，默认
	private enum State {
		UP, DOWN, NOMAL
	};

	// 默认状态
	private State state = State.NOMAL;

	public void setTurnListener(onTurnListener turnListener) {
		this.turnListener = turnListener;
	}

	public PersonalScrollViewListener getScrollListener() {
		return mScrollListener;
	}

	public void setScrollListener(PersonalScrollViewListener scrollListener) {
		this.mScrollListener = scrollListener;
	}

	// 注入背景图
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	protected void onScrollChanged(int positionX, int positionY, int prePositionX, int prePositionY) {
		super.onScrollChanged(positionX, positionY, prePositionX, prePositionY);
		// 当自定义ScrollView控件滚动时自动调用onScrollChanged()方法 这时将方法的执行交给外部来完成
		if (mScrollListener != null) {
			// 判断接口对象是否为空，若不为空，那么调用onScrollChanged()方法
			mScrollListener.onScrollChanged(this, positionX, positionY, prePositionX, prePositionY);
		} else {
			return;
		}
	}

	// public void setInfoMain(GridView infoMain)
	// {
	// this.infoMain = infoMain;
	// }
	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			inner = getChildAt(0).findViewById(R.id.layout_location_country_detail_container);
			contentView = getChildAt(0);
		}
	}

	/** touch 事件处理 **/
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (inner != null) {
			commOnTouchEvent(ev);
		}
		// ture：禁止控件本身的滑动.
		if (shutTouch)
			return true;
		else
			return super.onTouchEvent(ev);
	}

//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		super.onLayout(changed, l, t, r, b);
//
//		if (contentView == null)
//			return;
//
//		// ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
//		originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
//	}

//	/**
//	 * 在触摸事件中, 处理上拉和下拉的逻辑
//	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//
//		if (contentView == null) {
//			return super.dispatchTouchEvent(ev);
//		}
//
//		int action = ev.getAction();
//
//		switch (action) {
//		case MotionEvent.ACTION_DOWN:
//
//			// 判断是否可以上拉和下拉
//			// canPullDown = isCanPullDown();
//			canPullUp = isCanPullUp();
//
//			// 记录按下时的Y值
//			startY = ev.getY();
//			break;
//
//		case MotionEvent.ACTION_UP:
//
//			if (!isMoved)
//				break; // 如果没有移动布局， 则跳过执行
//
//			// 开启动画
//			TranslateAnimation anim = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
//			anim.setDuration(ANIM_TIME);
//
//			contentView.startAnimation(anim);
//
//			// 设置回到正常的布局位置
//			contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
//
//			// 将标志位设回false
//			canPullDown = false;
//			canPullUp = false;
//			isMoved = false;
//
//			break;
//		case MotionEvent.ACTION_MOVE:
//
//			// 在移动的过程中， 既没有滚动到可以上拉的程度， 也没有滚动到可以下拉的程度
//			if (!canPullDown && !canPullUp) {
//				startY = ev.getY();
//				// canPullDown = isCanPullDown();
//				canPullUp = isCanPullUp();
//
//				break;
//			}
//
//			// 计算手指移动的距离
//			float nowY = ev.getY();
//			int deltaY = (int) (nowY - startY);
//
//			// 是否应该移动布局
//			boolean shouldMove = (canPullDown && deltaY > 0) // 可以下拉， 并且手指向下移动
//					|| (canPullUp && deltaY < 0) // 可以上拉， 并且手指向上移动
//					|| (canPullUp && canPullDown); // 既可以上拉也可以下拉（这种情况出现在ScrollView包裹的控件比ScrollView还小）
//
//			if (shouldMove) {
//				// 计算偏移量
//				int offset = (int) (deltaY * MOVE_FACTOR);
//
//				// 随着手指的移动而移动布局
//				contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right,
//						originalRect.bottom + offset);
//
//				isMoved = true; // 记录移动了布局
//			}
//
//			break;
//		default:
//			break;
//		}
//
//		return super.dispatchTouchEvent(ev);
//	}
//
//	/**
//	 * 判断是否滚动到顶部
//	 */
//	private boolean isCanPullDown() {
//		return getScrollY() == 0 || contentView.getHeight() < getHeight() + getScrollY();
//	}
//
//	/**
//	 * 判断是否滚动到底部
//	 */
//	private boolean isCanPullUp() {
//		return contentView.getHeight() <= getHeight() + getScrollY();
//	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		if(isBack)return;
		int action = ev.getAction();
		if (0 == initTouchY) {
			initTouchY = ev.getY();
			current_Top = initTop = imageView.getTop();

			current_Bottom = initBottom = imageView.getBottom();

		}
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			
			mHandler.removeMessages(1);
			break;
		case MotionEvent.ACTION_UP:
			/** 回缩动画 **/
			if (isNeedAnimation()) {
				isBack=true;
				img_move_d = (initTop - current_Top) / 15;
				inner_move_d = current_inner_move / 15;
				mHandler.sendEmptyMessageDelayed(1, 10);
				// animation();
			}
			if (getScrollY() == 0) {
				state = State.NOMAL;

			}
			isMoveing = false;
			initTouchY = 0;
			touchY = 0;
			shutTouch = false;
			break;
		/***
		 * 排除出第一次移动计算，因为第一次无法得知deltaY的高度， 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0.
		 * 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:
			touchY = ev.getY();
			deltaY = touchY - initTouchY;// 滑动距离
			/** 对于首次Touch操作要判断方位：UP OR DOWN **/
			if (deltaY < 0 && state == State.NOMAL) {
				state = State.UP;
			} else if (deltaY > 0 && state == State.NOMAL) {
				state = State.DOWN;
			}
			if (state == State.UP) {
				deltaY = deltaY < 0 ? deltaY : 0;
				isMoveing = false;
				shutTouch = false;
			} else if (state == State.DOWN) {
				if (getScrollY() <= deltaY) {
					if (deltaY > 30) {
						turnListener.onTurn(true);
						isBottomFlag = true;
					}
					shutTouch = true;
					isMoveing = true;
				}
				deltaY = deltaY < 0 ? 0 : deltaY;
				// if (getScrollY() <= deltaY)
				// {
				// shutTouch = true;
				// isMoveing = true;
				// }
				// deltaY = deltaY < 0 ? 0 : deltaY;
			}
			if (isMoveing) {
				LogUtil.i("举行放大", "", "");
				// 初始化头部矩形
				if (normal.isEmpty()) {
					// 保存正常的布局位置
					normal.set(inner.getLeft(), inner.getTop(), inner.getRight(), inner.getBottom());
					current_Top = initTop = imageView.getTop();
					current_Bottom = initBottom = imageView.getBottom();
				}
				// 移动布局(手势移动的1/3)
				float inner_move_H = deltaY / 3;
				current_inner_move = (int) inner_move_H;
				// LogUtil.i(TAG, "ACTION_MOVE", "inner_move_H=" +
				// inner_move_H);
				if (isBottomFlag) {
					// inner.layout(normal.left, normal.top, normal.right,
					// normal.bottom);
					inner.layout(normal.left, (int) (normal.top + inner_move_H), normal.right,
							(int) (normal.bottom + inner_move_H));
				} else {
					// inner.layout(normal.left, (int) (normal.top +
					// inner_move_H), normal.right, (int) (normal.bottom +
					// inner_move_H));
				}

				/** image_bg **/
				float image_move_H = deltaY / 4;
				current_img_move = (int) image_move_H;
				current_Top = (int) (initTop - image_move_H);
				current_Bottom = (int) (initBottom + image_move_H);

				imageView.layout(imageView.getLeft(), current_Top, imageView.getRight(), current_Bottom);

			}
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				current_img_move = current_img_move - img_move_d;
				current_inner_move = current_inner_move - inner_move_d;
				if (current_img_move > 0 && current_inner_move > 0) {
					imageView.layout(imageView.getLeft(), initTop - current_img_move, imageView.getRight(),
							initBottom + current_img_move);
					inner.layout(normal.left, normal.top + current_inner_move, normal.right,
							normal.bottom + current_inner_move);
					// inner.layout(normal.left, normal.top, normal.right,
					// normal.bottom);// 保持指定的view原始的位置
					mHandler.sendEmptyMessageDelayed(1, 10);
				} else {
					isBack=false;
					imageView.layout(imageView.getLeft(), (int) initTop, imageView.getRight(), (int) initBottom);
					inner.layout(normal.left, normal.top, normal.right, normal.bottom);// 保持指定的View原来的位置
					normal.setEmpty();
				}

				break;
			case 2:
				break;
			default:
				break;
			}
		}
	};

	/***
	 * 回缩动画
	 */
	// public void animation()
	// {
	// TranslateAnimation image_Anim = new TranslateAnimation(0, 0,
	// Math.abs(initTop - current_Top), 0);
	// image_Anim.setDuration(900);
	// imageView.startAnimation(image_Anim);
	// imageView.layout(imageView.getLeft(), (int) initTop,
	// imageView.getRight(), (int) initBottom);
	// // 开启移动动画
	// TranslateAnimation inner_Anim = new TranslateAnimation(0, 0,
	// inner.getTop(), normal.top);
	// inner_Anim.setDuration(900);
	// inner.startAnimation(inner_Anim);
	// inner.layout(normal.left, normal.top, normal.right, normal.bottom);
	// normal.setEmpty();
	// }
	/** 是否需要开启动画 **/
	public boolean isNeedAnimation() {
		return !normal.isEmpty();
	}

	/***
	 * 执行翻转
	 * 
	 * @author jia
	 * 
	 */
	public interface onTurnListener {
		/** 必须达到一定程度才执行 **/
		void onTurn(boolean isShowing);
	}
}
