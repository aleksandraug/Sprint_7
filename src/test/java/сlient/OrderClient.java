package сlient;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.CreateOrderRequest;

public class OrderClient extends BaseClient {

    private static final String ORDERS_PATH = "/api/v1/orders";
    private static final String ACCEPT_ORDER_PATH = "/api/v1/orders/accept";
    private static final String TRACK_ORDER_PATH = "/api/v1/orders/track";

    @Step("Создать заказ")
    public Response createOrder(CreateOrderRequest orderRequest) {
        return getBaseSpec()
                .body(orderRequest)
                .post(ORDERS_PATH);
    }

    @Step("Получить список заказов")
    public Response getOrdersList() {
        return getBaseSpec()
                .get(ORDERS_PATH);
    }

    @Step("Отменить заказ по track")
    public Response cancelOrder(int track) {
        return getBaseSpec()
                .queryParam("track", track)
                .put(ORDERS_PATH + "/cancel");
    }

    @Step("Принять заказ курьером")
    public Response acceptOrder(int orderId, int courierId) {
        return getBaseSpec()
                .queryParam("courierId", courierId)
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принять заказ без courierId")
    public Response acceptOrderWithoutCourierId(int orderId) {
        return getBaseSpec()
                .put(ACCEPT_ORDER_PATH + "/" + orderId);
    }

    @Step("Принять заказ без id заказа")
    public Response acceptOrderWithoutOrderId(int courierId) {
        return getBaseSpec()
                .queryParam("courierId", courierId)
                .put(ACCEPT_ORDER_PATH);
    }

    @Step("Получить заказ по track")
    public Response getOrderByTrack(int track) {
        return getBaseSpec()
                .queryParam("t", track)
                .get(TRACK_ORDER_PATH);
    }

    @Step("Получить заказ без track")
    public Response getOrderByTrackWithoutTrack() {
        return getBaseSpec()
                .get(TRACK_ORDER_PATH);
    }
}