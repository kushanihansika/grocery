package com.example.grocery.service;

import com.example.grocery.entity.Recipe;
import com.example.grocery.repository.RecipesRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

@Service
public class DetailsService {

    @Autowired
    private RecipesRepository recipesRepository;
    public void saveCSVData(MultipartFile file) throws IOException, ParseException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);

            for (CSVRecord record : records) {
                Recipe recipes = new Recipe();

                recipes.setUrl(record.get("url"));
                recipes.setTitle(record.get("title"));
                recipes.setCook(record.get("cook"));
                recipes.setDescription(record.get("description"));
                recipes.setDirections(record.get("directions"));
                recipes.setImages(record.get("images"));
                recipes.setIngredients(record.get("ingredients"));
                recipes.setPrepTime(record.get("prep"));
                recipes.setServings(record.get("servings"));
                recipes.setTotalTime(record.get("total"));
                recipes.setMenuType(record.get("menu-type"));
                recipes.setType(record.get("type"));
                recipes.setHealthyDetails(record.get("healthy"));

                recipesRepository.save(recipes);
            }
        }
    }
}
