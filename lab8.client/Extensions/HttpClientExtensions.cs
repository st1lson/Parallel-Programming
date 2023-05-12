using lab8.client.Models;
using Microsoft.AspNetCore.WebUtilities;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using System.Diagnostics;
using System.Text;

namespace lab8.client.Extensions;

internal static class HttpClientExtensions
{
    internal static async Task<long> SendDataAsync(this HttpClient client, int rows, int columns, int numberOfThreads)
    {
        var watch = Stopwatch.StartNew();

        var firstMatrix = GenerateMatrix(rows, columns);
        var secondMatrix = GenerateMatrix(rows, columns);

        var request = new MultiplyMatrixRequest(firstMatrix, secondMatrix, numberOfThreads);
        var json = JsonConvert.SerializeObject(request, new JsonSerializerSettings { ContractResolver = new CamelCasePropertyNamesContractResolver() });
        var body = new StringContent(json, Encoding.UTF8, "application/json");

        await client.PostAsync("multiply", body);

        watch.Stop();

        return watch.ElapsedMilliseconds;
    }

    internal static async Task<long> UseServerDataAsync(this HttpClient client, int rows, int columns, int numberOfThreads)
    {
        var watch = Stopwatch.StartNew();

        var query = new Dictionary<string, string>
        {
            [nameof(rows)] = rows.ToString(),
            [nameof(columns)] = columns.ToString(),
            [nameof(numberOfThreads)] = numberOfThreads.ToString()
        };

        await client.GetAsync(QueryHelpers.AddQueryString("multiply", query));

        watch.Stop();

        return watch.ElapsedMilliseconds;
    }

    private static int[,] GenerateMatrix(int rows, int columns)
    {
        var matrix = new int[rows, columns];

        var random = new Random();
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                matrix[i, j] = random.Next(10);
            }
        }

        return matrix;
    }
}
