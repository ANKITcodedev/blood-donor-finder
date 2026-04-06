package com.ankit.BloodDonorFinder.exception;

public class BadRequestException
        extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}