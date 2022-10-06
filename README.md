# Market Making Service

Simple market making service that can be used to provide liquidity to a LBank exchange.

## Quick Start

### Grab Source Code

```bash
git clone https://github.com/xorde-microservices/market-making-service.git
```

### Run Docker Compose

```bash
cd market-making-service
docker-compose up -d
```

##  Build

In project home dir run:
1. `gradle build`
2. `docker build -t market-making-service .`
3. `docker-compose up` Or run from env file: `docker compose --env-file .env.dev up`

## Configurations
Main payment configuration located in `application.yml` file. 
`./src/main/resources/application.yml`

```
microservice:
  orderExchangeCronJob: '* * * * *'
   exchange:
    method: RSA
    apiKey: TEST
    secretKey: TEST
    pair: usdr_usdt
    amountStep: 0.0001
    amountRangeMin: 1
    amountRangeMax: 1.5
    priceRangeMin: 0.9997
    priceRangeMax: 1.0000
```

The service can also be configured via environment variables.

### Payment exchange cron expression:

```
microservice:
    orderExchangeCronJob: '* * * * *'
```
To execute a task every minute - `'* * * * *'` 

### Payment exchange LBank API config:
```
method: RSA
apiKey: YourApiKeyValue
secretKey: YourSecretKeyValue
pair: usdr_usdt
```

1. `method` - signature_method encryption: RSA/HmacSHA256
2. `apiKey` - is used in API request.
3. `secretKey` - is used to generate the signature(only visible once after creation in BE)
4. `pair` - currency pair for processing 

### Payment exchange generation configs:
```
amountStep: 0.0001
amountRangeMin: 1
amountRangeMax: 1.5
priceRangeMin: 0.9997
priceRangeMax: 1.0000
```
1. `amountStep` - amount generation step
2. `amountRangeMin` - amount generation min value
3. `amountRangeMax` - amount generation max value
4. `priceRangeMin` - price generation min value
5. `priceRangeMax` - price generation max value