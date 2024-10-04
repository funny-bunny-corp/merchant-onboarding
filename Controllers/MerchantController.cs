using MerchantOnboarding.Controllers.Dtos;
using MerchantOnboarding.Domain;
using MerchantOnboarding.Domain.Service;
using Microsoft.AspNetCore.Mvc;

namespace MerchantOnboarding.Controllers;

[ApiController]
[Route("api/merchants")]
public class MerchantController:ControllerBase
{
    private readonly ILogger<MerchantController> _logger;
    private readonly IMerchantService _merchantService;
    
    public MerchantController(ILogger<MerchantController> logger,IMerchantService merchantService)
    {
        this._logger = logger;
        this._merchantService = merchantService;
    }
    
    [HttpPost]
    public void CreateMerchant([FromBody]NewMerchant merchant)
    {
        this._merchantService.Save(merchant.ToMerchant());
    }
    
    [HttpGet]
    public Task<List<Merchant>> Merchants()
    {
        return this._merchantService.Merchants();
    }
    
    [HttpGet("{id}")]
    public Task<Merchant> GetMerchant(Guid id)
    {
        return this._merchantService.Get(id);
    }
    
}