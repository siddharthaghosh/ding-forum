/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ding.snippet

import scala.xml.NodeSeq
import scala.xml.Text
import com.ding.model.CartInfo
import com.ding.util.MetaModels
import net.liftweb.http.SHtml
import net.liftweb.http.RequestVar
import com.alipay.util._

object paymentPrice extends RequestVar[Double](0)
object paymentOrderNo extends RequestVar[String]({""})

class CartSnippet {
    def cartBrief(kids: NodeSeq): NodeSeq = {
        val itemNum = CartInfo.allItems.length
        Text("Cart now has " + itemNum.toString + " items.") ++
        SHtml.link("cart.html", () => {}, Text("go to cart"))
    }

    def cartDetail(kids : NodeSeq) : NodeSeq = {
        val itemIDs = CartInfo.allItems
        var totalPrice : Double = 0
        val detailnode = itemIDs.flatMap {
            gid => {
                val gitem = MetaModels.metaGoods.findOneInstance(gid)
                val pitem = MetaModels.metaProduct.findOneInstance(gitem.getProductID)
                val name = pitem.getName(com.ding.util.LanguageUtils.getDefaultLang)
                val price = gitem.getPrice
                val store = gitem.getStore
                totalPrice = totalPrice + price
                Text("product name : " + name) ++ Text("\t") ++
                Text("prodcut price : " + price.toString) ++ Text("\t") ++
                Text("product store : " + store.toString) ++ <br/>

            }
        }
        
        def createOrder() {
            val orderNo = net.liftweb.util.StringHelpers.randomString(32)
            paymentOrderNo(orderNo)
            paymentPrice(totalPrice)
        }

        detailnode ++
        Text("Total price : " + totalPrice.toString) ++ <br/> ++ SHtml.link("pay.html", createOrder, Text("Pay Now!"))

    }

    def testPay(kids : NodeSeq) : NodeSeq = {
        val pprice = paymentPrice.get
        val porder = paymentOrderNo.get
        Text("price : " + pprice.toString + "\t") ++ Text("OrderNumber : " + porder) ++
        <br/>
    }

    def pay(kids : NodeSeq) : NodeSeq = {

        val paygateway = "https://www.alipay.com/cooperate/gateway.do?"
        val service = "create_partner_trade_by_buyer"
        val sign_type = "MD5"
        val out_trade_no = paymentOrderNo.get
        val input_charset = "utf-8"
        val partner = "2088002093319313"
        val key = "89hcjl8mg99e6juosy2pc6ew6oloox8t"
	val seller_email = "killer456@21cn.com"

        val body = "支付宝（订单号:"+out_trade_no+")"; //商品描述，推荐格式：商品名称（订单编号：订单编号）,例如：支付宝（订单号：2008122500120）
	val subject = "test goods"; //商品名称，如果想显示多个商品名称，可以将多个商品叠加传入。
	val price = paymentPrice.get.toString; //订单总价，范围：0.01～100000000.00（小数点后面最多两位）例如：23.80
	val quantity = "1";  //一般情况可以默认为1，具体可以参看开发文档
	val show_url = "http://www.xianglegou.com.cn";   //商品展示地址，例如：http://www.alipay.com；如果锁定具体商品页面，可以传入（http://www.alipay.com?pid=变量）
	val payment_type = "1";   //支付宝类型.1代表商品购买（目前填写1即可，不可以修改）
	val discount = "0";   //折扣价格，不能直接传入百分比，范围：-10000000.00<i<10000000.00

        val logistics_type = "EXPRESS";  //物流配送方式：POST(平邮)、EMS(EMS)、EXPRESS(其他快递)
	val logistics_fee = "0.00";  //物流配送费用x
	val logistics_payment = "SELLER_PAY";     //物流配送费用付款方式：SELLER_PAY(卖家支付)、BUYER_PAY(买家支付)、BUYER_PAY_AFTER_RECEIVE(货到付款)
	val notify_url = "http://192.168.1.103:8080/client/alipay_notify.jsp"; //通知接收URL，需要写返回处理文件的绝对路径，例如："http://localhost:8081/jsp_shi_gbk/alipay_notify.jsp"
	val return_url = "http://192.168.1.103:8080/client/alipay_return.jsp"; //支付完成后跳转返回的网址URL,需要写返回处理文件的绝对路径，例如：http://localhost:8080/jsp_shi_gbk/alipay_return.jsp



        val ItemUrl = Payment.CreateUrl(paygateway, service, sign_type,
                                              out_trade_no, input_charset, partner, key, seller_email,
                                              body, subject, price, quantity, show_url, payment_type,
                                              discount, logistics_type, logistics_fee, logistics_payment,
                                              return_url, notify_url)
        <a href={ItemUrl}> <img src="/client/images/alipay_bwrx.gif" border="0" /></a>
            }
            }
