package com.example.merchantonboarding.domain.repository;

import com.example.merchantonboarding.domain.Merchant;
import java.util.List;
import java.util.UUID;

public interface MerchantRepository {
    
    void save(Merchant merchant);
    
    Merchant findById(UUID id);
    
    List<Merchant> findAll();
}