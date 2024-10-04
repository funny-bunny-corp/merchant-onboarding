using System.Text.Json;
using System.Text.Json.Serialization;

namespace MerchantOnboarding.Adapters.Apis.Data;

public class CreateOwner
{
    [JsonPropertyName("owner")]
    public Owner Owner { get; set; }
}