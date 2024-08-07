package tn.examen.truetech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.services.IPhoneService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/phone")
//@CrossOrigin(origins="http://localhost:4200")
public class PhoneController {

    @Autowired
    IPhoneService iPhoneService;


    @PostMapping("/add-phone")
    @ResponseBody
    public ResponseEntity<Phone> createPack(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) {
        try {
            Phone phone = new Phone();
            phone.setName(name);
            Phone createdPhone = iPhoneService.createPhone(phone, file);
            return new ResponseEntity<>(createdPhone, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/find-all-phones")
    @ResponseBody
    public List<Phone> getPacks() {
        List<Phone> listPhone = iPhoneService.retrievePhones();
        return listPhone;
    }

    @GetMapping("/find-phone/{phoneId}")
    @ResponseBody
    public Phone getPhoneById(@PathVariable("phoneId") long phoneId) {
        return  iPhoneService.retrievePhoneById(phoneId);
    }

    @PutMapping("/update-phone/{id}")
    @ResponseBody
    public ResponseEntity<Phone> updatePhone(@PathVariable("id") Long phoneId, @RequestParam("name") String name,
                             @RequestParam("file") MultipartFile file) {
        try {
            Phone phone = new Phone();
            phone.setName(name);
            Phone updatedPhone = iPhoneService.updatePhone(phoneId,phone, file);
            return new ResponseEntity<>(updatedPhone, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/delete-phone/{id}")
    @ResponseBody
    public void deletePhone(@PathVariable("id") Long phoneId) {
        iPhoneService.deletePhone(phoneId);
    }

}
