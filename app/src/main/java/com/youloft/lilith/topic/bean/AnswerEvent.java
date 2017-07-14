package com.youloft.lilith.topic.bean;

/**
 *
 */

public class AnswerEvent {
    public int position;
    public int count;
    public String nickNmae;
    public String content;
    public AnswerEvent(int count,int position,String name,String content) {
        this.position = position;
        this.count = count;
        this.nickNmae = name;
        this.content = content;

    }


}
