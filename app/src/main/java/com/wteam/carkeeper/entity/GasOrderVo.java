package com.wteam.carkeeper.entity;

import java.io.Serializable;

public class GasOrderVo implements Serializable {
	/**
	 * 主键
	 */
	private String gasOrderId;
	/**
	 * 订单编号
	 */
	private String orderNum;
	/**
	 * 订单创建时间
	 */
	private String createTime;
	/**
	 * 预约时间
	 */
	private String reserveTime;
	/**
	 * 加油站名称
	 */
	private String gasStationName;
	/**
	 * 汽油类型
	 */
	private String gasType;
	/**
	 * 预约加油的数量
	 */
	private String amountOfGasoline;
	/**
	 * 订单状态
	 */
	private String statu;

	public String getGasOrderId() {
		return gasOrderId;
	}

	public void setGasOrderId(String gasOrderId) {
		this.gasOrderId = gasOrderId;
	}

	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getReserveTime() {
		return reserveTime;
	}
	public void setReserveTime(String reserveTime) {
		this.reserveTime = reserveTime;
	}
	public String getGasStationName() {
		return gasStationName;
	}
	public void setGasStationName(String gasStationName) {
		this.gasStationName = gasStationName;
	}
	public String getGasType() {
		return gasType;
	}
	public void setGasType(String gasType) {
		this.gasType = gasType;
	}
	public String getAmountOfGasoline() {
		return amountOfGasoline;
	}
	public void setAmountOfGasoline(String amountOfGasoline) {
		this.amountOfGasoline = amountOfGasoline;
	}
	public String getStatu() {
		return statu;
	}
	public void setStatu(String statu) {
		this.statu = statu;
	}
}
