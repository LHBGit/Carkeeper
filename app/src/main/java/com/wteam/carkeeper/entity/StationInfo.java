package com.wteam.carkeeper.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lhb on 2016/6/2.
 */
public class StationInfo {
    private String resultcode;
    private String reason;
    private ResultBean result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {

        private PageinfoBean pageinfo;
        private List<DataBean> data;

        public PageinfoBean getPageinfo() {
            return pageinfo;
        }

        public void setPageinfo(PageinfoBean pageinfo) {
            this.pageinfo = pageinfo;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class PageinfoBean {
            private int pnums;
            private String current;
            private int allpage;

            public int getPnums() {
                return pnums;
            }

            public void setPnums(int pnums) {
                this.pnums = pnums;
            }

            public String getCurrent() {
                return current;
            }

            public void setCurrent(String current) {
                this.current = current;
            }

            public int getAllpage() {
                return allpage;
            }

            public void setAllpage(int allpage) {
                this.allpage = allpage;
            }
        }

        public static class DataBean implements Serializable{
            private String id;
            private String name;
            private String area;
            private String areaname;
            private String address;
            private String brandname;
            private String type;
            private String discount;
            private String exhaust;
            private String position;
            private String lon;
            private String lat;
            private String fwlsmc;
            private int distance;

            private String price;
            private String gastprice;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getAreaname() {
                return areaname;
            }

            public void setAreaname(String areaname) {
                this.areaname = areaname;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getBrandname() {
                return brandname;
            }

            public void setBrandname(String brandname) {
                this.brandname = brandname;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public String getExhaust() {
                return exhaust;
            }

            public void setExhaust(String exhaust) {
                this.exhaust = exhaust;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getFwlsmc() {
                return fwlsmc;
            }

            public void setFwlsmc(String fwlsmc) {
                this.fwlsmc = fwlsmc;
            }

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }



            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getGastprice() {
                return gastprice;
            }

            public void setGastprice(String gastprice) {
                this.gastprice = gastprice;
            }
        }
    }
}

