package com.youloft.lilith.info.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.youloft.lilith.R;
import com.youloft.lilith.common.base.BaseActivity;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.common.utils.LoginUtils;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.bean.FeedBackBean;
import com.youloft.lilith.info.repo.UpdateUserRepo;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.ui.view.BaseToolBar;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 意见反馈页面
 * <p>
 * Created by gyh on 2017/6/30.
 */
@Route(path = "/test/FeedBackActivity")
public class FeedBackActivity extends BaseActivity {
    @BindView(R.id.btl_feedback)
    BaseToolBar btlFeedback; //标题
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent; //反馈内容
    @BindView(R.id.et_feedback_phone)
    EditText etFeedbackPhone; //用户电话
    private String mApiVersion; //系统版本

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initTitle();
        mApiVersion = String.valueOf(android.os.Build.VERSION.SDK_INT);
        etFeedbackPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
    }

    /**
     * Title相关的设置
     */
    private void initTitle() {
        btlFeedback.setTitle(getResources().getString(R.string.feed_back));
        btlFeedback.setShowShareBtn(false);
        btlFeedback.setOnToolBarItemClickListener(new BaseToolBar.OnToolBarItemClickListener() {
            @Override
            public void OnBackBtnClick() {
                onBackPressed();
            }

            @Override
            public void OnTitleBtnClick() {

            }

            @Override
            public void OnShareBtnClick() {

            }

            @Override
            public void OnSaveBtnClick() {

            }
        });
    }

    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        //1.校验反馈内容和电话号码是否为空
        String feedContent = etFeedbackContent.getText().toString();
        String feedPhone = etFeedbackPhone.getText().toString();
        if (TextUtils.isEmpty(feedContent) || TextUtils.isEmpty(feedPhone)) {
            Toaster.showShort("内容或号码不能为空");
            return;
        }
        if(!LoginUtils.isPhoneNumber(feedPhone)){
            Toaster.showShort("请输入正确的手机号码");
            return;
        }

        UpdateUserRepo.feedBack(feedPhone, getHostIP(), String.valueOf(AppSetting.getVersionCode()), mApiVersion, feedContent)
                .compose(this.<FeedBackBean>bindToLifecycle())
                .subscribeOn(Schedulers.newThread())
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObserver<FeedBackBean>() {
                    @Override
                    public void onDataSuccess(FeedBackBean feedBackBean) {
                        if (feedBackBean.data.result) {
                            Toaster.showShort("反馈成功");
                        } else {
                            Toaster.showShort("网络异常");
                        }
                    }
                });
    }


    /**
     * 获取ip地址
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return hostIp;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
