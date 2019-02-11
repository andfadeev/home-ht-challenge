# home-ht-challenge

## How to run

To start service backed with postgresql db, run from root of this repo:
```
docker-compose up -d
```

Db schema is in `init.sql` with predefined contracts.

## Test

Create some payments:

```
curl -X POST \
  http://localhost:8080/contract/1/payment \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"time": "2016-12-09T00:00:00.00Z",
	"description": "Rent payment",
	"value": 100
}'
```

```
curl -X POST \
  http://localhost:8080/contract/1/payment \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"time": "2016-12-09T00:00:00.00Z",
	"description": "Rent to be paid",
	"value": -100
}'
```

Query payments:

```
curl -X GET \
  'http://localhost:8080/contract/1/payments?startDate=2016-12-09T00:00:00.00Z&endDate=2016-12-10T00:00:00.00Z' \
  -H 'Cache-Control: no-cache'
```

Without results:

```
curl -X GET \
  'http://localhost:8080/contract/1/payments?startDate=2016-12-10T00:00:00.00Z&endDate=2016-12-11T00:00:00.00Z' \
  -H 'Cache-Control: no-cache'
```

Mark as deleted:

```
curl -X POST \
  http://localhost:8080/payment/1/delete \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json'
```

Update payment:

```
curl -X POST \
  http://localhost:8080/payment/2/update \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -d '{
	"time": "2016-12-10T00:00:00.00Z",
	"description": "More rent to be paid",
	"value": -120
}'
```




