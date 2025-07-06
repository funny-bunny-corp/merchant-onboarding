package com.example.merchantonboarding.infrastructure.client;

import com.example.merchantonboarding.domain.Address;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.MerchantDocument;
import com.example.merchantonboarding.infrastructure.client.dto.CreateOwnerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelfApiClientTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private ShelfApiClient shelfApiClient;
    private Merchant merchant;
    private final String shelfServiceUrl = "http://localhost:8081/api/shelf";

    @BeforeEach
    void setUp() {
        WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.build()).thenReturn(webClient);
        
        shelfApiClient = new ShelfApiClient(webClientBuilder, shelfServiceUrl);
        
        UUID merchantId = UUID.randomUUID();
        LocalDateTime validUntil = LocalDateTime.now().plusYears(1);
        LocalDateTime startRelationship = LocalDateTime.now().plusDays(30);
        MerchantDocument document = new MerchantDocument("CPF", "12345678901");
        Address address = new Address("Main Street", "123", "Apt 1", "Downtown", "New York");
        
        merchant = new Merchant("Test Merchant", "test@example.com", validUntil, startRelationship, document, address);
        merchant.setId(merchantId);
    }

    @Test
    void testRegisterMerchant() {
        // Mock the WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(CreateOwnerRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Test the register method
        assertDoesNotThrow(() -> {
            shelfApiClient.register(merchant);
        });

        // Verify the WebClient chain was called
        verify(webClient, times(1)).post();
        verify(requestBodyUriSpec, times(1)).uri(shelfServiceUrl);
        verify(requestBodySpec, times(1)).bodyValue(any(CreateOwnerRequest.class));
        verify(requestBodySpec, times(1)).retrieve();
        verify(responseSpec, times(1)).bodyToMono(Void.class);
    }

    @Test
    void testRegisterMerchantWithException() {
        // Mock the WebClient chain to throw an exception
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(CreateOwnerRequest.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.error(new RuntimeException("Network error")));

        // Test that exception is propagated
        assertThrows(RuntimeException.class, () -> {
            shelfApiClient.register(merchant);
        });

        verify(webClient, times(1)).post();
    }
}