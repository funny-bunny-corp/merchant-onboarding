package com.example.merchantonboarding.infrastructure.client;

import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.repository.ShelfRepository;
import com.example.merchantonboarding.infrastructure.client.dto.CreateOwnerRequest;
import com.example.merchantonboarding.infrastructure.client.dto.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ShelfApiClient implements ShelfRepository {
    
    private final WebClient webClient;
    private final String shelfServiceUrl;
    
    @Autowired
    public ShelfApiClient(WebClient.Builder webClientBuilder, 
                          @Value("${app.shelf-service.url}") String shelfServiceUrl) {
        this.webClient = webClientBuilder.build();
        this.shelfServiceUrl = shelfServiceUrl;
    }
    
    @Override
    public void register(Merchant merchant) {
        CreateOwnerRequest request = new CreateOwnerRequest();
        request.setOwner(new OwnerDto(merchant.getId().toString()));
        
        Mono<Void> response = webClient.post()
                .uri(shelfServiceUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
        
        // Block to maintain synchronous behavior as in the original C# code
        response.block();
    }
}