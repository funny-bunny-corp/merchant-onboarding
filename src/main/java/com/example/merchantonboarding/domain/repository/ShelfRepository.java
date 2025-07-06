package com.example.merchantonboarding.domain.repository;

import com.example.merchantonboarding.domain.Merchant;

public interface ShelfRepository {
    
    void register(Merchant merchant);
}