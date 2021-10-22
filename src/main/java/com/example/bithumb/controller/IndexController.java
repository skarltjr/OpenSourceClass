package com.example.bithumb;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IndexController {
    private final BitService bitService;

    @GetMapping("/{kinds}/{current}")
    public ResponseEntity currentValue(@PathVariable String kinds, @PathVariable String current) {
        OrderDto result = bitService.getCurrent(kinds, current);
        Data data = bitService.computeTerm(result);
        return ResponseEntity.ok(data);
    }
}
