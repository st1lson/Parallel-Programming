namespace lab8.client.Models;

internal sealed record MultiplyMatrixRequest(int[,] FirstMatrix, int[,] SecondMatrix, int NumberOfThreads);
