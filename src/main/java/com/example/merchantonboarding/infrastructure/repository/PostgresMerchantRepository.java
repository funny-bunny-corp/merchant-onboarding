package com.example.merchantonboarding.infrastructure.repository;

import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PostgresMerchantRepository implements MerchantRepository {
    
    private final JpaMerchantRepository jpaMerchantRepository;
    
    @Autowired
    public PostgresMerchantRepository(JpaMerchantRepository jpaMerchantRepository) {
        this.jpaMerchantRepository = jpaMerchantRepository;
    }
    
    @Override
    public void save(Merchant merchant) {
        jpaMerchantRepository.save(merchant);
    }
    
    @Override
    public Merchant findById(UUID id) {
        return jpaMerchantRepository.findById(id).orElse(null);
    }
    
    @Override
    public List<Merchant> findAll() {
        return jpaMerchantRepository.findAll();
    }
}