package co.xorde.market_making_service.client

import com.lbank.java.api.sdk.response.ResCancelOrderVo
import com.lbank.java.api.sdk.response.ResCreateOrderVo
import com.lbank.java.api.sdk.response.ResOrderVo

interface Client{

    fun createOrder(pair:String, type: String, price: Double, amount: Double, customId: String): ResCreateOrderVo
    fun cancelOrder(orderId: String): ResCancelOrderVo
    fun ordersInfo(orderId: String): ResOrderVo
}