using MerchantOnboarding.Adapters.Apis.Data;
using MerchantOnboarding.Domain;
using MerchantOnboarding.Domain.Repository;

namespace MerchantOnboarding.Adapters.Apis;

public class HttpShelfRepository:ShelfRepository
{
    private readonly IHttpClientFactory _httpClientFactory;
    
    public HttpShelfRepository(IHttpClientFactory httpClientFactory)
    {
        _httpClientFactory = httpClientFactory;
    }

    public void Register(Merchant merchant)
    {
        var createOwner = new CreateOwner()
        {
            Owner = new Owner()
            {
                Id = merchant.Id.ToString(),
            }
        };
        var client = _httpClientFactory.CreateClient("ShelfClient");
        var response = client.PostAsJsonAsync("http://localhost:8089/shelves", createOwner).Result;
        response.EnsureSuccessStatusCode();
    }
}