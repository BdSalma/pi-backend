package tn.examen.truetech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Model;
import tn.examen.truetech.entity.Option;
import tn.examen.truetech.services.IOptionService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/option")
//@CrossOrigin(origins="http://localhost:4200")
public class OptionController {
    @Autowired
    IOptionService optionService;

    @PostMapping("{modelId}/add-option")
    @ResponseBody
    public ResponseEntity<Option> createOption(@RequestParam("title") String title,
                                               @RequestParam("description") String description,
                                               @RequestParam("clientPrice") Long clientPrice,
                                               @RequestParam("supplierPrice") Long supplierPrice,
                                               @RequestParam("quantity") int quantity,
                                               @RequestParam("file") MultipartFile file,
                                               @PathVariable("modelId") Long modelId) {

        try {
            Option option = new Option();
            option.setTitle(title);
            option.setDescription(description);
            option.setSupplierPrice(supplierPrice);
            option.setClientPrice(clientPrice);
            option.setQuantity(quantity);

            Option createdOption = optionService.createOption(option,modelId ,file);
            return new ResponseEntity<>(createdOption, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/find-all-options")
    @ResponseBody
    public List<Option> getOptions() {
        List<Option> listOption = optionService.retrieveOptions();
        return listOption;
    }

    @GetMapping("/find-options/{id}")
    @ResponseBody
    public List<Option> getOptionByModel(@PathVariable("id") Long modelId ) {
        List<Option> listOption = optionService.retrieveOptionByModel(modelId);
        return listOption;
    }

    @GetMapping("/find-option/{optionId}")
    @ResponseBody
    public Option getOptionById(@PathVariable("optionId") long optionId) {
        return  optionService.getOptionById(optionId);
    }

    @PutMapping("/update-option/{id}")
    @ResponseBody
    public ResponseEntity<Option> updateOption(@PathVariable("id") Long optionId, @RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("clientPrice") Long clientPrice,
                               @RequestParam("supplierPrice") Long supplierPrice,
                               @RequestParam("quantity") int quantity,
                               @RequestParam("file") MultipartFile file) {

        try {
            Option option = new Option();
            option.setTitle(title);
            option.setDescription(description);
            option.setSupplierPrice(supplierPrice);
            option.setClientPrice(clientPrice);
            option.setQuantity(quantity);

            Option updatedOption = optionService.updateOption(optionId,option, file);
            return new ResponseEntity<>(updatedOption, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/delete-option/{id}")
    @ResponseBody
    public void deleteOption(@PathVariable("id") Long optionid) {
        optionService.deleteOption(optionid);
    }
}
