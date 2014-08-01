/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.android.xiwao.washcar.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088511389645417";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "sh_xiwo@163.com";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANTuDwTgxstnYwCO+pMlBVsDPxO5x6WU4mj6vyFRBSOHj5K8t/8aIEzCb4z49y8lMen17zepRhhgJD1ExO4sepvNq4I3m67g9Sl5NJh4+9Vd9/inxp5MXn8vI2rjnhKOaCJF19PBFzaHRRh1xxNMaGWhtONkRHx7f0Mq8IbThH0pAgMBAAECgYA9ZXs9K9cGdYQteAy2evlFWfVJKctwajKylIKiB6uUqBT0+aeQTic8GuBxZKRZmr7+uCRHB28nvikU5YxnJLohRgHKs4rgIY/FH7gfXU9YtzkTrGeTK3M4IXGD5JXiALXfVRlMRFu55Dp7Nk1lZHXKGlxFq8sqP3ynCb9mo2QflQJBAPD/upmTDRfN4UPrur8wTghLBulTCXCS3IFYOyZCZVOHBXLJ+Cc355O7bfoj4/S+Yktqp3mov2FELOvDbmREsJ8CQQDiLw4qjNs4OkeGL0m7Ks4jRP+me7csUHUHlr2y2TysJga3RiQjE+Gj46Bdvob0IYSkcDFSWr0wPwkZxD/ASpU3AkANX9PGNA/08zr+HvXlKFQcR7Whezc55RyRnQjjTan74bFetnCsFIEohK1MxXMgIPSphMi4irHcPwFTMCxPKjS3AkEAxh5LfU/mEYlS7yFqpuxmpZQhjnVdTA6pg90xCoCoWW3xggWJT8RxZ7nR1Ws3EI5vfg3b1fQvNRxx30T8RnXN3wJATHQVTuAcIs9sAw3w/0zShGpMsM6OU24Ovdip/e2mRxy4Ac42wwY8VuBd70LwfaHu4dqZECkAUj4KuXQjC86V0Q==";

	public static final String PUBLIC = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANTuDwTgxstnYwCO+pMlBVsDPxO5x6WU4mj6vyFRBSOHj5K8t/8aIEzCb4z49y8lMen17zepRhhgJD1ExO4sepvNq4I3m67g9Sl5NJh4+9Vd9/inxp5MXn8vI2rjnhKOaCJF19PBFzaHRRh1xxNMaGWhtONkRHx7f0Mq8IbThH0pAgMBAAECgYA9ZXs9K9cGdYQteAy2evlFWfVJKctwajKylIKiB6uUqBT0+aeQTic8GuBxZKRZmr7+uCRHB28nvikU5YxnJLohRgHKs4rgIY/FH7gfXU9YtzkTrGeTK3M4IXGD5JXiALXfVRlMRFu55Dp7Nk1lZHXKGlxFq8sqP3ynCb9mo2QflQJBAPD/upmTDRfN4UPrur8wTghLBulTCXCS3IFYOyZCZVOHBXLJ+Cc355O7bfoj4/S+Yktqp3mov2FELOvDbmREsJ8CQQDiLw4qjNs4OkeGL0m7Ks4jRP+me7csUHUHlr2y2TysJga3RiQjE+Gj46Bdvob0IYSkcDFSWr0wPwkZxD/ASpU3AkANX9PGNA/08zr+HvXlKFQcR7Whezc55RyRnQjjTan74bFetnCsFIEohK1MxXMgIPSphMi4irHcPwFTMCxPKjS3AkEAxh5LfU/mEYlS7yFqpuxmpZQhjnVdTA6pg90xCoCoWW3xggWJT8RxZ7nR1Ws3EI5vfg3b1fQvNRxx30T8RnXN3wJATHQVTuAcIs9sAw3w/0zShGpMsM6OU24Ovdip/e2mRxy4Ac42wwY8VuBd70LwfaHu4dqZECkAUj4KuXQjC86V0Q==";

}
