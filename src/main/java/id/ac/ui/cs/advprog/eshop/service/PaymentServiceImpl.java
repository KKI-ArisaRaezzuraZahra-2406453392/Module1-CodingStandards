package id.ac.ui.cs.advprog.eshop.service;

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

        Payment payment = new Payment(paymentId, method, "SUCCESS", paymentData);

        if(paymentRepository.findById(payment.getId()) == null) {
            paymentRepository.save(payment);
            return payment;
        }
        return null;
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