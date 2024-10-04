using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MerchantOnboarding.Domain;

[ComplexType]
public class MerchantDocument
{
    [Required]
    public String Type { get; set; }
    
    [Required]
    public String Value { get; set; }
    
}