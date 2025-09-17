package com.example.qualitycontrolproject.WebController;


import com.example.qualitycontrolproject.Services.FilterService;
import com.example.qualitycontrolproject.entities.Filter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/FilterController")
//responsible for handling incoming HTTP requests and providing appropriate responses.
@CrossOrigin(origins = "*")
public class FilterController {
    private final FilterService filterService;


    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    //  Get all filters
    @GetMapping
    public ResponseEntity<List<Filter>> getFilters() {
        List<Filter> filters = filterService.getAllFilters();
        System.out.println("Données envoyées en JSON : " + filters); // Log amélioré
        return ResponseEntity.ok(filters);
    }
    //  Get a filter by ID
    @GetMapping("/{id}")
    public ResponseEntity<Filter> getFilterById(@PathVariable Long id) {
        return filterService.getFilterById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Get a filter by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Filter> getFilterByName(@PathVariable String name) {
        Filter filter = filterService.getFilterByName(name);
        return filter != null ? ResponseEntity.ok(filter) : ResponseEntity.notFound().build();
    }

    //  Search filters by query keyword
    @GetMapping("/search")
    public List<Filter> searchFiltersByQuery(@RequestParam String keyword) {
        return filterService.searchFiltersByQuery(keyword);
    }

    //  Create a new filter
    @PostMapping
    public Filter createFilter(@RequestBody Filter filter) {
        return filterService.createFilter(filter);
    }

    //  Update a filter
    @PutMapping("/{id}")
    public ResponseEntity<Filter> updateFilter(@PathVariable Long id, @RequestBody Filter updatedFilter) {
        Optional<Filter> result = filterService.updateFilter(id, updatedFilter);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Delete a filter by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        filterService.deleteFilter(id);
        return ResponseEntity.noContent().build();
    }
}
