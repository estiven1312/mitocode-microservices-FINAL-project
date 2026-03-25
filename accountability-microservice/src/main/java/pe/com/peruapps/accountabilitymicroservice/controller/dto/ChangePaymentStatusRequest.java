package pe.com.peruapps.accountabilitymicroservice.controller.dto;

import pe.com.peruapps.accountabilitymicroservice.model.Payment;

import java.io.Serializable;

public record ChangePaymentStatusRequest(Payment.Status status) implements Serializable {}
