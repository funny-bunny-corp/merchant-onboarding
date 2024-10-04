using MerchantOnboarding.Domain;
using Microsoft.EntityFrameworkCore;

namespace MerchantOnboarding.Infrastructure;

public class MerchantDbContext:DbContext
{
    public MerchantDbContext(DbContextOptions<MerchantDbContext> options):base(options)
    {
        
    }
    
    public DbSet<Merchant> Merchants { get; set; }
    
}