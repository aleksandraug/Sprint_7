package tests;


import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.Test;
import util.TestData;
import сlient.CourierClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteCourierTest {

    private final CourierClient courierClient = new CourierClient();

    @Test
    public void shouldDeleteCourierSuccessfully() {
        Courier courier = TestData.generateCourier();

        courierClient.createCourier(courier);
        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        int courierId = loginResponse.jsonPath().getInt("id");

        Response deleteResponse = courierClient.deleteCourier(courierId);

        assertEquals(200, deleteResponse.statusCode());
        assertTrue(deleteResponse.jsonPath().getBoolean("ok"));
    }

    @Test
    public void shouldReturnErrorWhenDeleteWithoutId() {
        Response response = courierClient.deleteCourierWithoutId();

        assertEquals(404, response.statusCode());
    }

    @Test
    public void shouldReturnErrorWhenDeleteWithNonExistingId() {
        Response response = courierClient.deleteCourier(999999);

        assertEquals(404, response.statusCode());
    }
}