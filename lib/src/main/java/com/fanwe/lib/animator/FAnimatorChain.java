package com.fanwe.lib.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FAnimatorChain implements AnimatorChain
{
    private final AnimatorSet mAnimatorSet = new AnimatorSet();
    private NodeAnimator mCurrent;

    private final boolean mIsDebug;
    private List<NodeAnimator> mListNode;

    FAnimatorChain(boolean isDebug)
    {
        mIsDebug = isDebug;
    }

    void setCurrent(NodeAnimator current)
    {
        if (current == null) throw new NullPointerException("current is null");

        if (current.getType() == NodeAnimator.Type.HEAD)
        {
            if (mCurrent == null)
                mAnimatorSet.play(current.toObjectAnimator());
            else
                throw new UnsupportedOperationException("HEAD is already set");
        } else
        {
            if (mCurrent == null)
                throw new UnsupportedOperationException("HEAD must be set before this");
        }

        mCurrent = current;
        addNodeIfNeed(current);
    }

    private void addNodeIfNeed(NodeAnimator animator)
    {
        if (mIsDebug)
        {
            if (mListNode == null) mListNode = new ArrayList<>();
            mListNode.add(animator);
        }
    }

    @Override
    public NodeAnimator with()
    {
        return with(null);
    }

    @Override
    public NodeAnimator with(View target)
    {
        final NodeAnimator animator = new NodeAnimator(NodeAnimator.Type.WITH, this);
        animator.setTarget(target);
        return withInternal(animator);
    }

    @Override
    public NodeAnimator withClone()
    {
        final NodeAnimator animator = mCurrent.clone();
        animator.setType(NodeAnimator.Type.WITH);
        return withInternal(animator);
    }

    private NodeAnimator withInternal(NodeAnimator animator)
    {
        initNodeAnim(animator);
        mAnimatorSet.play(mCurrent.toObjectAnimator()).with(animator.toObjectAnimator());
        setCurrent(animator);
        return animator;
    }

    @Override
    public NodeAnimator next()
    {
        return next(null);
    }

    @Override
    public NodeAnimator next(View target)
    {
        final NodeAnimator animator = new NodeAnimator(NodeAnimator.Type.NEXT, this);
        animator.setTarget(target);
        return nextInternal(animator);
    }

    private NodeAnimator nextInternal(NodeAnimator animator)
    {
        initNodeAnim(animator);
        mAnimatorSet.play(animator.toObjectAnimator()).after(mCurrent.toObjectAnimator());
        setCurrent(animator);
        return animator;
    }

    @Override
    public NodeAnimator delay(long time)
    {
        final NodeAnimator animator = new NodeAnimator(NodeAnimator.Type.DELAY, this);
        animator.setDuration(time);
        return nextInternal(animator);
    }

    private void initNodeAnim(NodeAnimator anim)
    {
        final View target = anim.getTarget();
        if (target == null)
        {
            anim.setTarget(mCurrent.getTarget());
        }
    }

    @Override
    public AnimatorSet toAnimatorSet()
    {
        return mAnimatorSet;
    }

    @Override
    public void start()
    {
        if (mIsDebug)
        {
            if (mListNode != null)
            {
                final StringBuilder sb = new StringBuilder("----------");
                for (NodeAnimator item : mListNode)
                {
                    switch (item.getType())
                    {
                        case NodeAnimator.Type.HEAD:
                            sb.append("\r\n").append("Head:").append(item.getTag());
                            break;
                        case NodeAnimator.Type.NEXT:
                            sb.append("\r\n").append("Next:").append(item.getTag());
                            break;
                        case NodeAnimator.Type.WITH:
                            sb.append(" With:").append(item.getTag());
                            break;
                        case NodeAnimator.Type.DELAY:
                            sb.append("\r\n").append("Delay:").append(item.getTag());
                            break;
                    }
                }
                Log.i(AnimatorChain.class.getSimpleName(), sb.toString());
            }
        }

        mAnimatorSet.start();
    }

    @Override
    public void startAsPop()
    {
        final ArrayList<android.animation.Animator> listChild = mAnimatorSet.getChildAnimations();
        if (listChild == null || listChild.isEmpty())
        {
            return;
        }
        final HashMap<View, ImageView> mapCache = new HashMap<>();
        for (Animator animator : listChild)
        {
            final View target = (View) ((ObjectAnimator) animator).getTarget();
            if (target == null) continue;

            final ImageView cache = mapCache.get(target);
            if (cache == null)
            {
                final Context context = target.getContext();
                if (context instanceof Activity)
                {
                    PopImageView imageView = new PopImageView(context);
                    imageView.setDrawingCacheView(target);
                    imageView.attachTarget(target);

                    animator.setTarget(imageView);
                    mapCache.put(target, imageView);
                }
            } else
            {
                animator.setTarget(cache);
            }
        }
        start();
    }

    @Override
    public boolean isRunning()
    {
        return mAnimatorSet.isRunning();
    }

    @Override
    public boolean isStarted()
    {
        return mAnimatorSet.isStarted();
    }

    @Override
    public void cancel()
    {
        mAnimatorSet.cancel();
    }
}
