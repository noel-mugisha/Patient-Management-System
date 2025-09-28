package com.demo.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    // Constructor
    public BillingServiceGrpcClient (
            @Value("${billing.service.address}") String serverAddress,
            @Value("${billing.service.grpc.port}") int serverPort
    ) {
        log.info("Connecting to Billing Service GRPC at {}:{}",serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,serverPort)
                .usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount (String patientId, String name, String email) {
        var request = BillingRequest.newBuilder().setPatientId(patientId)
                .setName(name)
                .setEmail(email).build();
        var response = blockingStub.createBillingAccount(request);
        log.info("Received response from billing service: {}", response);
        return response;
    }
}
