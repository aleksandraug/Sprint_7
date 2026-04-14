package util;

import model.Courier;
import model.CreateOrderRequest;

import java.util.List;
import java.util.UUID;

public class TestData {

    public static Courier generateCourier() {
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        return new Courier(
                "login_" + random,
                "pass_" + random,
                "name_" + random
        );
    }

    public static CreateOrderRequest generateOrder(List<String> colors) {
        return new CreateOrderRequest(
                "Виктор",
                "Тестов",
                "Москва, ул. Пушкина, д. 1",
                "Черкизовская",
                "+79999999999",
                5,
                "2026-04-20",
                "Тестовый заказ",
                colors
        );
    }
}