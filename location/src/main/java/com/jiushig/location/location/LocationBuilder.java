package com.jiushig.location.location;

import android.content.Context;

/**
 * Created by zk on 2018/2/26.
 */

public class LocationBuilder {

    public static String POI = "餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";

    private Context context;

    private TYPE type = TYPE.GAO_DE;
    private LocationBase.CallbackSuccess callbackSuccess;
    private LocationBase.CallbackFail callbackFail;
    // 最大定位错误次数
    private int maxErrorNum = 5;

    public enum TYPE {
        GAO_DE, BAI_DU
    }

    public LocationBuilder(Context context) {
        this.context = context;
    }

    public LocationBuilder setType(TYPE type) {
        this.type = type;
        return this;
    }

    public LocationBuilder setCallbackSuccess(LocationBase.CallbackSuccess callbackSuccess) {
        this.callbackSuccess = callbackSuccess;
        return this;
    }

    public LocationBuilder setCallbackFail(LocationBase.CallbackFail callbackFail) {
        this.callbackFail = callbackFail;
        return this;
    }

    public LocationBuilder setmaxErrorNum(int maxErrorNum) {
        this.maxErrorNum = maxErrorNum;
        return this;
    }

    /**
     * 开始执行
     */
    public void start() {
        switch (type) {
            case GAO_DE:
                new LocationGaode(context)
                        .setCallbackSuccess(callbackSuccess)
                        .setCallbackFail(callbackFail)
                        .setMaxErrorNum(maxErrorNum)
                        .start();
                break;
        }
    }

}
