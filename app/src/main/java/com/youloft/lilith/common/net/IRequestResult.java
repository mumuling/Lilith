package com.youloft.lilith.common.net;

/**
 * Desc: 请求回调接口
 * Change: 
 * 
 * @version 
 * @author zchao created at 2017/6/28 11:26
 * @see 
*/
public interface IRequestResult<T> {

    public void onSuccessful(T result);

    public void onFailure(String errorMsg);

    public void onCompleted();
}
