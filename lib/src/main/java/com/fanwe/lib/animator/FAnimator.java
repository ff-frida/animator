/*
 * Copyright (C) 2017 zhengjun, fanwe (http://www.fanwe.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fanwe.lib.animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.fanwe.lib.animator.listener.OnStartVisible;

import java.util.ArrayList;

/**
 * 对ObjectAnimator进行封装提供更方便的调用方法
 */
public class FAnimator implements PropertyAnimator, Cloneable
{
    private ObjectAnimator mAnimator = new ObjectAnimator();

    private int[] mTargetLocation;
    private int[] mTempLocation;
    private AlignType mAlignType = AlignType.TopLeft;

    public FAnimator(View target)
    {
        mAnimator.setFloatValues(0);

        setTarget(target);
        addListener(new OnStartVisible());
    }

    @Override
    public FAnimator setTarget(View target)
    {
        mAnimator.setTarget(target);
        return this;
    }

    @Override
    public View getTarget()
    {
        return (View) mAnimator.getTarget();
    }

    @Override
    public FAnimator setDuration(long duration)
    {
        mAnimator.setDuration(duration);
        return this;
    }

    @Override
    public long getDuration()
    {
        return mAnimator.getDuration();
    }

    @Override
    public FAnimator setRepeatCount(int count)
    {
        if (count < 0)
        {
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        } else
        {
            mAnimator.setRepeatCount(count);
        }
        return this;
    }

    @Override
    public int getRepeatCount()
    {
        return mAnimator.getRepeatCount();
    }

    @Override
    public FAnimator setInterpolator(TimeInterpolator interpolator)
    {
        mAnimator.setInterpolator(interpolator);
        return this;
    }

    @Override
    public TimeInterpolator getInterpolator()
    {
        return mAnimator.getInterpolator();
    }

    @Override
    public FAnimator setStartDelay(long delay)
    {
        mAnimator.setStartDelay(delay);
        return this;
    }

    @Override
    public long getStartDelay()
    {
        return mAnimator.getStartDelay();
    }

    @Override
    public FAnimator addListener(Animator.AnimatorListener listener)
    {
        if (listener != null)
        {
            if (!containsListener(listener))
            {
                mAnimator.addListener(listener);
            }
        }
        return this;
    }

    private boolean containsListener(Animator.AnimatorListener listener)
    {
        final ArrayList<Animator.AnimatorListener> list = mAnimator.getListeners();
        if (list == null) return false;
        return list.contains(listener);
    }

    @Override
    public FAnimator removeListener(Animator.AnimatorListener listener)
    {
        mAnimator.removeListener(listener);
        return this;
    }

    @Override
    public FAnimator clearListener()
    {
        final ArrayList<Animator.AnimatorListener> list = mAnimator.getListeners();
        if (list != null) list.clear();
        return this;
    }

    @Override
    public void start()
    {
        mAnimator.start();
    }

    @Override
    public boolean isRunning()
    {
        return mAnimator.isRunning();
    }

    @Override
    public boolean isStarted()
    {
        return mAnimator.isStarted();
    }

    @Override
    public void cancel()
    {
        mAnimator.cancel();
    }

