package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        String paymentId = UUID.randomUUID().toString();
        String status = PaymentStatus.REJECTED.getValue();

        if (method.equals("VoucherCode")) {
            String voucherCode = paymentData.get("voucherCode");
            if (isValidVoucherCode(voucherCode)) {
                status = PaymentStatus.SUCCESS.getValue();
            }
        } else if (method.equals("BankTransfer")) {
            if (isValidBankTransfer(paymentData)) {
                status = PaymentStatus.SUCCESS.getValue();
            }
        }

        Payment payment = new Payment(paymentId, method, status, paymentData);
        return paymentRepository.save(payment);
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16 || !voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        return digitCount == 8;
    }

    private boolean isValidBankTransfer(Map<String, String> paymentData) {
        String bankName = paymentData.get("bankName");
        String referenceNumber = paymentData.get("referenceNumber");

        return bankName != null && !bankName.isBlank() &&
                referenceNumber != null && !referenceNumber.isBlank();
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        Payment currentPayment = paymentRepository.findById(payment.getId());
        if(currentPayment != null) {
            currentPayment.setStatus(status);
            paymentRepository.save(currentPayment);
            return currentPayment;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public List<Payment> getAllPayment() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }
}