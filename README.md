```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/BTC_values.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/DOGE_values.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/ETH_values.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/LTC_values.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/XRP_values.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/BTC_values_01_24.csv"
```

```sh
curl -X POST http://localhost:8080/api/ingest -F "file=@docs/prices/DOGE_values_06_24.csv"
```

```sh
curl http://localhost:8080/api/BTC/stats/yearly
```

```sh
curl http://localhost:8080/api/DOGE/stats/six-months
```

```sh
curl http://localhost:8080/api/normalized-range
```

```sh
curl http://localhost:8080/api/DOGE/stats
```

```sh
curl http://localhost:8080/api/highest-normalized-range/2022-01-21
```