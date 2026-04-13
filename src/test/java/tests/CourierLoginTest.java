package tests;


import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestData;
import сlient.CourierClient;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourierLoginTest {

    private final CourierClient courierClient = new CourierClient();
    private Courier courier;
    private Integer courierId;

    @BeforeEach
    public void setUp() {
        courier = TestData.generateCourier();
        courierClient.createCourier(courier);

        Response loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.jsonPath().getInt("id");
    }

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    public void shouldLoginCourierSuccessfully() {
        Response response = courierClient.loginCourier(CourierCredentials.from(courier));

        assertEquals(200, response.statusCode());
        assertNotNull(response.jsonPath().get("id"));
        assertTrue(response.jsonPath().getInt("id") > 0);
    }

    @Test
    public void shouldReturnErrorWhenLoginIsMissing() {
        CourierCredentials credentials = new CourierCredentials(null, courier.getPassword());

        Response response = courierClient.loginCourier(credentials);

        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для входа",
                response.jsonPath().getString("message"));
    }

    @Test
    @Disabled("Стенд нестабилен: вместо 400 иногда возвращает 504")
    public void shouldReturnErrorWhenPasswordIsMissing() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), null);

        Response response = courierClient.loginCourier(credentials);

        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для входа",
                response.jsonPath().getString("message"));
    }

    @Test
    public void shouldReturnErrorForWrongLogin() {
        CourierCredentials credentials = new CourierCredentials("wrong_login", courier.getPassword());

        Response response = courierClient.loginCourier(credentials);

        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена",
                response.jsonPath().getString("message"));
    }

    @Test
    public void shouldReturnErrorForWrongPassword() {
        CourierCredentials credentials = new CourierCredentials(courier.getLogin(), "wrong_password");

        Response response = courierClient.loginCourier(credentials);

        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена",
                response.jsonPath().getString("message"));
    }

    @Test
    public void shouldReturnErrorForNonExistingCourier() {
        CourierCredentials credentials = new CourierCredentials("not_existing_user", "123456");

        Response response = courierClient.loginCourier(credentials);

        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена",
                response.jsonPath().getString("message"));
    }
}