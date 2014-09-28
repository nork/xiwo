package com.android.xiwao.washcar.httpconnection;

public abstract class CommandFactory {
	public abstract BaseCommand getLogin(String user, String password);
	public abstract BaseCommand getRegister(String codestr, String phonenumber, String password, String nickNameStr);
	public abstract BaseCommand getCode(String phone, String operate);
	public abstract BaseCommand getCarQuery(long customId);
	public abstract BaseCommand getCarRegister(String carCode, String carBrand, String carColor, String carType, String carPic, long customerId);
	public abstract BaseCommand getCarModify(long carId, String carCode, String carBrand, String carColor, String carType, String carPic, long customerId);
	public abstract BaseCommand getPasswordReset(String validateCode, String mobile, String password);
	public abstract BaseCommand getPasswordModify(long id, String oldpwd, String newpwd);
	public abstract BaseCommand getAccountQuery(long id);
	public abstract BaseCommand getAccountRecharge(long id, long amt);
	public abstract BaseCommand getAddressQuery(long customerId);
	public abstract BaseCommand getDistractQuery();
	public abstract BaseCommand getAddressCreate(long distractId, String detailAddress, long customerId);
	public abstract BaseCommand getPlaceOrder(long customerId, String serviceType, String mobileNum, long carId, long distractId
			, long addressId, String payType, String note, String address, String serviceTypeMi, int fee);
	public abstract BaseCommand getOrderQuery(long custmerId, String orderState, int startIndex, int page);
	public abstract BaseCommand getActivityQuery(long customerId);
	public abstract BaseCommand getAccountConsume(long customerId, int changeAmt, long orderId);
	public abstract BaseCommand getActivityConsume(long customerId, long orderId);
	public abstract BaseCommand getCustomerModify(long customerId, String customerName, String email, String headImg);
	public abstract BaseCommand getCarDelete(long carId);
	public abstract BaseCommand getAddressDelete(long addressId);
	public abstract BaseCommand getRateQuery();
	public abstract BaseCommand updateOrderStateCancel(int orderId);
	public abstract BaseCommand getVIPInfoQuery(long customerId);
}
