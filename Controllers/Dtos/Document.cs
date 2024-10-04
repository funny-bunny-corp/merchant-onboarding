using System.ComponentModel.DataAnnotations;

namespace MerchantOnboarding.Controllers.Dtos;

public class Document
{
    [Required]
    public String Type { get; set; }
    
    [Required]
    public String Value { get; set; }
    
}