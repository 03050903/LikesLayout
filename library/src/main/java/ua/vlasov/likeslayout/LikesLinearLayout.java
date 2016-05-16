package ua.vlasov.likeslayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * LinearLayout based implementation of likes layout..
 */
public class LikesLinearLayout extends LinearLayout implements LikesDrawer.LikesLayout {


    private OnChildTouchListener mOnChildTouchListener;

    @Nullable
    private LikesDrawer mLikesDrawer;

    public LikesLinearLayout(Context context) {
        this(context, null);
    }

    public LikesLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikesLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LikesLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        setWillNotDraw(false);
        if (attrs != null) {
            final LikesAttributes likesAttributes = LikesAttributes.create(context, attrs, LikesAttributes.LAYOUT_TYPE_LINEAR);
            mLikesDrawer = new LikesDrawer(this, likesAttributes);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams(((LayoutParams) p));
        }
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        if (layoutParams.mAttributes.isLikesModeEnabled()) {
            child.setOnTouchListener(mLikesDrawer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLikesDrawer != null) {
            mLikesDrawer.onDraw(canvas);
        }
    }

    /**
     * Set instance of OnChildTouchListener.
     * @param onChildTouchListener instance of OnChildTouchListener
     */
    public void setOnChildTouchListener(@Nullable OnChildTouchListener onChildTouchListener) {
        mOnChildTouchListener = onChildTouchListener;
    }

    @Override
    public void onChildTouched(View child) {
        if (mOnChildTouchListener != null) {
            mOnChildTouchListener.onChildTouched(child);
        }
    }

    @Override
    public void onChildReleased(View child, boolean isCanceled) {
        if (mOnChildTouchListener != null) {
            mOnChildTouchListener.onChildReleased(child, isCanceled);
        }
    }

    @Override
    public void onLikeProduced(View child) {
        if (mOnChildTouchListener != null) {
            mOnChildTouchListener.onLikeProduced(child);
        }
    }

    /**
     * Per-child layout information associated with LikesLinearLayout.
     */
    public static class LayoutParams extends LinearLayout.LayoutParams implements LikesDrawer.LikesLayoutParams {

        private final LikesAttributes mAttributes;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAttributes = LikesAttributes.create(c, attrs, LikesAttributes.LAYOUT_TYPE_LINEAR);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            mAttributes = LikesAttributes.empty();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            mAttributes = LikesAttributes.empty();
        }

        @SuppressWarnings("IncompleteCopyConstructor")
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public LayoutParams(LayoutParams source) {
            super(source);
            mAttributes = LikesAttributes.copy(source.mAttributes);
        }

        @Override
        public LikesAttributes getAttributes() {
            return mAttributes;
        }
    }
}
