package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
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
        Payment payment = new Payment("first-payment-1", "VoucherCode", PaymentStatus.SUCCESS.getValue(), this.paymentData);

        assertEquals("first-payment-1", payment.getId());
        assertEquals("VoucherCode", payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
        assertEquals("ESHOP1234ABC5678", payment.getPaymentData().get("VoucherCode"));
    }

    @Test
    void testAddPaymentNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment(null, "VoucherCode",
                    PaymentStatus.SUCCESS.getValue(), this.paymentData);
        });
    }

    @Test
    void testAddPaymentNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("first-payment-1", null,
                    PaymentStatus.SUCCESS.getValue(), this.paymentData);
        });
    }

    @Test
    void testAddPaymentNullPaymentData() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("first-payment-1", "VoucherCode",
                    PaymentStatus.SUCCESS.getValue(), null);
        });
    }

    @Test
    void testAddPaymentSuccessStatus() {
        Payment payment = new Payment("first-payment-1", "VoucherCode",
                PaymentStatus.SUCCESS.getValue(), this.paymentData);

        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
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
                PaymentStatus.SUCCESS.getValue(), this.paymentData);

        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment("first-payment-1", "VoucherCode",
                PaymentStatus.SUCCESS.getValue(), this.paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}