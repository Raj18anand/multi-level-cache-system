package org.codebook.multilevelcachesystem.controller;

import org.codebook.multilevelcachesystem.service.CacheService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public class CacheController {
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    // get mapping for getting the key
    @GetMapping("/get/{key}")
    public String get(@PathVariable String key) {
        return cacheService.get(key);
    }

    // post mapping for setting the key
    @PostMapping("/{key}")
    public void post(@PathVariable String key, @RequestBody String value){
        cacheService.set(key, value);
    }

    // delete mapping for deleting the key
    @DeleteMapping("/{key}")
    public void delete(@PathVariable String key){
        cacheService.remove(key);
    }
}
