using MerchantOnboarding.Domain.Repository;

namespace MerchantOnboarding.Domain.Service;

public class MerchantService:IMerchantService
{
    private readonly MerchantRepository _merchantRepository;

    private readonly ShelfRepository _shelfRepository;
    
    public MerchantService(MerchantRepository merchantRepository,ShelfRepository shelfRepository)
    {
        this._merchantRepository = merchantRepository;
        this._shelfRepository = shelfRepository;
    }
    
    public void Save(Merchant merchant)
    {
        this._merchantRepository.Save(merchant);
        this._shelfRepository.Register(merchant);
    }

    public Task<List<Merchant>> Merchants()
    {
        return Task.FromResult(this._merchantRepository.Merchants());
    }

    public Task<Merchant> Get(Guid id)
    {
        return Task.FromResult(this._merchantRepository.Get(id));
    }
}