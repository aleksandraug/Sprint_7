package tests;

import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import util.TestData;
import сlient.CourierClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierCreateTest {

    private final CourierClient courierClient = new CourierClient();
    private Integer courierId;

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void shouldCreateCourierSuccessfully() {
        Courier courier = TestData.generateCourier();

        Response createResponse = courierClient.createCourier(courier);
        assertEquals(201, createResponse.statusCode());
        assertTrue(createResponse.jsonPath().getBoolean("ok"));

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.jsonPath().getInt("id");

        assertNotNull(courierId);
    }

    @Test
    public void shouldNotCreateDuplicateCourier() {
        Courier courier = TestData.generateCourier();

        Response firstCreateResponse = courierClient.createCourier(courier);
        assertEquals(201, firstCreateResponse.statusCode());

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.jsonPath().getInt("id");

        Response secondCreateResponse = courierClient.createCourier(courier);

        assertEquals(409, secondCreateResponse.statusCode());
        assertEquals("Этот логин уже используется. Попробуйте другой.",
                secondCreateResponse.jsonPath().getString("message"));
    }

    @Test
    public void shouldReturnErrorWhenLoginIsMissing() {
        Courier courier = new Courier(null, "1234", "Viktor");

        Response response = courierClient.createCourier(courier);

        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message"));
    }

    @Test
    public void shouldReturnErrorWhenPasswordIsMissing() {
        Courier courier = new Courier("test_login", null, "Viktor");

        Response response = courierClient.createCourier(courier);

        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи",
                response.jsonPath().getString("message"));
    }
}