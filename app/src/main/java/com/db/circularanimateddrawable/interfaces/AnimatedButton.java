package com.db.circularanimateddrawable.interfaces;


public interface AnimatedButton {
    void startAnimation();

    void revertAnimation();

    void revertAnimation(final OnAnimationEndListener onAnimationEndListener);

    void dispose();
}
