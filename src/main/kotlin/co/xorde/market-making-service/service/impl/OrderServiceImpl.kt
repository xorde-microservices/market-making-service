package co.xorde.market_making_service.service.impl

import co.xorde.market_making_service.client.Client
import co.xorde.market_making_service.Configuration
import co.xorde.market_making_service.Constants
import co.xorde.market_making_service.Constants.Companion.BUY_ORDER_ID_KEY
import co.xorde.market_making_service.Constants.Companion.SELL_ORDER_ID_KEY
import co.xorde.market_making_service.Constants.Companion.STATUS_FILLED_PARTIALLY
import co.xorde.market_making_service.Constants.Companion.STATUS_ON_TRADING
import co.xorde.market_making_service.service.OrderService
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.validation.Valid
import kotlin.collections.HashMap
import kotlin.math.floor
import kotlin.math.roundToInt

@Singleton
open class OrderServiceImpl @Inject constructor(@Valid config: Configuration, client: Client) : OrderService {

    private var clientApi: Client
    private var config: Configuration
    private var df = DecimalFormat("###0.0000")
    private var metadata: HashMap<String,String> = HashMap()
    private val roundVal = 10000.0

    init {
        val otherSymbols = DecimalFormatSymbols(Locale.US)
        otherSymbols.decimalSeparator = '.'
        otherSymbols.groupingSeparator = ','
        df = DecimalFormat("###0.0000", otherSymbols)
        this.clientApi = client
        this.config = config
        LOG.info("Using API Key: ${config.apiKey}")
    }

    /**
     * This method is used to start order iteration
     */
    override fun orderExchange() {
        val amount = getAmount(config.amountRangeMin!!,config.amountRangeMax!!,config.amountStep!!)
        val price = getPrice(config.priceRangeMin!!, config.priceRangeMax!!)
        orderExchange(config.pair!! ,price, amount)
    }

    /**
     * This method is used to start trading iteration
     * @param pair Pair/symbol of the order
     * @param price - Price of the order
     * @param amount - Amount of the order
     */
    override fun orderExchange(pair: String, price:Double, amount:Double) {

        if(metadata.containsKey(SELL_ORDER_ID_KEY)){
            metadata[SELL_ORDER_ID_KEY]?.let { cancelOrderIfActive(pair, it) }
        }

        LOG.info("Start Order Exchange pair: {} , amount: {} , price: {} ", pair, amount, price)
        val sellOrderId = createSellOrder(pair, price, amount)
        sellOrderId?.let {  metadata[SELL_ORDER_ID_KEY] = it }

        val buyOrderId = createBuyOrder(pair, price, amount)
        buyOrderId?.let {  metadata[BUY_ORDER_ID_KEY] = it }

        LOG.info("End Order Exchange pair: {} , amount: {} , price: {} sellOrder: {} buyOrder: {}", pair, amount, price, sellOrderId, buyOrderId)
    }

    /**
     * This method is used to cancel order if it is active
     * @param pair Pair/symbol of the order
     * @param orderId Order id of the order
     */
    override fun cancelOrderIfActive(pair: String, orderId: String) {

        val orderInfo = clientApi.ordersInfo(orderId)
        LOG.info("Check order response - orderId: {} order: {}", orderId, orderInfo)
        if (orderInfo.result) {
            for (order in orderInfo.data) {
                if (STATUS_FILLED_PARTIALLY == order.status || STATUS_ON_TRADING == order.status) {
                    cancelOrder(order.symbol, order.order_id)
                }
            }
        } else {
            LOG.error("Check order status - orderId: {} order: {}", orderId, orderInfo)
        }
    }

    /**
     * This method is used to cancel order
     * @param pair Pair/symbol of the order
     * @param orderId Order id of the order
     */
    private fun cancelOrder(pair:String, orderId: String) {
        LOG.info("Cancel order request - pair: {}  orderId: {} ", pair, orderId)
        val cancelOrder = clientApi.cancelOrder(orderId)
        if (cancelOrder.result) {
            LOG.info("Cancel order response - pair: {}  orderId: {} order: {}", pair, orderId, cancelOrder)
        } else {
            LOG.error("Cancel order error - error: {} pair: {}  orderId: {} order: {}", cancelOrder.error_code, pair, orderId, cancelOrder)
        }
    }

    /**
     * This method is used to create sell order
     * @param pair Pair/symbol of the order
     * @param price Price of the order
     * @param amount Amount of the order
     * @return Order id of the order or null if order is not created
     */
    private fun createSellOrder(pair:String, price: Double, amount: Double): String? {

        val customId = UUID.randomUUID().toString()
        val sellOrder = clientApi.createOrder(pair, Constants.SELL, price, amount, customId)

        if (sellOrder.result) {
            LOG.info("Sell Order SUCCESS data: {} ", sellOrder)
            return sellOrder.data["order_id"]
        } else {
            LOG.error("sellOrder:{} ", sellOrder)
        }

        return null
    }

    /**
     * This method is used to create buy order
     * @param pair Pair/symbol of the order
     * @param price Price of the order
     * @param amount Amount of the order
     * @return Order id of the order or null if order is not created
     */
    private fun createBuyOrder(pair:String, price: Double, amount: Double): String? {

        val customId = UUID.randomUUID().toString()
        val sellOrder = clientApi.createOrder(pair, Constants.BUY, price, amount, customId)

        if (sellOrder.result) {
            LOG.info("Buy Order SUCCESS data: {} ", sellOrder)
            return sellOrder.data["order_id"]
        } else {
            LOG.error("Buy order: {} ", sellOrder)
        }
        return null
    }

    /**
     * This method is used to get amount of the order
     * @param min Amount range min
     * @param max Amount range max
     * @return Amount of the order
     */
    override fun getAmount(min:Double, max: Double, step:Double): Double {
        var amount = floor(Math.random() * max.minus(min) / step).toInt() * step + min
        amount = (amount * roundVal).roundToInt() / roundVal
        LOG.debug("CalculateAmount result min: {}  max: {}  step: {}  amount: {} ", min, max, step, amount)
        return amount
    }

    /**
     * This method is used to get price of the order
     * @param min Price range min
     * @param max Price range max
     * @return - price of the order
     */
    override fun getPrice(min:Double, max:Double): Double {
        var price: Double = ThreadLocalRandom.current().nextDouble(min, max)
        price = (price * roundVal).roundToInt() / roundVal
        LOG.debug("CalculatePrice result priceRangeMin: {}  priceRangeMax: {} price: {} ", min, max, price)
        return price
    }

    override fun getMetadata(): HashMap<String,String>{
        return metadata
    }

    override fun setMetadata(metadata: HashMap<String,String> ){
        this.metadata = metadata
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(OrderServiceImpl::class.java)
    }
}