package com.example.lab8.Controllers;

import com.example.lab8.Models.Matrix;
import com.example.lab8.Models.MultiplyRequest;
import com.example.lab8.Models.Result;
import com.example.lab8.Services.MultiplyService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/multiply")
    public ResponseEntity<Result> multiply(@NotNull @RequestBody MultiplyRequest request) {
        var firstMatrix = new Matrix(request.firstMatrix());
        var secondMatrix = new Matrix(request.secondMatrix());

        var multiplyService = new MultiplyService(firstMatrix, secondMatrix);
        var result = multiplyService.solve(request.numberOfThreads());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/multiply")
    public ResponseEntity<Result> randomMultiply(@RequestParam int rows, @RequestParam int columns,  @RequestParam int numberOfThreads) {
        var firstMatrix = new Matrix(rows, columns);
        var secondMatrix = new Matrix(rows, columns);

        var multiplyService = new MultiplyService(firstMatrix, secondMatrix);
        var result = multiplyService.solve(numberOfThreads);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/generate-matrix")
    public @ResponseBody ResponseEntity<int[][]> multiply(@RequestParam int rows, @RequestParam int columns, @RequestParam int seed) {
        var matrix = new Matrix(rows, columns, seed);

        return ResponseEntity.ok(matrix.getMatrix());
    }
}
