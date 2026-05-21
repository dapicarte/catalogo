package BookPoint.catalogo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import BookPoint.catalogo.service.CatalogoService;

@RestController
@RequestMapping("api/catalgo")
public class CatalogoController {
    @Autowired
    private CatalogoService catalogoService;

    @PostMapping()
    public ResponseEntity<>
}
