/**
 * Copyright (c) 2011-2015, Unas 小强哥 (unas@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.sdk.msg.in.event;

import com.jfinal.weixin.sdk.msg.in.InMsg;

@SuppressWarnings("serial")
public abstract class EventInMsg extends InMsg {
    private static final String MSG_TYPE = "event";
    protected String event;

    public EventInMsg(String toUserName, String fromUserName, Integer createTime, String event) {
        super(toUserName, fromUserName, createTime, MSG_TYPE);
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
