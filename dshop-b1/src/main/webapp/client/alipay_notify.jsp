
<%
   /*
    名称：付款过程中服务器通知页面
	功能：服务器通知返回，不会出现掉单情况，推荐使用
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
				//valueStr=new String (valueStr.getBytes("ISO-8859-1"),"GBK"); 
				params.put(name, valueStr);
			}
			
			String mysign = com.alipay.util.SignatureHelper.sign(params, privateKey);
			// 交易状态更多信息请看文档介绍， 该文件不允许包含html代码，请注意，只能做数据业务操作.
			if (mysign.equals(request.getParameter("sign")) && responseTxt.equals("true") ){
				
				if(request.getParameter("trade_status").equals("WAIT_BUYER_PAY")){
					//交易已创建，等待买家付款。
					//在这里可以写入数据处理,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("WAIT_SELLER_SEND_GOODS")){
					//买家付款成功，等待卖家发货
					//在这里可以写入数据处理,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("WAIT_BUYER_CONFIRM_GOODS")){
					//卖家已发货， 等待买家确认
					//在这里可以写入数据处理,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("TRADE_FINISHED")){
					//交易成功结束
					//在这里可以写入数据处理,
					out.println("success");
				}
			}
			else
			{
				out.println("fail");
			}
%>
