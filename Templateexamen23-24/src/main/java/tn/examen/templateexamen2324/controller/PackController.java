package tn.examen.templateexamen2324.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.examen.templateexamen2324.entity.Pack;
import tn.examen.templateexamen2324.entity.Stand;
import tn.examen.templateexamen2324.services.IPackService;
import tn.examen.templateexamen2324.services.IStandService;

import java.util.List;

@RestController
@RequestMapping("/pack")
public class PackController {

    @Autowired
    IPackService packService;

    @GetMapping("/find-all-packs")
    @ResponseBody
    public List<Pack> getPacks() {
        List<Pack> listPack = packService.retrieveAllPacks();
        return listPack;
    }

    @PostMapping("/add-pack")
    @ResponseBody
    public Pack createPack(@RequestBody Pack b) {
        Pack pack = packService.addPack(b);
        return pack;
    }

    @DeleteMapping("/delete-pack/{id}")
    @ResponseBody
    public void deletePack(@PathVariable("id") int packId) {
        packService.deletePack(packId);
    }

    @PutMapping("/update-pack/{id}")
    @ResponseBody
    public Pack updateBloc(@PathVariable("id") int packId, @RequestBody Pack pack) {
        return packService.updatePack(packId, pack);

    }}
