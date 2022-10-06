package co.xorde.market_making_service.client.impl

import co.xorde.market_making_service.Configuration
import co.xorde.market_making_service.client.Client
import com.lbank.java.api.sdk.response.ResCancelOrderVo
import com.lbank.java.api.sdk.response.ResCreateOrderVo
import com.lbank.java.api.sdk.response.ResOrderVo
import com.lbank.java.api.sdk.service.LBankServiceImpl
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

@Singleton
open class LBankClientImpl @Inject constructor(private val config: Configuration) : Client {

    private val client: LBankServiceImpl
    private var df:DecimalFormat

    init {
        val otherSymbols = DecimalFormatSymbols(Locale.US)
        otherSymbols.decimalSeparator = '.'
        otherSymbols.groupingSeparator = ','
        df = DecimalFormat("###0.0000", otherSymbols)
        client = LBankServiceImpl(config.apiKey, config.secretKey, config.method)
    }

    /**
     * Create exchange order
     * @param pair Trading pair
     * @param type Order type
     * @param price Order price
     * @param amount Order amount
     * @param customId Custom Order ID (Used to identify the order)
     * @return
     */
    override fun createOrder(pair:String, type: String, price: Double, amount: Double, customId: String): ResCreateOrderVo
    {
        val formattedAmount = df.format(amount)
        val formattedPrice = df.format(price)
        LOG.debug("Request order type:{} pair: {} formattedAmount: {} formattedPrice: {}  customId: {}",type, pair, formattedAmount, formattedPrice, customId)

        return client.createOrder(pair, type, formattedPrice, formattedAmount, customId)
    }

    /**
     * Cancel exchange order
     * @param orderId
     * @return
     */
    override fun cancelOrder(orderId: String): ResCancelOrderVo
    {
        return client.cancelOrder(config.pair, orderId)
    }

    /**
     * Get exchange order
     * @param orderId
     * @return
     */
    override fun ordersInfo(orderId: String): ResOrderVo
    {
        LOG.debug("Check order status request - pair: {}  orderId: {} ", config.pair, orderId)
        return client.getOrdersInfo(config.pair, orderId)
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(LBankClientImpl::class.java)
    }
}