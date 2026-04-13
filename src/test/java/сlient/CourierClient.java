package сlient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

public class CourierClient extends BaseClient {

    private static final String COURIER_PATH = "/api/v1/courier";
    private static final String LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создать курьера")
    public Response createCourier(Courier courier) {
        return getBaseSpec()
                .body(courier)
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return getBaseSpec()
                .body(credentials)
                .post(LOGIN_PATH);
    }

    @Step("Удалить курьера по id")
    public Response deleteCourier(int courierId) {
        return getBaseSpec()
                .delete(COURIER_PATH + "/" + courierId);
    }

    @Step("Удалить курьера без id")
    public Response deleteCourierWithoutId() {
        return getBaseSpec()
                .delete(COURIER_PATH);
    }
}