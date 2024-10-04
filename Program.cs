using MerchantOnboarding.Adapters.Apis;
using MerchantOnboarding.Adapters.Database;
using MerchantOnboarding.Domain.Repository;
using MerchantOnboarding.Domain.Service;
using MerchantOnboarding.Infrastructure;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen(options =>
{
    options.CustomSchemaIds(type => type.ToString());
});
builder.AddNpgsqlDbContext<MerchantDbContext>("db", static settings => settings.ConnectionString = "Host=localhost;Port=5433;Database=onboarding;Username=onboarding;Password=onboarding;");

builder.Services.AddScoped<MerchantRepository, PostgresMerchantRepository>();
builder.Services.AddScoped<IMerchantService,MerchantService>();
builder.Services.AddScoped<ShelfRepository,HttpShelfRepository>();
builder.Services.AddHttpClient();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();