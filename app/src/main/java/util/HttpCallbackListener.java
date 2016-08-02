package util;

/**
 * 回调服务返回的结果
 * Created by Administrator on 2016/8/1.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
