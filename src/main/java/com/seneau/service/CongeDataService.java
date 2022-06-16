package com.seneau.service;

import com.netflix.discovery.converters.Auto;
import com.seneau.domain.CongeData;
import com.seneau.dto.CongeDataDto;
import com.seneau.helpers.interfaces.ExcelExtracterInterface;
import com.seneau.helpers.interfaces.FileExtracterInterface;
import com.seneau.repository.CongeDataRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CongeDataService {

    @Autowired
    private CongeDataRepository congeDataRepository;
    @Autowired
    private ExcelExtracterInterface excelExtracterInterface;
    @Autowired
    private FileExtracterInterface  fileExtracterInterface;


    public Object saveRecuperationByFile(MultipartFile file) throws IOException {
        val recuDTOs = excelExtracterInterface.getListCongeDataFromExcelFile(file.getInputStream());
        List<CongeData> congeDatas=new ArrayList<>();
        recuDTOs.forEach( recuDTO->{
            // System.out.println();
            if(recuDTO.getMatricule()!=null){
                CongeData congeData;
                Optional<CongeData> OptongeData = congeDataRepository.findByMatricule(recuDTO.getMatricule());
                if(!OptongeData.isPresent()){
                    congeData =new CongeData();
                    congeData.setMatricule(recuDTO.getMatricule());
                    congeData.setNbrJourRecup(recuDTO.getNbrJourRecup());
                    congeDataRepository.save(congeData);
                    congeDatas.add(congeData);
                }else if( OptongeData.get()!=null){

                    congeData= OptongeData.get();
                    congeData.setNbrJourRecup(recuDTO.getNbrJourRecup());
                    congeDataRepository.save(congeData);

                }

            }
        });

        return recuDTOs;
    }

    public List<CongeData> updateCongeDataByFile(MultipartFile file) throws IOException {
        val congeDatas= fileExtracterInterface.congeDataFiletoCongeData(file.getInputStream());
        List<CongeData> returnCDs= new ArrayList<>();
        congeDatas.forEach( cd ->{
            Optional<CongeData> optConge = congeDataRepository.findByMatricule(cd.getMatricule());
            if(optConge.isPresent()){
                CongeData newcd= optConge.get();
                newcd.setNbrJour(cd.getNbrJour());
                newcd.setDateDepart(cd.getDateDepart());
                newcd.setDateRetour(cd.getDateRetour());
                congeDataRepository.save(newcd);
                returnCDs.add(newcd);
            } else{
              CongeData result =  congeDataRepository.save(cd);
                returnCDs.add(result);
            }
        });
        return returnCDs;
    }
}
