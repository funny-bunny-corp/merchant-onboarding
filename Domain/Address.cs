using System.ComponentModel.DataAnnotations.Schema;

namespace MerchantOnboarding.Domain;

[ComplexType]
public class Address
{
    public String Street { get; set; }
    public String Number { get; set; }
    public String Complement { get; set; }
    public String Neighborhood { get; set; }
    public String City { get; set; }
    
    public Address(string street, string number, string complement, string neighborhood, string city)
    {
        Street = street;
        Number = number;
        Complement = complement;
        Neighborhood = neighborhood;
        City = city;
    }
}