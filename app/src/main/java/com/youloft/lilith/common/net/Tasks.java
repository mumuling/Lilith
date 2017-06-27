package com.youloft.lilith.common.net;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import bolts.Task;

/**
 * Desc: 
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/26 13:43
 * @see 
*/
public class Tasks {

    public static final Executor IO_EXECUTOR = Executors.newFixedThreadPool(3);
    public static final Executor NET_EXECUTOR = Executors.newFixedThreadPool(5);
    public static final Executor COM_EXECUTOR = Executors.newCachedThreadPool();
    public static final Executor UI_EXECUTOR = Task.UI_THREAD_EXECUTOR;
    public static final Executor DB_EXECUTOR = Executors.newFixedThreadPool(2);
    public static final Executor VIEW_EXCUTORS = Executors.newFixedThreadPool(4);


    public static boolean isOk(Task task) {
        if (task.getError() != null) {
            task.getError().printStackTrace();
        }
        return task != null && task.isCompleted() && !task.isCancelled() && !task.isFaulted();
    }
}
