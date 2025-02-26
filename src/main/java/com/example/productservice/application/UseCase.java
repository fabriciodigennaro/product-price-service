package com.example.productservice.application;

public interface UseCase<REQ, RES> {
    RES execute(REQ request);
}
