package com.sd.lib.animator.listener;

import android.animation.Animator;
import android.view.View;

public class InvisibleListener extends FLifecycleAnimatorListener
{
    public InvisibleListener(Lifecycle lifecycle)
    {
        super(lifecycle);
    }

    public InvisibleListener(Lifecycle lifecycle, View target)
    {
        super(lifecycle, target);
    }

    @Override
    public void onAnimationLifecycle(Animator animator)
    {
        final View target = getTarget();
        if (target == null)
            return;

        if (target.getVisibility() != View.INVISIBLE)
            target.setVisibility(View.INVISIBLE);
    }
}
