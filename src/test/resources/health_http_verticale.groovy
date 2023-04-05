vertx.createHttpServer().requestHandler({ request ->
  File file = new File("/usr/verticles/health_status.txt");
  def statusCode = file.text
  request.response().setStatusCode(statusCode.toInteger()).end(statusCode)
}).listen(8080);
