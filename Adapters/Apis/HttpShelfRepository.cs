using MerchantOnboarding.Adapters.Apis.Data;
using MerchantOnboarding.Domain;
using MerchantOnboarding.Domain.Repository;

namespace MerchantOnboarding.Adapters.Apis;

public class HttpShelfRepository:ShelfRepository
{
    private readonly IHttpClientFactory _httpClientFactory;
    
    private readonly IConfiguration _configuration;
    
    public HttpShelfRepository(IHttpClientFactory httpClientFactory,IConfiguration configuration)
    {
        this._httpClientFactory = httpClientFactory;
        this._configuration = configuration;
    }   

    public void Register(Merchant merchant)
    {
        ;
        var createOwner = new CreateOwner()
        {
            Owner = new Owner()
            {
                Id = merchant.Id.ToString(),
            }
        };
        var client = _httpClientFactory.CreateClient("ShelfClient");
        var response = client.PostAsJsonAsync(this._configuration.GetConnectionString("ShelfService"), createOwner).Result;
        response.EnsureSuccessStatusCode();
    }
}