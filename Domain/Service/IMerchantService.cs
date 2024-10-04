namespace MerchantOnboarding.Domain.Service;

public interface IMerchantService
{
    void Save(Merchant merchant);

    Task<List<Merchant>> Merchants();
    
    Task<Merchant> Get(Guid id);

}