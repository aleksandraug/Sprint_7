package tests;


import io.restassured.response.Response;
import model.CreateOrderRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.TestData;
import сlient.OrderClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderCreateTest {

    private final OrderClient orderClient = new OrderClient();
    private Integer track;

    public static Stream<List<String>> orderColors() {
        return Stream.of(
                Collections.singletonList("BLACK"),
                Collections.singletonList("GREY"),
                Arrays.asList("BLACK", "GREY"),
                Collections.emptyList()
        );
    }

    @AfterEach
    public void tearDown() {
        if (track != null) {
            orderClient.cancelOrder(track);
        }
    }

    @ParameterizedTest
    @MethodSource("orderColors")
    @DisplayName("Можно создать заказ с разными вариантами цвета")
    public void shouldCreateOrderWithDifferentColors(List<String> colors) {
        CreateOrderRequest orderRequest = TestData.generateOrder(colors);

        Response response = orderClient.createOrder(orderRequest);

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().get("track"));

        track = response.jsonPath().getInt("track");
        assertTrue(track > 0);
    }
}