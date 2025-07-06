package com.example.merchantonboarding.domain.service;

import com.example.merchantonboarding.domain.Address;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.MerchantDocument;
import com.example.merchantonboarding.domain.repository.MerchantRepository;
import com.example.merchantonboarding.domain.repository.ShelfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceImplTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private ShelfRepository shelfRepository;

    @InjectMocks
    private MerchantServiceImpl merchantService;

    private Merchant merchant;
    private UUID merchantId;

    @BeforeEach
    void setUp() {
        merchantId = UUID.randomUUID();
        LocalDateTime validUntil = LocalDateTime.now().plusYears(1);
        LocalDateTime startRelationship = LocalDateTime.now().plusDays(30);
        MerchantDocument document = new MerchantDocument("CPF", "12345678901");
        Address address = new Address("Main Street", "123", "Apt 1", "Downtown", "New York");
        
        merchant = new Merchant("Test Merchant", "test@example.com", validUntil, startRelationship, document, address);
        merchant.setId(merchantId);
    }

    @Test
    void testSaveMerchant() {
        doNothing().when(merchantRepository).save(any(Merchant.class));
        doNothing().when(shelfRepository).register(any(Merchant.class));

        merchantService.save(merchant);

        verify(merchantRepository, times(1)).save(merchant);
        verify(shelfRepository, times(1)).register(merchant);
    }

    @Test
    void testFindAllMerchants() {
        List<Merchant> merchants = Arrays.asList(merchant);
        when(merchantRepository.findAll()).thenReturn(merchants);

        CompletableFuture<List<Merchant>> result = merchantService.findAllMerchants();

        assertNotNull(result);
        List<Merchant> resultList = result.join();
        assertEquals(1, resultList.size());
        assertEquals(merchant, resultList.get(0));
        verify(merchantRepository, times(1)).findAll();
    }

    @Test
    void testFindMerchantById() {
        when(merchantRepository.findById(merchantId)).thenReturn(merchant);

        CompletableFuture<Merchant> result = merchantService.findMerchantById(merchantId);

        assertNotNull(result);
        Merchant resultMerchant = result.join();
        assertEquals(merchant, resultMerchant);
        verify(merchantRepository, times(1)).findById(merchantId);
    }

    @Test
    void testFindMerchantByIdNotFound() {
        when(merchantRepository.findById(merchantId)).thenReturn(null);

        CompletableFuture<Merchant> result = merchantService.findMerchantById(merchantId);

        assertNotNull(result);
        Merchant resultMerchant = result.join();
        assertNull(resultMerchant);
        verify(merchantRepository, times(1)).findById(merchantId);
    }

    @Test
    void testSaveMerchantWithException() {
        doThrow(new RuntimeException("Database error")).when(merchantRepository).save(any(Merchant.class));

        assertThrows(RuntimeException.class, () -> {
            merchantService.save(merchant);
        });

        verify(merchantRepository, times(1)).save(merchant);
        // Shelf repository should not be called if merchant repository fails
        verify(shelfRepository, never()).register(any(Merchant.class));
    }
}