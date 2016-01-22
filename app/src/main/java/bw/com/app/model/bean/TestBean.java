package bw.com.app.model.bean;

/**
 * Created by zhaoyechao on 2016/1/20.
 */
public class TestBean {

    public String title;
    public String desc;
    public String phone;
    public String time;

    public TestBean() {
    }

    public TestBean(String title, String desc, String phone, String time) {
        this.title = title;
        this.desc = desc;
        this.phone = phone;
        this.time = time;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", phone='" + phone + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
