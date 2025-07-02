package com.ecomarket.usuario.service;

import com.ecomarket.usuario.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductoClient {

    private final RestTemplate restTemplate;

    // URL base del microservicio Producto (ajusta según tu entorno)
    private static final String PRODUCTO_SERVICE_URL = "http://localhost:8082/api/productos";

    @Autowired
    public ProductoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        String url = PRODUCTO_SERVICE_URL + "/" + id;
        return restTemplate.getForObject(url, ProductoDTO.class);
    }
    
}