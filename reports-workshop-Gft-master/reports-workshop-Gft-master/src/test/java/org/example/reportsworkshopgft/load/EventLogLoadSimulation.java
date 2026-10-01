package org.example.reportsworkshopgft.load;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class EventLogLoadSimulation extends Simulation {

    private static final String BASE_URL = System.getProperty("baseUrl", "http://localhost:8081");

    private static final int CONCURRENT_USERS = 20;
    private static final int RAMP_SECONDS = 10;
    private static final int HOLD_SECONDS = 30;

    private final HttpProtocolBuilder httpProtocol =
            http.baseUrl(BASE_URL)
                    .acceptHeader("application/json")
                    .contentTypeHeader("application/json");

    public ScenarioBuilder buildEventLogScenario() {
        return scenario("EventLog API Load")
                .exec(
                        http("GET /reports/ — all events paginated")
                                .get("/reports/")
                                .queryParam("page", "0")
                                .queryParam("size", "20")
                                .check(status().is(200)))
                .pause(1)
                .exec(
                        http("GET /reports/stats — system stats")
                                .get("/reports/stats")
                                .check(status().is(200))
                                .check(jmesPath("totalOrders").exists()))
                .pause(1)
                .exec(
                        http("GET /reports/orders/history — order history")
                                .get("/reports/orders/history")
                                .queryParam("page", "0")
                                .queryParam("size", "50")
                                .check(status().is(200)));
    }

    public PopulationBuilder buildPopulation() {
        return buildEventLogScenario()
                .injectOpen(
                        rampUsers(CONCURRENT_USERS).during(RAMP_SECONDS),
                        constantUsersPerSec(5).during(HOLD_SECONDS));
    }

    {
        setUp(buildPopulation())
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(2000),
                        global().successfulRequests().percent().gt(99.0));
    }
}
