package com.project.demo.rest.form;

import com.project.demo.logic.entity.form.Form;
import com.project.demo.logic.entity.form.FormRepository;
import com.project.demo.logic.entity.gameresult.GameResult;
import com.project.demo.logic.entity.gameresult.GameResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms")
public class FormRestController {
    @Autowired
    private FormRepository formRepository;
    @GetMapping
    public List<Form> GetAllForms() {
        return formRepository.findAll();
    }
    @PostMapping
    public Form addForm(@RequestBody Form form) {
        return formRepository.save(form);
    }
    @GetMapping("/{id}")
    public Form getFormbyId(@PathVariable Long id) {
        return formRepository.findById(id).orElseThrow(RuntimeException::new);
    }
    @DeleteMapping("/{id}")
    public void deleteForms(@PathVariable Long id) {
        formRepository.deleteById(id);
    }
}