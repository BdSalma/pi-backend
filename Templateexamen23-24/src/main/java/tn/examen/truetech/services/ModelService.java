package tn.examen.truetech.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Model;
import tn.examen.truetech.entity.Phone;
import tn.examen.truetech.repository.ModelRepository;
import tn.examen.truetech.repository.PhoneRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ModelService implements IModelService {

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    PhoneRepository phoneRepository;

    private static final String uploadPath = "C:/Users/user/Documents/GitHub/test/Templateexamen23-24/src/main/resources/fils/models";

    @Override
    public Model createModel(Model model,Long phoneId, MultipartFile file) throws IOException {
        Optional<Phone> OptPhone = phoneRepository.findById(phoneId);
        Model savedModel = modelRepository.save(model);
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        if (OptPhone.isPresent()) {
            Phone phone = OptPhone.get();

            // Save the file to the server
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, savedModel.getIdModel() + "_" + fileName);
            Files.write(filePath, file.getBytes());

            savedModel.setPhone(phone);
            phone.getModels().add(savedModel);
            savedModel.setImage(filePath.toString());
            return modelRepository.save(savedModel);
        }else {
            throw new RuntimeException("Phone not found with id: " + phoneId);
        }
    }

    @Override
    public List<Model> retrieveModels() {
        return modelRepository.findAll();
    }

    @Override
    public List<Model> retrieveModelByPhone(Long phoneId) {
        return modelRepository.findByPhoneIdPhone(phoneId);
    }

    @Override
    public Model getModelById(Long modelId) {
        return modelRepository.findById(modelId).get();
    }

    @Override
    public Model updateModel(Long modelId, Model updatedModel, MultipartFile file) throws IOException {
        Optional<Model> existingModelOpt = modelRepository.findById(modelId);

        if (existingModelOpt.isPresent()) {

            Model existingModel = existingModelOpt.get();
            existingModel.setName(updatedModel.getName());
            // Check if a new file is provided
            if (file != null && !file.isEmpty()) {

                // Delete the old file if it exists
                if (existingModel.getImage() != null) {
                    File oldFile = new File(existingModel.getImage());
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
                Path filePath = Paths.get(uploadPath, existingModel.getIdModel() + "_" + fileName);
                Files.write(filePath, file.getBytes());

                // Update the Phone entity with the new file path
                existingModel.setImage(filePath.toString());
            } else {
                // If no new file is provided, retain the existing image path
                existingModel.setImage(updatedModel.getImage());
            }

            return modelRepository.save(existingModel);
        } else {
            throw new EntityNotFoundException("Model with id " + modelId + " not found");
        }
    }

    @Override
    public void deleteModel(Long id) {
    modelRepository.deleteById(id);
    }


}
