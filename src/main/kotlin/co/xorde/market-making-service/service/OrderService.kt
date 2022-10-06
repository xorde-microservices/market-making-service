package co.xorde.market_making_service.service

interface OrderService {
    fun orderExchange()
    fun orderExchange(pair: String, price:Double, amount:Double)
    fun cancelOrderIfActive(pair: String, orderId: String)
    fun getAmount(min:Double, max: Double, step:Double): Double
    fun getPrice(min: Double, max: Double): Double
    fun getMetadata(): HashMap<String, String>
    fun setMetadata(metadata: HashMap<String,String> )
}