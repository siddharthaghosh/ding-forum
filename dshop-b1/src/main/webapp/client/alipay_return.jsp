
<%
	/*
	 ���ƣ���������ת��ҳ��
	���ܣ�ͬ���������ع��ܣ������׳��ֵ���.
	�ӿ����ƣ���׼ʵ��˫�ӿ�
	�汾��2.0
	���ڣ�2008-12-25
	���ߣ�֧������˾���۲�����֧���Ŷ�
	��ϵ��0571-26888888
	��Ȩ��֧������˾
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
			String partner = ""; //partner�������id��������д��
			String privateKey = ""; //partner �Ķ�Ӧ���װ�ȫУ���루������д��
			//**********************************************************************************
			//�������������֧��https����������ʹ��http����֤��ѯ��ַ
			//***ע�������ע�ͣ�����ڲ��Ե�ʱ����response���ڿ�ֵ��������뽫����һ��ע�ͣ�������һ����֤����
			//String alipayNotifyURL = "https://www.alipay.com/cooperate/gateway.do?service=notify_verify"
			String alipayNotifyURL = "http://notify.alipay.com/trade/notify_query.do?"
					+ "partner="
					+ partner
					+ "&notify_id="
					+ request.getParameter("notify_id");
			//**********************************************************************************
			String sign = request.getParameter("sign");
			//��ȡ֧����ATN���ؽ����true����ȷ�Ķ�����Ϣ��false ����Ч��
			String responseTxt = CheckURL.check(alipayNotifyURL);

			Map params = new HashMap();
			//���POST �����������õ��µ�params��
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
				//����������δ����ڳ�������ʱʹ�á����mysign��sign�����Ҳ����ʹ����δ���ת���������Ѿ�ʹ�ã�
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
				params.put(name, valueStr);
			}

			String mysign = com.alipay.util.SignatureHelper_return.sign(params,
					privateKey);

			if (mysign.equals(request.getParameter("sign"))
					&& responseTxt.equals("true")) {
				//�����������ʱ���ã�����ɾ��
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
				out.println("����״̬:" + request.getParameter("trade_status")
						+ "<br>");
				out.println("��ʾ������Ϣ" + "<br>");
				out.println("responseTxt=" + responseTxt + "<br>");
				//������ʵ��ӿڵĽ���״̬�����Ը��ݲ�ͬ��״̬�޸ĺ�̨����״̬�������������ݴ���������첽���ش���
				if ((request.getParameter("trade_status"))
						.equals("WAIT_SELLER_SEND_GOODS")) {
					out.println("����Ѿ�����ȴ����ҷ���"); // ����Ѿ�����ȴ����ҷ���������Ķ���״̬
					//�˷��ط�ʽ�� ֻ���ٿͻ�����ɹ�֮�󷵻ء�
				}
				if ((request.getParameter("trade_status"))
						.equals("WAIT_BUYER_CONFIRM_GOODS")) {
					out.println("�����ѷ����� �ȴ����ȷ��"); // �����ѷ����� �ȴ����ȷ�ϣ�����Ķ���״̬
					//�˷��ط�ʽ�� ֻ���ٿͻ�����ɹ�֮�󷵻ء�
				}
				if ((request.getParameter("trade_status"))
						.equals("TRADE_FINISHED")) {
					out.println("���׳ɹ�"); // ����Ѿ����������ɣ�����Ķ���״̬
					//�˷��ط�ʽ�� ֻ���ٿͻ�����ɹ�֮�󷵻ء�
				}
			} else {
				//��ӡ���յ���Ϣ�ȶ�sign�ļ������ʹ�������sign�Ƿ�ƥ��
				out.println(mysign + "----------------" + sign + "<br>");
				out.println("responseTxt=" + "<br>");
				out.println("֧��ʧ��" + responseTxt + "<br>");
			}
		%>




	</body>
</html>