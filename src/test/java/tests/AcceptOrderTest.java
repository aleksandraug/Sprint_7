package tests;


import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import model.CreateOrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestData;
import сlient.CourierClient;
import сlient.OrderClient;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptOrderTest {

    private final CourierClient courierClient = new CourierClient();
    private final OrderClient orderClient = new OrderClient();

    private Integer courierId;
    private Integer track;
    private Integer orderId;

    @BeforeEach
    public void setUp() {
        Courier courier = TestData.generateCourier();
        courierClient.createCourier(courier);

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.jsonPath().getInt("id");

        CreateOrderRequest orderRequest = TestData.generateOrder(Collections.singletonList("BLACK"));
        Response createOrderResponse = orderClient.createOrder(orderRequest);
        track = createOrderResponse.jsonPath().getInt("track");

        Response getOrderResponse = orderClient.getOrderByTrack(track);
        orderId = getOrderResponse.jsonPath().getInt("order.id");
    }

    @AfterEach
    public void tearDown() {
        if (track != null) {
            orderClient.cancelOrder(track);
        }
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void shouldAcceptOrderSuccessfully() {
        Response response = orderClient.acceptOrder(orderId, courierId);

        assertEquals(200, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("ok"));
    }

    @Test
    public void shouldReturnErrorWhenCourierIdIsMissing() {
        Response response = orderClient.acceptOrderWithoutCourierId(orderId);

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturnErrorWhenCourierIdIsInvalid() {
        Response response = orderClient.acceptOrder(orderId, 999999);

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnErrorWhenOrderIdIsMissing() {
        Response response = orderClient.acceptOrderWithoutOrderId(courierId);

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnErrorWhenOrderIdIsInvalid() {
        Response response = orderClient.acceptOrder(999999, courierId);

        assertEquals(404, response.statusCode());
    }
}