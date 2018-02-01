package newjohn.com.pigpen.bean;

import org.greenrobot.greendao.annotation.Entity;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PigpenHisData {
    String area;
    String deviceNum;
    String value;
    String dateTime;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