    @Override
    public FAnimator x(float... values)
    {
        mAnimator.setPropertyName(X);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator y(float... values)
    {
        mAnimator.setPropertyName(Y);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator translationX(float... values)
    {
        mAnimator.setPropertyName(TRANSLATION_X);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator translationY(float... values)
    {
        mAnimator.setPropertyName(TRANSLATION_Y);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator alpha(float... values)
    {
        mAnimator.setPropertyName(ALPHA);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator scaleX(float... values)
    {
        mAnimator.setPropertyName(SCALE_X);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator scaleY(float... values)
    {
        mAnimator.setPropertyName(SCALE_Y);
        mAnimator.setFloatValues(values);
        return this;
    }

    @Override
    public FAnimator rotation(float... values)
    {
        mAnimator.setPropertyName(ROTATION);
        mAnimator.setFloatValues(values);
        setInterpolator(new LinearInterpolator());
        return this;
    }

    @Override
    public FAnimator rotationX(float... values)
    {
        mAnimator.setPropertyName(ROTATION_X);
        mAnimator.setFloatValues(values);
        setInterpolator(new LinearInterpolator());
        return this;
    }

    @Override
    public FAnimator rotationY(float... values)
    {
        mAnimator.setPropertyName(ROTATION_Y);
        mAnimator.setFloatValues(values);
        setInterpolator(new LinearInterpolator());
        return this;
    }

    @Override
    public boolean isEmptyProperty()
    {
        final String propertyName = mAnimator.getPropertyName();
        return TextUtils.isEmpty(propertyName) || "null".equals(propertyName);
    }

    /**
     * 获得内部包装的ObjectAnimator对象
     *
     * @return
     */
    public ObjectAnimator get()
    {
        return mAnimator;
    }

    /**
     * 设置ObjectAnimator对象
     *
     * @param animator
     */
    public void setAnimator(ObjectAnimator animator)
    {
        if (animator == null) throw new NullPointerException("animator is null");
        mAnimator = animator;
    }

    @Override
    public FAnimator clone()
    {
        try
        {
            FAnimator clone = (FAnimator) super.clone();
            clone.setAnimator(mAnimator.clone());
            clone.clearListener();
            clone.addListener(new OnStartVisible());
            return clone;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //----------extend start----------

    private void updateTargetLocation()
    {
        final View target = getTarget();
        if (target == null) return;

        if (mTargetLocation == null) mTargetLocation = new int[]{0, 0};
        target.getLocationOnScreen(mTargetLocation);
    }

    private void updateTempLocation(View view)
    {
        if (view == null) return;

        if (mTempLocation == null) mTempLocation = new int[]{0, 0};
        view.getLocationOnScreen(mTempLocation);
    }

    /**
     * 移动到屏幕x坐标
     *
     * @param values
     * @return
     */
    public FAnimator moveToX(float... values)
    {
        if (values != null && values.length > 0)
        {
            updateTargetLocation();

            final float[] realValues = new float[values.length];
            for (int i = 0; i < values.length; i++)
            {
                final float real = (values[i] - mTargetLocation[0]) + getTarget().getTranslationX();
                realValues[i] = real;
            }
            return translationX(realValues);
        } else
        {
            return translationX(values);
        }
    }

    /**
     * 移动到屏幕y坐标
     *
     * @param values
     * @return
     */
    public FAnimator moveToY(float... values)
    {
        if (values != null && values.length > 0)
        {
            updateTargetLocation();

            final float[] realValues = new float[values.length];
            for (int i = 0; i < values.length; i++)
            {
                final float real = (values[i] - mTargetLocation[1]) + getTarget().getTranslationY();
                realValues[i] = real;
            }
            return translationY(realValues);
        } else
        {
            return translationY(values);
        }
    }

    /**
     * 移动到view的屏幕x坐标
     *
     * @param views
     * @return
     */
    public FAnimator moveToX(View... views)
    {
        if (views != null && views.length > 0)
        {
            final float[] values = new float[views.length];
            for (int i = 0; i < views.length; i++)
            {
                final View view = views[i];
                updateTempLocation(view);
                values[i] = mTempLocation[0];

                if (mAlignType == AlignType.Center)
                {
                    final int delta = view.getWidth() / 2 - getTarget().getWidth() / 2;
                    values[i] = mTempLocation[0] + delta;
                }
            }
            return moveToX(values);
        } else
        {
            return this;
        }
    }

    /**
     * 移动到view的屏幕y坐标
     *
     * @param views
     * @return
     */
    public FAnimator moveToY(View... views)
    {
        if (views != null && views.length > 0)
        {
            final float[] values = new float[views.length];
            for (int i = 0; i < views.length; i++)
            {
                final View view = views[i];
                updateTempLocation(view);
                values[i] = mTempLocation[1];

                if (mAlignType == AlignType.Center)
                {
                    final int delta = view.getHeight() / 2 - getTarget().getHeight() / 2;
                    values[i] = mTempLocation[1] + delta;
                }
            }
            return moveToY(values);
        } else
        {
            return this;
        }
    }

    /**
     * 缩放x到views的宽度
     *
     * @param views
     * @return
     */
    public FAnimator scaleX(View... views)
    {
        if (views != null && views.length > 0)
        {
            final float[] values = new float[views.length];
            for (int i = 0; i < views.length; i++)
            {
                values[i] = ((float) views[i].getWidth()) / ((float) getTarget().getWidth());
            }
            return scaleX(values);
        } else
        {
            return this;
        }
    }

    /**
     * 缩放y到views的高度
     *
     * @param views
     * @return
     */
    public FAnimator scaleY(View... views)
    {
        if (views != null && views.length > 0)
        {
            final float[] values = new float[views.length];
            for (int i = 0; i < views.length; i++)
            {
                values[i] = ((float) views[i].getHeight()) / ((float) getTarget().getHeight());
            }
            return scaleY(values);
        } else
        {
            return this;
        }
    }

    /**
     * 设置对齐方式，仅对{@link #moveToX(View...)}和{@link #moveToY(View...)}有效
     *
     * @param alignType
     * @return
     */
    public FAnimator setAlignType(AlignType alignType)
    {
        if (alignType == null) throw new NullPointerException("alignType is null");
        mAlignType = alignType;
        return this;
    }

    public enum AlignType
    {
        /**
         * View左上角对齐
         */
        TopLeft,
        /**
         * View中间对齐
         */
        Center
    }

    //----------extend end----------
}
