package com.bcinfo.tripaway.bean;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 我的旅程，订单
 * 
 * @function
 * @author JiangCS
 * @version 1.0, 2015年5月29日 下午4:24:09
 */
public class MyOrder implements Parcelable
{
    private String id;
    
    private String refundPolicy;
    
    private String createTime;
    
    private String remark;
    
    private String price;
    
    private String status;
    
    private String no;
    
    private String endDate;
    
    private String beginDate;
    
    private String leaveword;
    
    private String statements;
    
    private String totalPrice;
    
    private String amount;
    
    private String refundButton;
    
    private ArrayList<DispatchInfo> dispatchInfos = new ArrayList<>();
    
    public ArrayList<DispatchInfo> getDispatchInfos() {
		return dispatchInfos;
	}

	public void setDispatchInfos(ArrayList<DispatchInfo> dispatchInfos) {
		this.dispatchInfos = dispatchInfos;
	}

	private String staffButton;
    /**
     * 联系方式
     */
    private String tel;
    /**
     * 联系人
     */
    private String contact_name;
    /**
     * 紧急联系人号码
     */
    private String urgentContactNum;
    private String discountPrice;
    private String couponCode;
    private String couponReducePrice;
//    分期
    private String is_instal;
    private String sfy_sf_price;
    private String period_num;
    
    public String getIs_instal() {
		return is_instal;
	}

	public void setIs_instal(String is_instal) {
		this.is_instal = is_instal;
	}

	public String getSfy_sf_price() {
		return sfy_sf_price;
	}

	public void setSfy_sf_price(String sfy_sf_price) {
		this.sfy_sf_price = sfy_sf_price;
	}

	public String getPeriod_num() {
		return period_num;
	}

