
<%
   /*
    ���ƣ���������з�����֪ͨҳ��
	���ܣ�������֪ͨ���أ�������ֵ���������Ƽ�ʹ��
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
				//valueStr=new String (valueStr.getBytes("ISO-8859-1"),"GBK"); 
				params.put(name, valueStr);
			}
			
			String mysign = com.alipay.util.SignatureHelper.sign(params, privateKey);
			// ����״̬������Ϣ�뿴�ĵ����ܣ� ���ļ����������html���룬��ע�⣬ֻ��������ҵ�����.
			if (mysign.equals(request.getParameter("sign")) && responseTxt.equals("true") ){
				
				if(request.getParameter("trade_status").equals("WAIT_BUYER_PAY")){
					//�����Ѵ������ȴ���Ҹ��
					//���������д�����ݴ���,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("WAIT_SELLER_SEND_GOODS")){
					//��Ҹ���ɹ����ȴ����ҷ���
					//���������д�����ݴ���,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("WAIT_BUYER_CONFIRM_GOODS")){
					//�����ѷ����� �ȴ����ȷ��
					//���������д�����ݴ���,
					out.println("success");
				}else if(request.getParameter("trade_status").equals("TRADE_FINISHED")){
					//���׳ɹ�����
					//���������д�����ݴ���,
					out.println("success");
				}
			}
			else
			{
				out.println("fail");
			}
%>
