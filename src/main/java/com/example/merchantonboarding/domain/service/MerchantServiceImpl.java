package com.example.merchantonboarding.domain.service;

import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.repository.MerchantRepository;
import com.example.merchantonboarding.domain.repository.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class MerchantServiceImpl implements MerchantService {
    
    private final MerchantRepository merchantRepository;
    private final ShelfRepository shelfRepository;
    
    @Autowired
    public MerchantServiceImpl(MerchantRepository merchantRepository, ShelfRepository shelfRepository) {
        this.merchantRepository = merchantRepository;
        this.shelfRepository = shelfRepository;
    }
    
    @Override
    public void save(Merchant merchant) {
        merchantRepository.save(merchant);
        shelfRepository.register(merchant);
    }
    
    @Override
    public CompletableFuture<List<Merchant>> findAllMerchants() {
        return CompletableFuture.completedFuture(merchantRepository.findAll());
    }
    
    @Override
    public CompletableFuture<Merchant> findMerchantById(UUID id) {
        return CompletableFuture.completedFuture(merchantRepository.findById(id));
    }
}