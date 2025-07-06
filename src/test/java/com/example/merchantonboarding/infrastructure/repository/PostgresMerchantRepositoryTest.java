package com.example.merchantonboarding.infrastructure.repository;

import com.example.merchantonboarding.domain.Address;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.MerchantDocument;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresMerchantRepositoryTest {

    @Mock
    private JpaMerchantRepository jpaMerchantRepository;

    @InjectMocks
    private PostgresMerchantRepository postgresMerchantRepository;

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
        when(jpaMerchantRepository.save(any(Merchant.class))).thenReturn(merchant);

        postgresMerchantRepository.save(merchant);

        verify(jpaMerchantRepository, times(1)).save(merchant);
    }

    @Test
    void testFindById() {
        when(jpaMerchantRepository.findById(merchantId)).thenReturn(Optional.of(merchant));

        Merchant result = postgresMerchantRepository.findById(merchantId);

        assertNotNull(result);
        assertEquals(merchant, result);
        verify(jpaMerchantRepository, times(1)).findById(merchantId);
    }

    @Test
    void testFindByIdNotFound() {
        when(jpaMerchantRepository.findById(merchantId)).thenReturn(Optional.empty());

        Merchant result = postgresMerchantRepository.findById(merchantId);

        assertNull(result);
        verify(jpaMerchantRepository, times(1)).findById(merchantId);
    }

    @Test
    void testFindAll() {
        List<Merchant> merchants = Arrays.asList(merchant);
        when(jpaMerchantRepository.findAll()).thenReturn(merchants);

        List<Merchant> result = postgresMerchantRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(merchant, result.get(0));
        verify(jpaMerchantRepository, times(1)).findAll();
    }

    @Test
    void testFindAllEmpty() {
        when(jpaMerchantRepository.findAll()).thenReturn(Arrays.asList());

        List<Merchant> result = postgresMerchantRepository.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(jpaMerchantRepository, times(1)).findAll();
    }
}