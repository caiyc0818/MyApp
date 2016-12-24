package com.bcinfo.tripaway.enums;

public enum OrderStatusEnum {

	APPLY_SUCCESS("apply_success","订单成功"),
	DEPOSIT_SUCCESS("deposit_success","预订成功-待支付尾款"),
	BOOK_SUCCESS("book_success","完成支付"),
	ORDER_ACCEPTED("order_accepted","已受理，正在派单"),
	REFUND_APPLY("refund_apply","退订申请"),
	REFUND_RUNNING("refund_running","退订中"),
	REFUND_PROCESS("refund_process","退订正在处理中"),
	REFUND_SUCCESS("refund_success","退订成功"),
	SERVICE_RUNNING("service_running","行程进行中"),
	BOOK_EXPIRE("book_expire","订单过期"),
	APPLY_CANCEL("apply_cancel","订单已取消"),
	SERVICE_END("service_end","行程完成");
	
	private String status;
	
	private String statusDesc;
	
	private OrderStatusEnum(String status,String statusDesc){
		this.status = status;
		this.statusDesc = statusDesc;
	}
	
	public static String  getFromStatus(String status){
		for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
			if(status.equals(orderStatusEnum.getStatus())){
				return orderStatusEnum.getStatusDesc();
			}
		}
		return "";
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc() {
		return statusDesc;
	}
	/**
	 * @param statusDesc the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

}
