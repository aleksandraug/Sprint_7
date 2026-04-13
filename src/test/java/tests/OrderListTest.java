package tests;


import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import сlient.OrderClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OrderListTest {

    private final OrderClient orderClient = new OrderClient();

    @Test
    public void shouldReturnOrdersList() {
        Response response = orderClient.getOrdersList();

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().getList("orders"));
    }
}