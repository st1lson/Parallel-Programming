using lab8.client.Extensions;

const int rows = 500;
const int columns = 500;
const int numberOfThreads = 4;
const bool dataFromClient = true;
const string baseUrl = "http://localhost:8080/api/";

using var client = new HttpClient();

client.BaseAddress = new Uri(baseUrl);

var result = dataFromClient
    ? await client.SendDataAsync(rows, columns, numberOfThreads)
    : await client.UseServerDataAsync(rows, columns, numberOfThreads);

Console.WriteLine($"Duration: {result}ms");