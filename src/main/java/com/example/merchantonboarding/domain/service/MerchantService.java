package com.example.merchantonboarding.domain.service;

import com.example.merchantonboarding.domain.Merchant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface MerchantService {
    
    void save(Merchant merchant);
    
    CompletableFuture<List<Merchant>> findAllMerchants();
    
    CompletableFuture<Merchant> findMerchantById(UUID id);
}