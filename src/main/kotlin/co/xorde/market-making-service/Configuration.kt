package co.xorde.market_making_service

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * Configuration properties for the service
 */
@ConfigurationProperties(Configuration.PREFIX)
@Requires(property = Configuration.PREFIX)
class Configuration{

    /**
     * Trading pair (symbol). Please refer to exchange documentation for available pairs.
     * @url https://www.lbank.info/en-US/docs/index.html#available-trading-pairs
     */
    @NotNull
    @NotEmpty
    var pair: String? = null

    /**
     * Trading amount step divider
     */
    @NotNull
    @NotEmpty
    @Digits(integer=4, fraction=6)
    @DecimalMin("0.0", inclusive = false, message = "Please Enter a valid amountStep should be decimal and greater tha 0.0")
    var amountStep: Double? = null

    /**
     * Trading amount range min
     */
    @NotNull
    @NotEmpty
    @Digits(integer=4, fraction=6)
    @DecimalMin("0.0", inclusive = false, message = "Please Enter a valid amountRangeMin should be decimal and greater than 0.0 and less than amountRangeMax")
    var amountRangeMin: Double? = null

    /**
     * Trading amount range max
     */
    @NotNull
    @NotEmpty
    @Digits(integer=4, fraction=6)
    @DecimalMin("0.0", inclusive = false, message = "Please Enter a valid amountRangeMax should be decimal and greater than amountRangeMin")
    var amountRangeMax: Double? = null

    /**
     * Trading price range min
     */
    @NotNull
    @NotEmpty
    @Digits(integer=4, fraction=6)
    @DecimalMin("0.0", message = "Please Enter a valid priceRangeMin should be decimal and greater than 0.0 and less than priceRangeMax")
    var priceRangeMin: Double? = null

    /**
     * Trading price range max
     */
    @NotNull
    @Digits(integer=4, fraction=6)
    @DecimalMin("0.0", inclusive = false, message = "Please Enter a valid amountRangeMax should be decimal and greater than priceRangeMin")
    var priceRangeMax: Double? = null

    /**
     * Exchange API URL address
     */
    @NotNull
    @NotEmpty
    var apiUrl: String? = null

    /**
     * Exchange API key (public part)
     */
    @NotNull
    @NotEmpty
    var apiKey: String? = null

    /**
     * Exchange API secret (secret part)
     */
    @NotNull
    @NotEmpty
    var secretKey: String? = null

    /**
     * Exchange API key generation method: HMAC-SHA256, RSA
     */
    @NotNull
    @NotEmpty
    var method: String? = null

    companion object {
        const val PREFIX = "microservice.exchange"
    }
}
