package com.jiushig.location.entity;

import java.io.Serializable;

/**
 * Created by zk on 2018/2/25.
 */

public class Location implements Serializable {
    public double longitude;
    public double latitude;
    public String country;
    public String province;
    public String city;
    public String poiName;
    public String details;

    @Override
    public String toString() {
        return poiName;
    }
}
