

using System.ComponentModel.DataAnnotations;

namespace MerchantOnboarding.Domain;

public class Merchant
{
    [Key]
    public Guid Id { get; set; }
    public String Name { get; set; }
    public String Email { get; set; }
    public DateTime ValidUntil { get; set; }
    public DateTime StartRelationship { get; set; }
    public required MerchantDocument Document { get; set; }
    public DateTime RegistrationDate { get; set; }
    public required Address Address { get; set; }


}