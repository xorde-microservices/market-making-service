package co.xorde.market_making_service

/**
 * Service constants
 */
interface Constants {
    companion object {
        const val DEFAULT_PAIR: String = "usdr_usdt"
        const val SELL: String = "sell"
        const val BUY: String = "buy"
        const val BUY_MARKET = "buy_market"
        const val SELL_MARKET = "sell_market"
        const val BUY_MAKER = "buy_maker"
        const val SELL_MAKER = "sell_maker"
        const val BUY_IOC = "BUY_IOC"
        const val SELL_IOC = "SELL_IOC"
        const val BUY_FOK = "buy_fok"
        const val SELL_FOK = "sell_fok"

        const val STATUS_ON_TRADING = "0"
        const val STATUS_FILLED_PARTIALLY = "1"
        const val STATUS_FILLED_PARTIALLY_AND_CANCELLED = "3"

        const val SELL_ORDER_ID_KEY:String = "sell_order_id_key"
        const val BUY_ORDER_ID_KEY:String = "buy_order_id_key"
    }
}