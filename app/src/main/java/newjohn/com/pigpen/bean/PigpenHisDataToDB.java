package newjohn.com.pigpen.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/1/22.
 */
@Entity
public class PigpenHisDataToDB {
    @Id(autoincrement = true)
    Long id;
    String area;
    String deviceNum;
    String value;
    String dateTime;

@Generated(hash = 1657125920)
public PigpenHisDataToDB(Long id, String area, String deviceNum, String value, String dateTime) {
    this.id = id;
    this.area = area;
    this.deviceNum = deviceNum;
    this.value = value;
    this.dateTime = dateTime;
}

@Generated(hash = 907854227)
public PigpenHisDataToDB() {
}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
