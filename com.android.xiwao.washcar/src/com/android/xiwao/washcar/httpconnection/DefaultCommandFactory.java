package com.android.xiwao.washcar.httpconnection;

public class DefaultCommandFactory extends CommandFactory {
	
	public BaseCommand getLogin(String user, String password){
		Login login = new Login();
		login.setUser(user);
		login.setPasswd(password);
		
		return login;
	}

	@Override
	public BaseCommand getRegister(String codestr, String phonenumber,
			String password, String nickNameStr) {
		// TODO Auto-generated method stub
		Register register = new Register();
		register.setCode(codestr);
		register.setPassword(password);
		register.setPhone(phonenumber);
		register.setNickName(nickNameStr);
		return register;
	}

	@Override
	public BaseCommand getCode(String phone, String operate) {
		// TODO Auto-generated method stub
		GetCode getCode = new GetCode();
		getCode.setMobileNum(phone);
		getCode.setMessageType("01");
		getCode.setMessageContent("01");
		return getCode;
	}

	@Override
	public BaseCommand getCarQuery(long customId) {
		// TODO Auto-generated method stub
		CarQuery carQuery = new CarQuery();
		carQuery.setCustomerId(customId);
		return carQuery;
	}

	@Override
	public BaseCommand getPasswordReset(String validateCode, String mobile,
			String password) {
		// TODO Auto-generated method stub
		PasswordReset passwordReset = new PasswordReset();
		passwordReset.setCode(validateCode);
		passwordReset.setPassword(password);
		passwordReset.setPhone(mobile);
		return passwordReset;
	}

	@Override
	public BaseCommand getPasswordModify(long id, String oldpwd, String newpwd) {
		// TODO Auto-generated method stub
		PasswordModify passwordModify = new PasswordModify();
		passwordModify.setCustomerId(id);
		passwordModify.setOldPwd(oldpwd);
		passwordModify.setNewPwd(newpwd);
		return passwordModify;
	}

	@Override
	public BaseCommand getCarRegister(String carCode, String carBrand,
			String carColor, String carType, String carPic, long customerId) {
		// TODO Auto-generated method stub
		CarRegister carRegister = new CarRegister();
		carRegister.setCarBrand(carBrand);
		carRegister.setCarCode(carCode);
		carRegister.setCarColor(carColor);
		carRegister.setCarPic(carPic);
		carRegister.setCarType(carType);
		carRegister.setCustomerId(customerId);
		return carRegister;
	}

	@Override
	public BaseCommand getCarModify(long carId, String carCode,
			String carBrand, String carColor, String carType, String carPic,
			long customerId) {
		// TODO Auto-generated method stub
		CarModify carModify = new CarModify();
		carModify.setCarBrand(carBrand);
		carModify.setCarCode(carCode);
		carModify.setCarColor(carColor);
		carModify.setCarId(carId);
		carModify.setCarPic(carPic);
		carModify.setCarType(carType);
		carModify.setCustomerId(customerId);
		return carModify;
	}

	@Override
	public BaseCommand getAccountQuery(long id) {
		// TODO Auto-generated method stub
		AccountQuery accountQuery = new AccountQuery();
		accountQuery.setCustmerId(id);
		return accountQuery;
	}

	@Override
	public BaseCommand getAccountRecharge(long id, long amt) {
		// TODO Auto-generated method stub
		AccountCharge accountCharge = new AccountCharge();
		accountCharge.setCustmerId(id);
		accountCharge.setChangeAmt(amt);
		return accountCharge;
	}

	@Override
	public BaseCommand getAddressQuery(long customerId) {
		// TODO Auto-generated method stub
		AddressQuery addressQuery = new AddressQuery();
		addressQuery.setCustmerId(customerId);
		return addressQuery;
	}

	@Override
	public BaseCommand getDistractQuery() {
		// TODO Auto-generated method stub
		DistractQuery distractQuery = new DistractQuery();
		return distractQuery;
	}

	@Override
	public BaseCommand getAddressCreate(long distractId, String detailAddress, long costomerId) {
		// TODO Auto-generated method stub
		AddressCreate addressCreate = new AddressCreate();
		addressCreate.setAddressDetail(detailAddress);
		addressCreate.setDistractId(distractId);
		addressCreate.setCustmerId(costomerId);
		return addressCreate;
	}

	@Override
	public BaseCommand getPlaceOrder(long customerId, String serviceType,
			String mobileNum, long carId, long distractId, long addressId,
			String payType, String note, String address) {
		// TODO Auto-generated method stub
		PlaceOrder placeOrder = new PlaceOrder();
		placeOrder.setAddress(address);
		placeOrder.setAddressId(addressId);
		placeOrder.setCarId(carId);
		placeOrder.setCustomerId(customerId);
		placeOrder.setDistractId(distractId);
		placeOrder.setMobileNum(mobileNum);
		placeOrder.setNote(note);
		placeOrder.setPayType(payType);
		placeOrder.setServiceType(serviceType);
		return placeOrder;
	}

	@Override
	public BaseCommand getOrderQuery(long custmerId, String orderState,
			int startIndex, int page) {
		// TODO Auto-generated method stub
		OrderQuery orderQuery = new OrderQuery();
		orderQuery.setCustomerId(custmerId);
		orderQuery.setOrderState(orderState);
		orderQuery.setPage(page);
		orderQuery.setStartIndex(startIndex);
		return orderQuery;
	}

	@Override
	public BaseCommand getActivityQuery(long customerId) {
		// TODO Auto-generated method stub
		CustomActivityQuery customActivityQuery = new CustomActivityQuery();
		customActivityQuery.setCustmerId(customerId);
		return customActivityQuery;
	}

	@Override
	public BaseCommand getAccountConsume(long customerId, int changeAmt,
			long orderId) {
		// TODO Auto-generated method stub
		AccountConsume accountConsume = new AccountConsume();
		accountConsume.setOrderId(orderId);
		accountConsume.setCustmerId(customerId);
		accountConsume.setChangeAmt(changeAmt);
		return accountConsume;
	}

	@Override
	public BaseCommand getActivityConsume(long customerId, long orderId) {
		// TODO Auto-generated method stub
		ActivityConsume activityConsume = new ActivityConsume();
		activityConsume.setCustmerId(customerId);
		activityConsume.setOrderId(orderId);
		return activityConsume;
	}

	@Override
	public BaseCommand getCustomerModify(long customerId, String customerName,
			String email, String headImg) {
		// TODO Auto-generated method stub
		CustomerModify customerModify = new CustomerModify();
		customerModify.setCustmerId(customerId);
		customerModify.setCustomerName(customerName);
		customerModify.setEmail(email);
		customerModify.setHeadImg(headImg);
		return customerModify;
	}

	@Override
	public BaseCommand getCarDelete(long carId) {
		// TODO Auto-generated method stub
		CarDelete carDelete = new CarDelete();
		carDelete.setCarId(carId);
		return carDelete;
	}

	@Override
	public BaseCommand getAddressDelete(long addressId) {
		// TODO Auto-generated method stub
		AddressDelete addressDelete = new AddressDelete();
		addressDelete.setAddressId(addressId);
		return addressDelete;
	}
	
}
