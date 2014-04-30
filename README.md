# geggg-scala-http
This is simple HTTP access client library __without third pirty's library depencencies__ .

This library supports GET, POST, and PUT method now.

## Simple GET/POST access

```scala
val client = HttpClient()
val resGet = client.get(YOUR_URL)
val resPost = client.post(YOUR_URL)

resGet.status // is HTTP status code
resGet.contents // is response contents(like HTML, etc)
```

## Access with query parameter
If you want to use query parameter, you can access `QueryMap` class and mothods.

```scala
val client = HttpClient()
val query = Map("key1" -> "value1") // Key-Value combinations of query using Map

val resGet = client.get(YOUR_URL, query)
val resPost = client.post(YOUR_URL, query)
```

### More information of QueryMap class
`QueryMap` supports 2 convenient methods.

```scala
val query = Map("key1" -> "value1", "key2" -> "あいうえお")
query.queryString("key1") // return string "key1=value1"
query.queryString("key2") // return string with encoding "key2=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A&"
query.queries() // return parameter string joining & character "key1=value1&key2=%E3%81%82%E3%81%84%E3%81%86%E3%81%88%E3%81%8A"
```

## Basic authentication
You can use `BasicAuthUser` class.

```scala
val client = HttpClient()
val query = QueryMap("keyX" -> "valueX")
val auth = BasicAuthUser(YOUR_USERNAME, YOUR_PASSWORD)

val resGet = client.get(YOUR_URL, query, auth)
val resPost = client.post(YOUR_URL, query, auth)
```

## Customize request configuration
If you want to modify some request settings, you are available for `HttpConfig` class.

```scala
val config = HttpConfig(
  30000, // connect timeout (default is 10s)
  40000, // read timeout (default is 10s)
  "Shift_JIS", // encoding of response contents (default is UTF-8)
  Map("User-Agent" -> YOUR_USER_AGENT) // Key-Value of request headers using Map (default is empty)
)

val client = HttpClient(config)
:
:
```
