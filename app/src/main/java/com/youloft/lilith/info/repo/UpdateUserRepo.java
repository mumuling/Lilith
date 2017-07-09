package com.youloft.lilith.info.repo;

import com.youloft.lilith.common.AbstractDataRepo;
import com.youloft.lilith.common.net.Urls;
import com.youloft.lilith.info.bean.CheckLoginBean;
import com.youloft.lilith.info.bean.OldPasswordBean;
import com.youloft.lilith.info.bean.UpdateUserInfoBean;
import com.youloft.lilith.login.bean.ModifyPasswordBean;

import java.util.HashMap;

import io.reactivex.Flowable;

/**
 * Created by gyh on 2017/7/7.
 */

public class UpdateUserRepo extends AbstractDataRepo{

    //确认老密码
    static HashMap<String,String> paramsOldPassword = new HashMap();
    public static Flowable<OldPasswordBean> checkOldPassword(String uid, String pwd) {
        paramsOldPassword.put("uid",uid);
        paramsOldPassword.put("pwd",pwd);
        return unionFlow(Urls.CHECK_OLD_PASSWORD, null, paramsOldPassword, true, OldPasswordBean.class, "old_password", 0);
    }

    //修改密码
    static HashMap<String,String> paramsUpdatePassword = new HashMap();
    public static Flowable<ModifyPasswordBean> updatePassword(String uid, String oldpwd,String newpwd) {
        paramsUpdatePassword.put("uid",uid);
        paramsUpdatePassword.put("oldpwd",oldpwd);
        paramsUpdatePassword.put("newpwd",newpwd);
        return unionFlow(Urls.MODIFY_PASSWORD, null, paramsUpdatePassword, true, ModifyPasswordBean.class, "update_password", 0);
    }

    //修改用户信息
    static HashMap<String,String> paramsUpdateUserInfo = new HashMap();
    public static Flowable<UpdateUserInfoBean> updateUserInfo(String uid, String nickName, String headImg,
                                                              String sex,String birthDay,String birthPlace,String birthLongi,
                                                              String birthLati,String livePlace,String liveLongi,String liveLati ) {
        paramsUpdateUserInfo.put("uid",uid);
        paramsUpdateUserInfo.put("NickName",nickName);
        paramsUpdateUserInfo.put("HeadImg",headImg);
        paramsUpdateUserInfo.put("Sex",sex);
        paramsUpdateUserInfo.put("BirthDay",birthDay);
        paramsUpdateUserInfo.put("BirthPlace",birthPlace);
        paramsUpdateUserInfo.put("BirthLongi",birthLongi);
        paramsUpdateUserInfo.put("BirthLati",birthLati);
        paramsUpdateUserInfo.put("LivePlace",livePlace);
        paramsUpdateUserInfo.put("LiveLongi",liveLongi);
        paramsUpdateUserInfo.put("LiveLati",liveLati);

        return unionFlow(Urls.UPDATE_USER_INFO, null, paramsUpdateUserInfo, true, UpdateUserInfoBean.class, "update_user_info", 0);
    }


    //起始检查用户登录状态
    static HashMap<String,String> paramsCheckLoginStatus = new HashMap();
    public static Flowable<CheckLoginBean> checkLoginStatus(String uid) {
        paramsCheckLoginStatus.put("uid",uid);
        return unionFlow(Urls.CHECK_LOGIN_STATUS, null, paramsCheckLoginStatus, true, CheckLoginBean.class, "check_login", 0);
    }

    public static Flowable<String> updateImg(String imgBase64, String nameEx){
        HashMap<String, String> param = new HashMap<>();
        param.put("data", imgBase64);
        param.put("ext", nameEx);
        return post(Urls.UPLOAD_FILE, null, param, true, String.class, "img_user_info", 0);
    }
}
