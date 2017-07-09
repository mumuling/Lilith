package com.youloft.lilith.common.widgets.picker;

/**
 * Created by zchao on 2017/7/9.
 * desc:
 * version:
 */

public class CityInfo {
    public CityInfo() {
    }

    public CityInfo(String pProvice, String pCity, String pDistrict, String pLongitude, String pLatitude) {
        this.pProvice = pProvice;
        this.pCity = pCity;
        this.pDistrict = pDistrict;
        this.pLongitude = pLongitude;
        this.pLatitude = pLatitude;
    }

    public String pProvice;     //省
    public String pCity;        //市
    public String pDistrict;    //县
    public String pLongitude;   //经度
    public String pLatitude;    //纬度
}
