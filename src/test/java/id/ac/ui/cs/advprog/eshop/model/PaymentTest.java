package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("VoucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testAddPaymentDefault() {
        Payment payment = new Payment("first-payment-1", "VoucherCode", "SUCCESS", this.paymentData);

        assertEquals("first-payment-1", payment.getId());
        assertEquals("VoucherCode", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("VoucherCode"));
    }

    @Test
    void testAddPaymentNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment(null, "VoucherCode",
                    "SUCCESS", this.paymentData);
        });
    }

    @Test
    void testAddPaymentNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("first-payment-1", null,
                    "SUCCESS", this.paymentData);
        });
    }

    @Test
    void testAddPaymentNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("first-payment-1", "VoucherCode",
                    "SUCCESS", null);
        });
    }

    @Test
    void testAddPaymentSuccessStatus() {
        Payment payment = new Payment("first-payment-1", "VoucherCode",
                "SUCCESS", this.paymentData);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testAddPaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("first-payment-1", "VoucherCode",
                    "MEOW", this.paymentData);
        });
    }

    @Test
    void testSetStatusToFailed() {
        Payment payment = new Payment("first-payment-1", "VoucherCode",
                "SUCCESS", this.paymentData);

        payment.setStatus("FAILED");
        assertEquals("FAILED", payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment("first-payment-1", "VoucherCode",
                "SUCCESS", this.paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}