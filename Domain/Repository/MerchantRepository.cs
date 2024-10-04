namespace MerchantOnboarding.Domain.Repository;

public interface MerchantRepository
{
    void Save(Merchant merchant);
    
    Merchant Get(Guid id);
    
    List<Merchant> Merchants();
    
}