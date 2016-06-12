package com.wteam.carkeeper.entity;

import java.io.Serializable;

public class CarInfoVo implements Serializable{
	/**
	 * 车辆图片路径
	 */
	private String carImageUrl;
	/**
	 * 汽车型号
	 */
	private String carType;
	/**
	 * 车牌号码
	 */
	private String carNum;
	/**
	 * 发动机号
	 */
	private String engineNum;
	/**
	 * 车门数量
	 */
	private Integer numOfDoor;
	/**
	 * 座位数量
	 */
	private Integer numOfSeat;
	/**
	 * 里程数
	 */
	private Float mileage;
	/**
	 * 汽车邮箱总量
	 */
	private Float totalAmountOfGasoline;
	/**
	 * 当前油量
	 */
	private Float currentAmountOfGasoline;
	/**
	 * 发动机性能(好、异常)
	 */
	private Boolean enginePerformance;
	/**
	 * 变速器性能(好、异常)
	 */
	private Boolean transmissionPerformance;
	/**
	 * 车灯状态(好、异常)
	 */
	private Boolean headlightStatu;
	/**
	 * 车辆类型Id
	 */
	private Integer carTypeId;
	/**
	 * 汽车标志
	 */
	private String carLogo;
	/**
	 * 汽车品牌
	 */
	private String carBrand;
	
	
	public String getCarImageUrl() {
		return carImageUrl;
	}
	public void setCarImageUrl(String carImageUrl) {
		this.carImageUrl = carImageUrl;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getEngineNum() {
		return engineNum;
	}
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}
	public Integer getNumOfDoor() {
		return numOfDoor;
	}
	public void setNumOfDoor(Integer numOfDoor) {
		this.numOfDoor = numOfDoor;
	}
	public Integer getNumOfSeat() {
		return numOfSeat;
	}
	public void setNumOfSeat(Integer numOfSeat) {
		this.numOfSeat = numOfSeat;
	}
	public Float getMileage() {
		return mileage;
	}
	public void setMileage(Float mileage) {
		this.mileage = mileage;
	}
	public Float getTotalAmountOfGasoline() {
		return totalAmountOfGasoline;
	}
	public void setTotalAmountOfGasoline(Float totalAmountOfGasoline) {
		this.totalAmountOfGasoline = totalAmountOfGasoline;
	}
	public Float getCurrentAmountOfGasoline() {
		return currentAmountOfGasoline;
	}
	public void setCurrentAmountOfGasoline(Float currentAmountOfGasoline) {
		this.currentAmountOfGasoline = currentAmountOfGasoline;
	}
	public Boolean getEnginePerformance() {
		return enginePerformance;
	}
	public void setEnginePerformance(Boolean enginePerformance) {
		this.enginePerformance = enginePerformance;
	}
	public Boolean getTransmissionPerformance() {
		return transmissionPerformance;
	}
	public void setTransmissionPerformance(Boolean transmissionPerformance) {
		this.transmissionPerformance = transmissionPerformance;
	}
	public Boolean getHeadlightStatu() {
		return headlightStatu;
	}
	public void setHeadlightStatu(Boolean headlightStatu) {
		this.headlightStatu = headlightStatu;
	}
	public Integer getCarTypeId() {
		return carTypeId;
	}
	public void setCarTypeId(Integer carTypeId) {
		this.carTypeId = carTypeId;
	}
	public String getCarLogo() {
		return carLogo;
	}
	public void setCarLogo(String carLogo) {
		this.carLogo = carLogo;
	}
	public String getCarBrand() {
		return carBrand;
	}
	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
}
