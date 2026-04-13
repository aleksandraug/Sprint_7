package tests;

import io.restassured.response.Response;
import model.CreateOrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestData;
import сlient.OrderClient;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GetOrderByTrackTest {

    private final OrderClient orderClient = new OrderClient();
    private Integer track;

    @BeforeEach
    public void setUp() {
        CreateOrderRequest orderRequest = TestData.generateOrder(Collections.singletonList("BLACK"));
        Response createOrderResponse = orderClient.createOrder(orderRequest);
        track = createOrderResponse.jsonPath().getInt("track");
    }

    @AfterEach
    public void tearDown() {
        if (track != null) {
            orderClient.cancelOrder(track);
        }
    }

    @Test
    public void shouldReturnOrderObjectByTrack() {
        Response response = orderClient.getOrderByTrack(track);

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().get("order"));
    }

    @Test
    public void shouldReturnErrorWhenTrackIsMissing() {
        Response response = orderClient.getOrderByTrackWithoutTrack();

        assertEquals(400, response.statusCode());
    }

    @Test
    public void shouldReturnErrorWhenTrackIsInvalid() {
        Response response = orderClient.getOrderByTrack(999999);

        assertEquals(404, response.statusCode());
    }
}