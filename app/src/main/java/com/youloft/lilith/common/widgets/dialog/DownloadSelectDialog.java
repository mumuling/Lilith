package com.youloft.lilith.common.widgets.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Toaster;
import com.youloft.lilith.info.DownloadService;
import com.youloft.lilith.info.bean.CheckVersionBean;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 确定更新的弹窗
 * Created by gyh on 2017/7/11.
 */

public class DownloadSelectDialog extends BaseDialog {
    @BindView(R.id.tv_version)
    TextView tvVersion;  //版本号显示
    @BindView(R.id.rv_desc)
    RecyclerView rvDesc;  //版本描述信息
    private Activity mActivity;
    private CheckVersionBean checkVersionBean;

    public DownloadSelectDialog(@NonNull Context context) {
        super(context);
        mActivity = (Activity) context;
        initView();
    }

    public DownloadSelectDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public DownloadSelectDialog(Context context, CheckVersionBean checkVersionBean) {
        super(context);
        mActivity = (Activity) context;
        this.checkVersionBean = checkVersionBean;
        initView();
    }

    private void initView() {
        setContentView(R.layout.dialog_download_select);
        ButterKnife.bind(this);
        if (checkVersionBean != null && checkVersionBean.data != null) {
            tvVersion.setText("V" + checkVersionBean.data.version);
            List<String> data = initData();
            rvDesc.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            rvDesc.setAdapter(new MyAdapter(data));
        }
    }

    /**
     * 把content里面的数据拆开
     */
    private List<String> initData() {
        String contents = checkVersionBean.data.contents;
        String[] split = contents.trim().split("\\r\\n");
        return Arrays.asList(split);
    }

    @OnClick({R.id.iv_close, R.id.tv_update, R.id.fl_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update:
                Intent intent = new Intent(mActivity, DownloadService.class);
                intent.putExtra("url", checkVersionBean.data.downPath);
                mActivity.startService(intent);
                Toaster.showShort("后台下载中");
                dismiss();
                break;
            case R.id.fl_root:
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyHolder> {
        private List<String> mdata;

        public MyAdapter(List<String> data) {
            mdata = data;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_version_desc, parent, false));
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            if (mdata != null && mdata.size() != 0) {
                holder.tv.setText(mdata.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (mdata == null || mdata.size() == 0) {
                return 0;
            } else {
                return mdata.size();
            }
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_desc);
        }
    }
}
