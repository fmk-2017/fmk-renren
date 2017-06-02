package com.cfkj.dushikuaibang.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2017/4/20.
 */

public class ShareUtils {

    private static Context mContext;
    private static ShareUtils shareUtils;
    private static Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Log.e("tag", "handleMessage");
            switch (msg.arg1) {
                case 1: {
                    // 成功
                    String str = (String) msg.obj;
                    Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2: {
                    // 失败
                    Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();

                    String expName = msg.obj.getClass().getSimpleName();
                    if ("WechatClientNotExistException".equals(expName)
                            || "WechatTimelineNotSupportedException".equals(expName)
                            || "WechatFavoriteNotSupportedException".equals(expName)) {
                        Toast.makeText(mContext, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                case 3: {
                    Toast.makeText(mContext, "取消····", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
            return false;
        }
    };
    private int MSG_ACTION_CCALLBACK = 1;
    private Handler handler;

    public static synchronized ShareUtils getInstance(Context context) {
        if (shareUtils == null) {
            shareUtils = new ShareUtils();
            mContext = context;
        }
        return shareUtils;
    }

    public void sharePlat(final String platName) {
        handler = new Handler(Looper.getMainLooper(), callback);
        final Platform plat = ShareSDK.getPlatform(platName);
        //判断指定平台是否已经完成授权
        if (plat.isAuthValid()) {
            plat.removeAccount(false);
            return;
        }
        plat.SSOSetting(false);//此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        plat.setPlatformActionListener(new PlatformActionListener() {// 设置分享事件回调

            @Override
            public void onError(Platform platform, int action, Throwable t) {
                Log.e("tag", "onError");
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 2;
                msg.arg2 = action;
                msg.obj = t;
                UIHandler.sendMessage(msg, callback);
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 1;
                msg.arg2 = action;
                System.out.println(res);
                //获取资料
                String nickname = platform.getDb().getUserName();//获取用户名字
                String iconUri = platform.getDb().getUserIcon(); //获取用户头像
                platform.getDb().get("openid");
                String openid = platform.getDb().getUserId();
                String token = platform.getDb().getToken();
                String refresh_token111 = platform.getDb().get("refresh_token");
                String toJSLogin = "{" +
                        "  \"headimgurl\" : \"" + iconUri + "\","
                        + "  \"nickname\" : \"" + nickname + "\","
                        + "  \"openid\" :  \"" + openid + "\""
                        + "}";
                Log.d("WXCHATLOGIN", toJSLogin);
                msg.obj = toJSLogin;
                UIHandler.sendMessage(msg, callback);
            }

            @Override
            public void onCancel(Platform platform, int action) {
                Message msg = new Message();
                msg.what = MSG_ACTION_CCALLBACK;
                msg.arg1 = 3;
                msg.arg2 = action;
                msg.obj = platform;
                UIHandler.sendMessage(msg, callback);
            }
        });
        plat.showUser(null);//授权并获取用户信息
    }

}
