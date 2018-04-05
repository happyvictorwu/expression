package com.yuxuan.admin.expression.demo;

import java.util.Map;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.yuxuan.admin.expression.utils.OrderInfoUtil2_0;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class PayDemoActivity extends FragmentActivity {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2018040102485657";
	
	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	/** 商户私钥，pkcs8格式 */
	/** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
	/** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
	/** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
	/** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
	/** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
	public static final String RSA_PRIVATE = "";
	public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3aWv2m9hEv9wYARqvOVom1+ae3i1o4FUOt8FLQSBLoZyOu8AG5meE+i3ilsGESPmJFBAzEJIAjU4j4eaGpB7kvKPqzf0mPvSy/FtNtSqO1qL9fkqRKp+lIuxptMCwLJhChNmzc5rdJq+JoQt+R4WHvumVH51z+5ohBkikit66IvRYvqWH3bcxCLnuvK3Ynv+XjEr1nhDfCnKlwhcq3N2G8LRMRCHftE+d9WasS/0i+xR53l3L0bfsx+ITQRKacQGmi23FhbWRuR6odCqg7HycBaNTmKiD/HWSvMyIVPm01jtbyAsDKlSAwZZVIj3eREv+2i4ZvaNjYxzEyZVU2nt1AgMBAAECggEAV4u4fuPwnRA/TC3qwMzNXVEcwaQnZLH/p6DKYNNbSP6BLhgsFp8Ptod8M3XmNPBoO6gZ+2XjauQH8lS3pnjcTi5Ex6U/Omw+fNi79CGPiNKmxfzsNtJzlW1QDzy55N3EZ6vmBiQePYVjsHvwcnxNHnhgIlAO5feooC7Rqcine4y4uOij62Dz0/FXsLIugWeomJA970bCSc33Q61d3dWgjK264qFcFeU6MlPMgQcz8tDzjMvme3/K70n84skBDn4R7VmFftkYuhvHcYSySNoOY0FMAL/b4UP1oAEdo0ngrAtRH//kYqW11eGC7W7ziFav9QFGBe3bBofIjP4J+Mj3AQKBgQDg1BBn/3L/BC04R3dATHTH+huod+q+0xhB7NdEbGNM7YE03HAt2uwuG4LnxE0iq00Xnl8POoMqi2LU6ds32PdU4z3y2/VJyj+6kLnY3kAAbrPJ6yzUnydCiiAX2M8nOITJBF2vyR4vzr1GS+SmU65BmR+sn33r9j7KmI9G437nlQKBgQDQ11ckvIG0nb/EWX7Apbqsq/Nr2EE2L1zYzOmylsWnKtLNKXJh2VUVtmFq+wRDUN2UpjXGWRVHnUqRWoRQs8RF9wtEiZDAeETbFpjqWx2OYg67t6PIOzAL7r7uwNffGvt1wF7IFLJVFI2Py42vcX5KEYZ/pqNJd9K0gDQHpdLMYQKBgQCToy5S3LCLPhbjyipJEuvtFhRrgLOqM7zOLdT+nZ5nud8K82bG9ef6Lx67S3DLv2mUhO8vdOEFYxq3bgoClnt0RvU7Ma3VkvZecQNZojitEAUIJ2L9DLYfP8zrW/hMbRTlW/SZppwoEL7CFrxDbNgVQ5RFK0bpWH7LU3tital0MQKBgG4z5BlPnAZKZAJh04AnNC2gAzC6ihbkwVLqNrv10dEXyOtYXrKBs+NkPV3tnd9D0dl6J9BF+/OxbcLsB/xqED13UqvVB9x42qTd9M1eTWdwGSQ3kKa9jOoPDxQAESn7HAyT+bANM9mvoc/qcxbMhf7h5p5/uZSwNTDoogwTFfyhAoGBANeksbQX+ZxvdTMZdfhFVXehyCo0BneaTg3M1njrlmypTrTgwt3pwo1Q5scCjwIrmUg4H1sTiiY1bBVvdm7Zmnn2dKUrtrGPj6IZ+0UHYZO3IimzdhWqZdbWsSEwovDXbfyeOuin850DlcHHqPH6/KwaiyhE+dxNZf4U/Hxsonen";
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(PayDemoActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(PayDemoActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;
			}
			default:
				break;
			}
		};
	};


	/**
	 * 支付宝支付业务
	 * 
	 * @param v
	 */
	public void payV2(View v) {
		if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
		final String orderInfo = orderParam + "&" + sign;
		
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
				PayTask alipay = new PayTask(PayDemoActivity.this);
				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
				|| (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
				|| TextUtils.isEmpty(TARGET_ID)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		boolean rsa2 = (RSA2_PRIVATE.length() > 0);
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		
		String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(PayDemoActivity.this);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}
}