	public void setPeriod_num(String period_num) {
		this.period_num = period_num;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getUrgentContactNum() {
		return urgentContactNum;
	}

	public void setUrgentContactNum(String urgentContactNum) {
		this.urgentContactNum = urgentContactNum;
	}

	public String getUrgentContactPerson() {
		return urgentContactPerson;
	}

	public void setUrgentContactPerson(String urgentContactPerson) {
		this.urgentContactPerson = urgentContactPerson;
	}

	/**
     * 紧急联系人
     */
    private String urgentContactPerson;
    
    private ProductNewInfo product = new ProductNewInfo();
    
    private List<ServiceInfo> serviceInfoList = new ArrayList<ServiceInfo>();
    
    private UserInfo receiverInfo = new UserInfo();
    
    private List<PartnerInfo> partnerInfoList = new ArrayList<PartnerInfo>();
    
    private List<ServResrouce> servResrouces = new ArrayList<ServResrouce>();
    
    private List<TraceInfo> traceInfoList = new ArrayList<TraceInfo>();
    
    /**
     * 评价按钮
     */
    private String appraiseBtn;
    
    /**
     * 删除按钮
     */
    private String deleteBtn;
    
    private String cancelBtn;
    
    private String finalPayButton;
    
    private String preferentialPrice;
    
    private String deposit;
    
    private String finalPayment;
    
    private String unit;
    
    private Invoice invoice;
    
    private String originalPrice;
    
    public String getPreferentialPrice() {
		return preferentialPrice;
	}

	public void setPreferentialPrice(String preferentialPrice) {
		this.preferentialPrice = preferentialPrice;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getFinalPayment() {
		return finalPayment;
	}

	public void setFinalPayment(String finalPayment) {
		this.finalPayment = finalPayment;
	}

	public String getAppraiseBtn() {
		return appraiseBtn;
	}

	public void setAppraiseBtn(String appraiseBtn) {
		this.appraiseBtn = appraiseBtn;
	}

	public String getDeleteBtn() {
		return deleteBtn;
	}

	public void setDeleteBtn(String deleteBtn) {
		this.deleteBtn = deleteBtn;
	}

	public MyOrder()
    {
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getRefundPolicy()
    {
        return refundPolicy;
    }
    
    public void setRefundPolicy(String refundPolicy)
    {
        this.refundPolicy = refundPolicy;
    }
    
    public String getPrice()
    {
        return price;
    }
    
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getNo()
    {
        return no;
    }
    
    public void setNo(String no)
    {
        this.no = no;
    }
    
    public String getEndDate()
    {
        return endDate;
    }
    
    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
    
    public String getBeginDate()
    {
        return beginDate;
    }
    
    public void setBeginDate(String beginDate)
    {
        this.beginDate = beginDate;
    }
    
    public ProductNewInfo getProduct()
    {
        return product;
    }
    
    public void setProduct(ProductNewInfo product)
    {
        this.product = product;
    }
    
    public String getLeaveword()
    {
        return leaveword;
    }
    
    public void setLeaveword(String leaveword)
    {
        this.leaveword = leaveword;
    }
    
    public String getStatements()
    {
        return statements;
    }
    
    public void setStatements(String statements)
    {
        this.statements = statements;
    }
    
    public UserInfo getReceiverInfo()
    {
        return receiverInfo;
    }
    
    public void setReceiverInfo(UserInfo receiverInfo)
    {
        this.receiverInfo = receiverInfo;
    }
    
    public List<ServiceInfo> getServiceInfoList()
    {
        return serviceInfoList;
    }
    
    public void setServiceInfoList(List<ServiceInfo> serviceInfoList)
    {
        this.serviceInfoList = serviceInfoList;
    }
    
    public List<PartnerInfo> getPartnerInfoList()
    {
        return partnerInfoList;
    }
    
    public void setPartnerInfoList(List<PartnerInfo> partnerInfoList)
    {
        this.partnerInfoList = partnerInfoList;
    }
    
    public String getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getTotalPrice()
    {
        return totalPrice;
    }
    
    public void setTotalPrice(String totalPrice)
    {
        this.totalPrice = totalPrice;
    }
    
    public List<ServResrouce> getServResrouces()
    {
        return servResrouces;
    }
    
    public void setServResrouces(List<ServResrouce> servResrouces)
    {
        this.servResrouces = servResrouces;
    }
    
    public String getAmount()
    {
        return amount;
    }
    
    public void setAmount(String amount)
    {
        this.amount = amount;
    }
    
    public String getRefundButton()
    {
        return refundButton;
    }
    
    public void setRefundButton(String refundButton)
    {
        this.refundButton = refundButton;
    }
    
    public String getStaffButton()
    {
        return staffButton;
    }
    
    public void setStaffButton(String staffButton)
    {
        this.staffButton = staffButton;
    }
    
    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public static final Parcelable.Creator<MyOrder> CREATOR = new Parcelable.Creator<MyOrder>()
    {
        public MyOrder createFromParcel(Parcel source)
        {
            return new MyOrder(source);
        }
        
        public MyOrder[] newArray(int size)
        {
            return new MyOrder[size];
        }
    };
    
    public MyOrder(Parcel in)
    {
        id = in.readString();
        refundPolicy = in.readString();
        price = in.readString();
        status = in.readString();
        no = in.readString();
        endDate = in.readString();
        beginDate = in.readString();
        leaveword = in.readString();
        statements = in.readString();
        refundButton = in.readString();
        staffButton = in.readString();
        product = in.readParcelable(ProductNewInfo.class.getClassLoader());
        in.readTypedList(serviceInfoList, ServiceInfo.CREATOR);
        receiverInfo = in.readParcelable(UserInfo.class.getClassLoader());
        in.readTypedList(partnerInfoList, PartnerInfo.CREATOR);
        in.readTypedList(servResrouces, ServResrouce.CREATOR);
        createTime = in.readString();
        remark = in.readString();
        totalPrice = in.readString();
        amount = in.readString();
        appraiseBtn = in.readString();
        deleteBtn = in.readString();
        in.readTypedList(traceInfoList,TraceInfo.CREATOR);
        in.readTypedList(dispatchInfos,DispatchInfo.CREATOR);
        preferentialPrice = in.readString();
        deposit = in.readString();
        finalPayment = in.readString();
        unit = in.readString();
        invoice = in.readParcelable(Invoice.class.getClassLoader());
        cancelBtn = in.readString();
        finalPayButton = in.readString();
        tel = in.readString();
        urgentContactPerson = in.readString();
        urgentContactNum = in.readString();
        contact_name = in.readString();
        originalPrice = in.readString();
        couponReducePrice = in.readString();
        couponCode = in.readString();
        couponReducePrice = in.readString();
        is_instal = in.readString();
        sfy_sf_price = in.readString();
        period_num = in.readString();
    }
    
    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        // TODO Auto-generated method stub
        out.writeString(id);
        out.writeString(refundPolicy);
        out.writeString(price);
        out.writeString(status);
        out.writeString(no);
        out.writeString(endDate);
        out.writeString(beginDate);
        out.writeString(leaveword);
        out.writeString(statements);
        out.writeString(refundButton);
        out.writeString(staffButton);
        out.writeParcelable(product, 0);
        out.writeTypedList(serviceInfoList);
        out.writeParcelable(receiverInfo, 0);
        out.writeTypedList(partnerInfoList);
        out.writeTypedList(servResrouces);
        out.writeString(createTime);
        out.writeString(remark);
        out.writeString(totalPrice);
        out.writeString(amount);
        out.writeString(appraiseBtn);
        out.writeString(deleteBtn);
        out.writeTypedList(traceInfoList);
        out.writeTypedList(dispatchInfos);
        out.writeString(preferentialPrice);
        out.writeString(deposit);
        out.writeString(finalPayment);
        out.writeString(unit);
        out.writeParcelable(invoice, 0);
        out.writeString(cancelBtn);
        out.writeString(finalPayButton);
        out.writeString(tel);
        out.writeString(urgentContactPerson);
        out.writeString(urgentContactNum);
        out.writeString(contact_name);
        out.writeString(originalPrice);
        out.writeString(couponReducePrice);
        out.writeString(couponCode);
        out.writeString(discountPrice);
        out.writeString(is_instal);
        out.writeString(sfy_sf_price);
        out.writeString(period_num);
    }

	/**
	 * @return the traceInfoList
	 */
	public List<TraceInfo> getTraceInfoList() {
		return traceInfoList;
	}

	/**
	 * @param traceInfoList the traceInfoList to set
	 */
	public void setTraceInfoList(List<TraceInfo> traceInfoList) {
		this.traceInfoList = traceInfoList;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the invoice
	 */
	public Invoice getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice to set
	 */
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the cancelBtn
	 */
	public String getCancelBtn() {
		return cancelBtn;
	}

	/**
	 * @param cancelBtn the cancelBtn to set
	 */
	public void setCancelBtn(String cancelBtn) {
		this.cancelBtn = cancelBtn;
	}

	/**
	 * @return the finalPayButton
	 */
	public String getFinalPayButton() {
		return finalPayButton;
	}

	/**
	 * @param finalPayButton the finalPayButton to set
	 */
	public void setFinalPayButton(String finalPayButton) {
		this.finalPayButton = finalPayButton;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the originalPrice
	 */
	public String getOriginalPrice() {
		return originalPrice;
	}

	/**
	 * @param originalPrice the originalPrice to set
	 */
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getCouponCode() {
		return couponCode;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponReducePrice() {
		return couponReducePrice;
	}

	public void setCouponReducePrice(String couponReducePrice) {
		this.couponReducePrice = couponReducePrice;
	}
	
}
