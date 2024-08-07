package tn.examen.truetech.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.repository.PhoneRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service

public class PhoneService implements IPhoneService {
    @Autowired
    PhoneRepository phoneRepository;

    private static final String uploadPath = "C:/Users/user/Documents/GitHub/test/Templateexamen23-24/src/main/resources/fils/phones";

    @Override
    public Phone createPhone(Phone phone, MultipartFile file) throws IOException {
        Phone savedPhone = phoneRepository.save(phone);

        // Ensure the upload directory exists
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Save the file to the server
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, savedPhone.getIdPhone() + "_" + fileName);
        Files.write(filePath, file.getBytes());

        // Update the Phone entity with the file path
        savedPhone.setImage(filePath.toString());
        return phoneRepository.save(savedPhone);
    }

    @Override
    public List<Phone> retrievePhones() {
        return phoneRepository.findAll();
    }


    @Override
    public Phone updatePhone(Long phoneId, Phone updatedPhone, MultipartFile file) throws IOException {


        Optional<Phone> existingPhoneOpt = phoneRepository.findById(phoneId);

        if (existingPhoneOpt.isPresent()) {

            Phone existingPhone = existingPhoneOpt.get();
            existingPhone.setName(updatedPhone.getName());
            existingPhone.setModels(updatedPhone.getModels());
            // Check if a new file is provided
            if (file != null && !file.isEmpty()) {

                // Delete the old file if it exists
                if (existingPhone.getImage() != null) {
                    File oldFile = new File(existingPhone.getImage());
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // Ensure the upload directory exists
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Save the new file to the server
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadPath, existingPhone.getIdPhone() + "_" + fileName);
                Files.write(filePath, file.getBytes());

                // Update the Phone entity with the new file path
                existingPhone.setImage(filePath.toString());
            } else {
                // If no new file is provided, retain the existing image path
                existingPhone.setImage(updatedPhone.getImage());
            }

            return phoneRepository.save(existingPhone);
        } else {
            throw new EntityNotFoundException("Phone with id " + phoneId + " not found");
        }
    }

    @Override
    public Phone retrievePhoneById(long id) {
        return phoneRepository.findById(id).get();
    }

    @Override
    public void deletePhone(long id) {
        phoneRepository.deleteById(id);
    }



    public void saveFile(MultipartFile multipartFile, String fileName) throws IOException {
        Path upload = Paths.get(uploadPath);
        if(!Files.exists(upload)){
            Files.createDirectories(upload);
        }
        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = upload.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not save file");
        }
    }

    public String nameFile(MultipartFile multipartFile){
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Integer fileDotIndex = originalFileName.lastIndexOf('.');
        String fileExtension = originalFileName.substring(fileDotIndex);
        return UUID.randomUUID().toString() + fileExtension;
    }

    public void deleteFile(String fileName) throws IOException{
        Path upload = Paths.get(uploadPath);
        Path filePath = upload.resolve(fileName);
        Files.deleteIfExists(filePath);
        System.out.println(fileName);
        System.out.println("deleted");
    }

}
