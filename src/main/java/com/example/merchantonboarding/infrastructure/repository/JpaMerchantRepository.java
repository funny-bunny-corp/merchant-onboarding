package com.example.merchantonboarding.infrastructure.repository;

import com.example.merchantonboarding.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaMerchantRepository extends JpaRepository<Merchant, UUID> {
}