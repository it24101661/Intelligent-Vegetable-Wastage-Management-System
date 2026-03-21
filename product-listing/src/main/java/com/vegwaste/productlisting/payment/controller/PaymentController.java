package com.vegwaste.productlisting.payment.controller;

import com.vegwaste.productlisting.payment.dto.*;
import com.vegwaste.productlisting.payment.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        log.info("PaymentController->processPayment->started");
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }

    @GetMapping("/overview")
    public ResponseEntity<OverviewResponse> getOverview() {
        return ResponseEntity.ok(paymentService.getOverview());
    }

    @GetMapping("/customers/dashboard")
    public ResponseEntity<List<CustomerCardResponse>> getCustomerDashboardCards() {
        return ResponseEntity.ok(paymentService.getCustomerDashboardCards());
    }

    @GetMapping("/admin/requests")
    public ResponseEntity<List<AdminRequestResponse>> getAdminRequests() {
        return ResponseEntity.ok(paymentService.getAdminRequests());
    }

    @PostMapping("/admin/requests/{orderId}/approve")
    public ResponseEntity<Void> approveOrder(@PathVariable Long orderId) {
        paymentService.approveOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/requests/{orderId}/reject")
    public ResponseEntity<Void> rejectOrder(@PathVariable Long orderId) {
        paymentService.rejectOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/report")
    public ResponseEntity<String> downloadReport() {
        String csv = paymentService.generateAdminReportCsv();
        String filename = "payment-report-" + LocalDate.now() + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
