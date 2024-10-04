using System.Text.Json.Serialization;

namespace MerchantOnboarding.Adapters.Apis.Data;

public class Owner
{
    [JsonPropertyName("id")]
    public String Id { get; set; }
}