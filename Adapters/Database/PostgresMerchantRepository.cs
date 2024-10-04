using MerchantOnboarding.Domain;
using MerchantOnboarding.Domain.Repository;
using MerchantOnboarding.Infrastructure;
using Npgsql;

namespace MerchantOnboarding.Adapters.Database;

public class PostgresMerchantRepository:MerchantRepository
{

    private readonly MerchantDbContext _context;
    
    public PostgresMerchantRepository(MerchantDbContext context)
    {
        this._context = context;
    }
    
    public void Save(Merchant merchant)
    {
        this._context.Merchants.Add(merchant);
        this._context.SaveChanges();
    }

    public Merchant Get(Guid id)
    {
        return this._context.Merchants.Find(id);
    }

    public List<Merchant> Merchants()
    {
        return this._context.Merchants.ToList();
    }
    
}