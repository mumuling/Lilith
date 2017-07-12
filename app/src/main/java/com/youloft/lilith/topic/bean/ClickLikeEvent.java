package com.youloft.lilith.topic.bean;

/**
 *
 */

public class ClickLikeEvent {
    public static int TYPE_POINT = 0;
    public static int TYPE_ANSWER = 1;
    public static int TYPE_AUTHOR = 2;
    public ClickLikeEvent(int type) {
        this.type = type;
    }

    public int type;
}

