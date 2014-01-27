package com.chenjw.spider.aliuser.model;

import java.util.ArrayList;
import java.util.List;

import com.chenjw.tools.BeanCopyUtils;

public class AliUserInfo {
    // aliwayId
    private int    aliwayId;

    private String userId;
    // 中文名
    private String name;
    // 昵称
    private String nickName;
    // 花名
    private String tbName;
    // 旺旺-贸易通
    private String wwB2b;
    // 旺旺-淘宝
    private String wwTb;
    // 来往
    private String laiwang;
    // 新浪微博
    private String sinaWeibo;
    // 工号
    private int    workNum;
    // 性别
    private String sex;
    // 职位
    private String post;
    // 入职时间
    private String entryDate;
    // 生日
    private String birthDate;
    // 星座
    private String constellation;

    // 实线主管
    private String hrg;
    // 实线主管
    private String actualDirector;

    // 虚线主管
    private String virtualDirector;
    // alipay最后登录
    private String alipayLastLoginDate;
    // 分机
    private String internalTel;

    // 支付宝
    private String alipayAccount;
    // 邮箱
    private String email;
    // 部门
    private String department;
    // 手机
    private String mobile;
    // 地点
    private String location;
    // 工位
    private String workStation;
    // /
    // 英文名
    private String englishName;
    // 公司
    private String company;
    // 员工类型
    private String employeeType;
    // 雅虎通
    private String yahooNum;
    // msn
    private String msnNum;

    // 老家
    private String hometown;
    // 地址
    private String address;
    // 状态
    private String status;
    // 我的地盘
    private String myspace;

    // 想说的话
    private String saySomething;
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAliwayId() {
        return aliwayId;
    }

    public void setAliwayId(int aliwayId) {
        this.aliwayId = aliwayId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public String getWwB2b() {
        return wwB2b;
    }

    public void setWwB2b(String wwB2b) {
        this.wwB2b = wwB2b;
    }

    public String getWwTb() {
        return wwTb;
    }

    public void setWwTb(String wwTb) {
        this.wwTb = wwTb;
    }

    public int getWorkNum() {
        return workNum;
    }

    public void setWorkNum(int workNum) {
        this.workNum = workNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAlipayLastLoginDate() {
        return alipayLastLoginDate;
    }

    public void setAlipayLastLoginDate(String alipayLastLoginDate) {
        this.alipayLastLoginDate = alipayLastLoginDate;
    }

    public String getInternalTel() {
        return internalTel;
    }

    public void setInternalTel(String internalTel) {
        this.internalTel = internalTel;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getYahooNum() {
        return yahooNum;
    }

    public void setYahooNum(String yahooNum) {
        this.yahooNum = yahooNum;
    }

    public String getMsnNum() {
        return msnNum;
    }

    public void setMsnNum(String msnNum) {
        this.msnNum = msnNum;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLaiwang() {
        return laiwang;
    }

    public void setLaiwang(String laiwang) {
        this.laiwang = laiwang;
    }

    public String getSinaWeibo() {
        return sinaWeibo;
    }

    public void setSinaWeibo(String sinaWeibo) {
        this.sinaWeibo = sinaWeibo;
    }

    public String getActualDirector() {
        return actualDirector;
    }

    public void setActualDirector(String actualDirector) {
        this.actualDirector = actualDirector;
    }

    public String getVirtualDirector() {
        return virtualDirector;
    }

    public void setVirtualDirector(String virtualDirector) {
        this.virtualDirector = virtualDirector;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public String getMyspace() {
        return myspace;
    }

    public void setMyspace(String myspace) {
        this.myspace = myspace;
    }

    public String getSaySomething() {
        return saySomething;
    }

    public void setSaySomething(String saySomething) {
        this.saySomething = saySomething;
    }

    public String getHrg() {
        return hrg;
    }

    public void setHrg(String hrg) {
        this.hrg = hrg;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

}
