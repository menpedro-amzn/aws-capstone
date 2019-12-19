import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class LoadTest extends Simulation {

  val httpConf = http
    .baseUrl("http://c2a7c096-default-ordersing-e3d7-274267425.eu-west-1.elb.amazonaws.com")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("Simple")
    .exec(http("Action")
      .get("/orders/last")
      .check(status.is(200), jsonPath("$.orderLines").exists))

  setUp(scn.inject(rampConcurrentUsers(100) to(1000) during(3 minutes)))
    .protocols(httpConf)
    .assertions(
      forAll.failedRequests.percent.is(0),
      global.responseTime.percentile3.lt(1000)
    )
}
