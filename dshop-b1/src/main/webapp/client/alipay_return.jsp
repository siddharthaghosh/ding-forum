
<%
	/*
	 名称：付完款后跳转的页面
	功能：同服务器返回功能，但容易出现掉单.
	接口名称：标准实物双接口
	版本：2.0
	日期：2008-12-25
	作者：支付宝公司销售部技术支持团队
	联系：0571-26888888
	版权：支付宝公司
	 */
%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.alipay.util.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title></title>
	</head>
	<body>
		<%
			String partner = ""; //partner合作伙伴id（必须填写）
			String privateKey = ""; //partner 的对应交易安全校验码（必须填写）
			//**********************************************************************************
			//如果您服务器不支持https交互，可以使用http的验证查询地址
			//***注意下面的注释，如果在测试的时候导致response等于空值的情况，请将下面一个注释，打开上面一个验证连接
			//String alipayNotifyURL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify"
			String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
					+ "partner="
					+ partner
					+ "&notify_id="
					+ request.getParameter("notify_id");
			//**********************************************************************************
			String sign = request.getParameter("sign");
			//获取支付宝ATN返回结果，true是正确的订单信息，false 是无效的
			String responseTxt = CheckURL.check(alipayNotifyURL);

			Map params = new HashMap();
			//获得POST 过来参数设置到新的params中
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化（现在已经使用）
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
				params.put(name, valueStr);
			}

			String mysign = com.alipay.util.SignatureHelper_return.sign(params,
					privateKey);

			if (mysign.equals(request.getParameter("sign"))
					&& responseTxt.equals("true")) {
				//以下输出测试时候用，可以删除
				String get_order = request.getParameter("out_trade_no");
				String get_total_fee = request.getParameter("total_fee");
				String get_subject = new String(request.getParameter("subject")
						.getBytes("ISO-8859-1"), "GBK");
				String get_body = new String(request.getParameter("body")
						.getBytes("ISO-8859-1"), "GBK");
				out.println(get_order + "<br>");
				out.println(get_total_fee + "<br>");
				out.println(get_subject + "<br>");
				out.println(get_body + "<br>");
				out.println("交易状态:" + request.getParameter("trade_status")
						+ "<br>");
				out.println("显示订单信息" + "<br>");
				out.println("responseTxt=" + responseTxt + "<br>");
				//以下是实物接口的交易状态，可以根据不同的状态修改后台订单状态，不过建议数据处理放置在异步返回处理
				if ((request.getParameter("trade_status"))
						.equals("WAIT_SELLER_SEND_GOODS")) {
					out.println("买家已经付款，等待卖家发货"); // 买家已经付款，等待卖家发货，请更改订单状态
					//此返回方式， 只有再客户付款成功之后返回。
				}
				if ((request.getParameter("trade_status"))
						.equals("WAIT_BUYER_CONFIRM_GOODS")) {
					out.println("卖家已发货， 等待买家确认"); // 卖家已发货， 等待买家确认，请更改订单状态
					//此返回方式， 只有再客户付款成功之后返回。
				}
				if ((request.getParameter("trade_status"))
						.equals("TRADE_FINISHED")) {
					out.println("交易成功"); // 买家已经付款，交易完成，请更改订单状态
					//此返回方式， 只有再客户付款成功之后返回。
				}
			} else {
				//打印，收到消息比对sign的计算结果和传递来的sign是否匹配
				out.println(mysign + "----------------" + sign + "<br>");
				out.println("responseTxt=" + "<br>");
				out.println("支付失败" + responseTxt + "<br>");
			}
		%>




	</body>
</html>