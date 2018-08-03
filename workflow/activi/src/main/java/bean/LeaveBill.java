package bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : Mingyue.Zhan
 * @version : 1.0
 * @date : 2018/7/27
 * @since : 1.5
 */

/**
 * 这是请假单实例
 */
public class LeaveBill implements Serializable {
    public LeaveBill() {
    }
    
    @Override
    public String toString() {
        return "LeaveBill{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", dept='" + dept + '\'' +
                ", leavetype='" + leavetype + '\'' +
                ", leavereason='" + leavereason + '\'' +
                ", leavestarttime=" + leavestarttime +
                ", leaveendtime=" + leaveendtime +
                ", day=" + day +
                ", hours=" + hours +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getLeavetype() {
        return leavetype;
    }

    public void setLeavetype(String leavetype) {
        this.leavetype = leavetype;
    }

    public String getLeavereason() {
        return leavereason;
    }

    public void setLeavereason(String leavereason) {
        this.leavereason = leavereason;
    }

    public Date getLeavestarttime() {
        return leavestarttime;
    }

    public void setLeavestarttime(Date leavestarttime) {
        this.leavestarttime = leavestarttime;
    }

    public Date getLeaveendtime() {
        return leaveendtime;
    }

    public void setLeaveendtime(Date leaveendtime) {
        this.leaveendtime = leaveendtime;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public LeaveBill(Integer id, String name, String position, String dept, String leavetype, String leavereason, Date leavestarttime, Date leaveendtime, int day, int hours) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.dept = dept;
        this.leavetype = leavetype;
        this.leavereason = leavereason;
        this.leavestarttime = leavestarttime;
        this.leaveendtime = leaveendtime;
        this.day = day;
        this.hours = hours;
    }

    private Integer id;   //请假单编号
    private String name;  //请假人姓名
    private String position;//请假人职位
    private String dept;//请假人所在部门
    private String leavetype;//请假类型
    private String leavereason;//请假原因
    private Date leavestarttime;//请假的开始时间
    private Date leaveendtime;//请假的结束时间
    private int day;//请假的天数
    private int hours;//请假的小时数
}
