using System.ComponentModel.DataAnnotations;
using MerchantOnboarding.Domain;

namespace MerchantOnboarding.Controllers.Dtos;

public class NewMerchant
{
    [Required]
    public Document Document { get; set; }
    [Required]
    public String Name { get; set; }
    public String Email { get; set; }
    [Required]
    public DateTime ValidUntil { get; set; }
    [Required]
    public DateTime StartRelationship { get; set; }
    public Address Address { get; set; }

    public Merchant ToMerchant()
    {
        return new Merchant
        {
            Document = new MerchantDocument()
            {
                Type = this.Document.Type,
                Value = this.Document.Value
            },
            Name = this.Name,
            Email = this.Email,
            ValidUntil = this.ValidUntil,
            StartRelationship = this.StartRelationship,
            Address = new Domain.Address{
                Street = this.Address.Street,
                Number = this.Address.Number,
                Complement = this.Address.Complement,
                City = this.Address.City,
                Neighborhood = this.Address.Neighborhood
            }
        };
    }


}