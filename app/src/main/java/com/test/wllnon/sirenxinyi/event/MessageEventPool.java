package com.test.wllnon.sirenxinyi.event;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/3/31.
 * Pay attention: Now only support the pool of receivedDataMessageEvent
 */
public class MessageEventPool {
    private static MessageEventPool instance = new MessageEventPool();

    private int startSize = 200;
    private int maxSize = 250;

    private final LinkedList<ReceivedDataMessageEvent> pool = new LinkedList<>();

    public static MessageEventPool getInstance() {
        return instance;
    }

    private MessageEventPool() {}

    public ReceivedDataMessageEvent getMessageEvent() {
        synchronized (pool) {
            if (pool.size() == 0) {
                pool.addLast(new ReceivedDataMessageEvent());
            }
            return pool.pollFirst();
        }
    }

    public void returnMessageEvent(ReceivedDataMessageEvent messageEvent) {
        synchronized (pool) {
            if (pool.size() < maxSize) {
                pool.addLast(messageEvent);
            }
        }
    }

    public MessageEventPool configStartSize(int startSize) {
        this.startSize = (startSize < 0 ? 0 : startSize);
        return this;
    }

    public MessageEventPool configMaxSize(int maxSize) {
        this.maxSize = (maxSize < startSize ? startSize : maxSize);
        return this;
    }

    public MessageEventPool initialize() {
        for (int i = 0; i < startSize; ++i) {
            pool.addLast(new ReceivedDataMessageEvent());
        }
        return this;
    }
}
