package tn.examen.truetech.controller;

import com.google.zxing.qrcode.decoder.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Model;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.services.IModelService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/model")
//@CrossOrigin(origins="http://localhost:4200")
public class ModelController {
    @Autowired
    IModelService modelService;




    @PostMapping("{phoneId}/add-model")
    @ResponseBody
    public ResponseEntity<Model> createModel(@RequestParam("name") String name,
                                             @RequestParam("file") MultipartFile file, @PathVariable("phoneId") Long phoneId) {

        try {
            Model model = new Model();
            model.setName(name);
            Model createdModel = modelService.createModel(model,phoneId ,file);
            return new ResponseEntity<>(createdModel, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/find-all-models")
    @ResponseBody
    public List<Model> getModels() {
        List<Model> listModel = modelService.retrieveModels();
        return listModel;
    }

    @GetMapping("/find-models/{id}")
    @ResponseBody
    public List<Model> getModelByPhoneName(@PathVariable("id") Long phoneId ) {
        List<Model> listModel = modelService.retrieveModelByPhone(phoneId);
        return listModel;
    }

    @GetMapping("/find-model/{modelId}")
    @ResponseBody
    public Model getModelById(@PathVariable("modelId") long modelId) {
        return  modelService.getModelById(modelId);
    }

    @PutMapping("/update-model/{id}")
    @ResponseBody
    public ResponseEntity<Model> updateModel(@PathVariable("id") Long modelId, @RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file) {
        try {
            Model model = new Model();
            model.setName(name);
            Model updatedModel = modelService.updateModel(modelId,model, file);
            return new ResponseEntity<>(updatedModel, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete-model/{id}")
    @ResponseBody
    public void deleteModel(@PathVariable("id") Long modelId) {
        modelService.deleteModel(modelId);
    }
}
