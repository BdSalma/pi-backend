package tn.examen.truetech.services;

import org.springframework.web.multipart.MultipartFile;
import tn.examen.truetech.entity.Model;

import java.io.IOException;
import java.util.List;

public interface IModelService {
    Model createModel(Model model,Long phoneId, MultipartFile file) throws IOException;

    List<Model> retrieveModels();
    List<Model> retrieveModelByPhone(Long phoneId);

    Model getModelById(Long modelId);

    Model updateModel(Long modelId, Model updatedModel, MultipartFile file) throws IOException;

    void deleteModel(Long id);

}
