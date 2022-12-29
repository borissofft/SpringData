package com.example.cardealer.model.service;

import com.example.cardealer.model.entity.Part;

import java.io.IOException;
import java.util.Set;

public interface PartService {

    void seedParts() throws IOException;

    Set<Part> findRandomParts();

}
