package com.youloft.lilith.common.widgets.picker;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youloft.lilith.R;
import com.youloft.lilith.common.database.CityInfoManager;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.common.widgets.dialog.PickerBaseDialog;
import com.youloft.lilith.common.widgets.picker.wheel.OnWheelChangedListener;
import com.youloft.lilith.common.widgets.picker.wheel.WheelView;
import com.youloft.lilith.common.widgets.picker.wheel.adapters.ArrayWheelAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Desc: 省市区三级选择,带经纬度
 * Change:城市选择弹窗，如果没有特殊需求，请直接调用{@link #getDefCityPicker(Context)} (Context)},
 * 然后通过接口来获得选择结果{@link #setOnCityItemClickListener(OnPickerSelectListener)}}
 * 最后必须要调用show()才会弹出弹窗;
 *
 * @author zchao created at 2017/7/13 14:13
 * @see
 */
public class CityPickerPop extends PickerBaseDialog implements CanShow, OnWheelChangedListener {

    private Context context;

    private WheelView mViewProvince;

    private WheelView mViewCity;

    private WheelView mViewDistrict;

    private RelativeLayout mRelativeTitleBg;

    private View mTvOK;

    private TextView mTvTitle;

    private View mTvCancel;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key 区 values经纬度
     */
    protected Map<String, String[]> mLongAndLati = new HashMap<String, String[]>();

    protected CityInfo mCurrentCityInfo = new CityInfo();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";


    private OnPickerSelectListener<CityInfo> listener;


    public CityPickerPop setOnCityItemClickListener(OnPickerSelectListener<CityInfo> listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Default text color
     */
    public static final int DEFAULT_TEXT_COLOR = 0xFF585858;

    /**
     * Default text size
     */
    public static final int DEFAULT_TEXT_SIZE = 18;

    // Text settings
    private int textColor = DEFAULT_TEXT_COLOR;

    private int textSize = DEFAULT_TEXT_SIZE;

    /**
     * 滚轮显示的item个数
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Count of visible items
    private int visibleItems = DEF_VISIBLE_ITEMS;

    /**
     * 省滚轮是否循环滚动
     */
    private boolean isProvinceCyclic = true;

    /**
     * 市滚轮是否循环滚动
     */
    private boolean isCityCyclic = true;

    /**
     * 区滚轮是否循环滚动
     */
    private boolean isDistrictCyclic = true;

    /**
     * item间距
     */
    private int padding = (int) ViewUtil.dp2px(18);


    /**
     * Color.BLACK
     */
    private String cancelTextColorStr = "#000000";


    /**
     * Color.BLUE
     */
    private String confirmTextColorStr = "#0000FF";

    /**
     * 标题背景颜色
     */
    private String titleBackgroundColorStr = "#E9E9E9";
    /**
     * 标题颜色
     */
    private String titleTextColorStr = "#E9E9E9";

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "北京市";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "北京市";

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrict = "东城区";

    /**
     * 两级联动
     */
    private boolean showProvinceAndCity = false;

    /**
     * 标题
     */
    private String mTitle = "选择地区";

    /**
     * 获取默认设置的城市选择器；如果需要自定义使用Builder来构建
     *
     * @param context
     * @return
     */
    public static CityPickerPop getDefCityPicker(Context context) {
        return new CityPickerPop(context).textSize(20)
                .backgroundPop(Color.TRANSPARENT)
                .textColor(Color.parseColor("#ffffff"))
                .textSize(16)
                .provinceCyclic(false)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(5)
                .itemPadding(20);
    }

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     *
     * @param defaultProvinceName
     * @return
     */
    public CityPickerPop province(String defaultProvinceName) {
        this.defaultProvinceName = defaultProvinceName;
        return this;
    }

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     *
     * @param defaultCityName
     * @return
     */
    public CityPickerPop city(String defaultCityName) {
        this.defaultCityName = defaultCityName;
        return this;
    }

    /**
     * 第一次默认地区显示，一般配合定位，使用
     *
     * @param defaultDistrict
     * @return
     */
    public CityPickerPop district(String defaultDistrict) {
        this.defaultDistrict = defaultDistrict;
        return this;
    }

    /**
     * 设置popwindow的背景
     */
    private int backgroundPop = 0xa0000000;

    private CityPickerPop(Context context) {
        super(context);
        setContentView(R.layout.pop_citypicker);
        this.context = context;
        this.mCurrentCityInfo.pProvice = defaultProvinceName;
        this.mCurrentCityInfo.pCity = defaultCityName;
        this.mCurrentCityInfo.pDistrict = defaultDistrict;

        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        mRelativeTitleBg = (RelativeLayout) findViewById(R.id.rl_title);
        mTvOK = findViewById(R.id.tv_confirm);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvCancel = findViewById(R.id.tv_cancel);

        /**
         * 设置标题
         */
        if (!TextUtils.isEmpty(this.mTitle)) {
            mTvTitle.setText(this.mTitle);
        }


        //设置确认按钮文字颜色
        if (!TextUtils.isEmpty(this.titleTextColorStr)) {
            mTvTitle.setTextColor(Color.parseColor(this.titleTextColorStr));
        }


        //只显示省市两级联动
        if (this.showProvinceAndCity) {
            mViewDistrict.setVisibility(View.GONE);
        } else {
            mViewDistrict.setVisibility(View.VISIBLE);
        }

        //初始化城市数据
        initProvinceDatas(context);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        // 添加onclick事件
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCancel();
                }
                hide();
            }
        });
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showProvinceAndCity) {
                    if (listener != null) {
                        listener.onSelected(mCurrentCityInfo);
                    }
                } else {
                    if (listener != null) {
                        listener.onSelected(mCurrentCityInfo);
                    }
                }
                hide();
            }
        });

    }


    /**
     * 设置popwindow的背景
     *
     * @param backgroundPopColor
     * @return
     */
    public CityPickerPop backgroundPop(int backgroundPopColor) {
        this.backgroundPop = backgroundPopColor;
        return this;
    }

    /**
     * 设置标题背景颜色
     *
     * @param titleTextColorStr
     * @return
     */
    public CityPickerPop titleTextColor(String titleTextColorStr) {
        this.titleTextColorStr = titleTextColorStr;
        return this;
    }


    /**
     * 设置标题
     *
     * @param mtitle
     * @return
     */
    public CityPickerPop title(String mtitle) {
        this.mTitle = mtitle;
        return this;
    }

    /**
     * 是否只显示省市两级联动
     *
     * @param flag
     * @return
     */
    public CityPickerPop onlyShowProvinceAndCity(boolean flag) {
        this.showProvinceAndCity = flag;
        return this;
    }


    /**
     * 确认按钮文字颜色
     *
     * @param color
     * @return
     */
    public CityPickerPop confirTextColor(String color) {
        this.confirmTextColorStr = color;
        return this;
    }


    /**
     * 取消按钮文字颜色
     *
     * @param color
     * @return
     */
    public CityPickerPop cancelTextColor(String color) {
        this.cancelTextColorStr = color;
        return this;
    }

    /**
     * item文字颜色
     *
     * @param textColor
     * @return
     */
    public CityPickerPop textColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * item文字大小
     *
     * @param textSize
     * @return
     */
    public CityPickerPop textSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * 滚轮显示的item个数
     *
     * @param visibleItems
     * @return
     */
    public CityPickerPop visibleItemsCount(int visibleItems) {
        this.visibleItems = visibleItems;
        return this;
    }

    /**
     * 省滚轮是否循环滚动
     *
     * @param isProvinceCyclic
     * @return
     */
    public CityPickerPop provinceCyclic(boolean isProvinceCyclic) {
        this.isProvinceCyclic = isProvinceCyclic;
        return this;
    }

    /**
     * 市滚轮是否循环滚动
     *
     * @param isCityCyclic
     * @return
     */
    public CityPickerPop cityCyclic(boolean isCityCyclic) {
        this.isCityCyclic = isCityCyclic;
        return this;
    }

    /**
     * 区滚轮是否循环滚动
     *
     * @param isDistrictCyclic
     * @return
     */
    public CityPickerPop districtCyclic(boolean isDistrictCyclic) {
        this.isDistrictCyclic = isDistrictCyclic;
        return this;
    }

    /**
     * item间距
     *
     * @param itemPadding
     * @return
     */
    public CityPickerPop itemPadding(int itemPadding) {
        this.padding = itemPadding;
        return this;
    }

    private void setUpData() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceDatas.length > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter<String>(context, mProvinceDatas);
        mViewProvince.setViewAdapter(arrayWheelAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        // 设置可见条目数量
        mViewProvince.setVisibleItems(visibleItems);
        mViewCity.setVisibleItems(visibleItems);
        mViewDistrict.setVisibleItems(visibleItems);
        mViewProvince.setCyclic(isProvinceCyclic);
        mViewCity.setCyclic(isCityCyclic);
        mViewDistrict.setCyclic(isDistrictCyclic);
        arrayWheelAdapter.setPadding(padding);
        arrayWheelAdapter.setTextColor(textColor);
        arrayWheelAdapter.setTextSize(textSize);

        updateCities();
        updateAreas();
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(final Context context) {
        mProvinceDatas = CityInfoManager.getInstance(context).getProvinceDatas().clone();
        mCitisDatasMap.putAll(CityInfoManager.getInstance(context).getProvinceAndCityDates());
        mDistrictDatasMap.putAll(CityInfoManager.getInstance(context).getCityAndDistrictDate());
        mLongAndLati.putAll(CityInfoManager.getInstance(context).getDistrictLongAndLati());
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }

        int districtDefault = -1;
        if (!TextUtils.isEmpty(defaultDistrict) && areas.length > 0) {
            for (int i = 0; i < areas.length; i++) {
                if (areas[i].contains(defaultDistrict)) {
                    districtDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter districtWheel = new ArrayWheelAdapter<String>(context, areas);
        // 设置可见条目数量
        districtWheel.setTextColor(textColor);
        districtWheel.setTextSize(textSize);
        mViewDistrict.setViewAdapter(districtWheel);
        if (-1 != districtDefault) {
            mViewDistrict.setCurrentItem(districtDefault);
            //获取默认设置的区
            mCurrentDistrictName = defaultDistrict;
        } else {
            mViewDistrict.setCurrentItem(0);
            //获取第一个区名称
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName)[0];

        }
        mCurrentCityInfo.pCity = mCurrentCityName;
        mCurrentCityInfo.pDistrict = mCurrentDistrictName;
        mCurrentCityInfo.pLongitude = mLongAndLati.get(mCurrentCityName + mCurrentDistrictName)[0];
        mCurrentCityInfo.pLatitude = mLongAndLati.get(mCurrentCityName + mCurrentDistrictName)[1];
        districtWheel.setPadding(padding);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }

        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].contains(defaultCityName)) {
                    cityDefault = i;
                    break;
                }
            }
        }

        ArrayWheelAdapter cityWheel = new ArrayWheelAdapter<String>(context, cities);
        // 设置可见条目数量
        cityWheel.setTextColor(textColor);
        cityWheel.setTextSize(textSize);
        mViewCity.setViewAdapter(cityWheel);
        if (-1 != cityDefault) {
            mViewCity.setCurrentItem(cityDefault);
        } else {
            mViewCity.setCurrentItem(0);
        }

        mCurrentCityInfo.pProvice = mCurrentProviceName;
        cityWheel.setPadding(padding);
        updateAreas();
    }

    @Override
    public void setType(int type) {
    }

    @Override
    public void show() {
        setUpData();
        super.show();
    }

    @Override
    public void hide() {
        if (isShow()) {
            dismiss();
        }
    }

    @Override
    public boolean isShow() {
        return isShowing();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentProviceName + mCurrentCityName)[newValue];
            mCurrentCityInfo.pDistrict = mCurrentDistrictName;
            mCurrentCityInfo.pLongitude = mLongAndLati.get(mCurrentCityName + mCurrentDistrictName)[0];
            mCurrentCityInfo.pLatitude = mLongAndLati.get(mCurrentCityName + mCurrentDistrictName)[1];
        }
    }
}
