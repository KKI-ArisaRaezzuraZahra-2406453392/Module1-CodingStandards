package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Builder
@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        if (id == null || method == null || paymentData == null) {
            throw new IllegalArgumentException();
        }

        String[] statusList = {"SUCCESS", "FAILED"};
        if (Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }

        this.id = id;
        this.method = method;
        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        String[] statusList = {"SUCCESS", "FAILED"};
        if (Arrays.stream(statusList).noneMatch(item -> (item.equals(status)))) {
            throw new IllegalArgumentException();
        } else {
            this.status = status;
        }
    }
}