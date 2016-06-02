package com.wteam.carkeeper.entity;

import java.util.Map;

/**
 * <p>Title:ResultMessage </p>
 * <p>Description:统一返回结果信息类 </p>
 * <p>Company:Wteam </p> 
 *  @author Wteam 李焕滨 86571705@qq.com
 *  @date 2016年4月12日 下午9:27:40
 */
public class ResultMessage {

	/**
     * 服务类执行状态码.
     */
    private String code;
    /**
     * 返回结果信息.
     */
    private String resultInfo;
    /** 
     * 请求返回值
     */
    private Map<String, Object> resultParm;
    
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
	}
	public Map<String, Object> getResultParm() {
		return resultParm;
	}
	public void setResultParm(Map<String, Object> resultParm) {
		this.resultParm = resultParm;
	}
}
