package com.seneau.helpers;

import com.seneau.domain.CongeData;
import com.seneau.dto.CongeDataDto;
import com.seneau.dto.CongeDataExtractDTO;
import com.seneau.helpers.interfaces.FileExtracterInterface;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileExtracter implements FileExtracterInterface {
    @Override
    public List<CongeData> congeDataFiletoCongeData(InputStream is) {
       // StringBuilder resultStrBuilder=new StringBuilder();

        List<CongeData> congeDataList= new ArrayList<>();
        try(BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(is))){
            String line;
            int numberLine =0;
            while((line =bufferedReader.readLine()) != null ){
                if(!line.contains("Matri")) {
                    String[] lineStr = line.split(";");
                    CongeDataExtractDTO congeDataDto= new CongeDataExtractDTO();
                    congeDataDto.setMatricule(Long.valueOf(lineStr[0]));
                    congeDataDto.setJrDateDepart(lineStr[1]);
                    congeDataDto.setMoisDateDepart(lineStr[2]);
                    congeDataDto.setAnDateDepart(lineStr[3]);
                    congeDataDto.setNbrJoursConge(Long.valueOf(lineStr[4]));
                    congeDataDto.setJrDateRetour(lineStr[5]);
                    congeDataDto.setMoisDateRetour(lineStr[6]);
                    congeDataDto.setAnDateRetour(lineStr[7]);
                    congeDataList.add(CongeDataExtractDTO.toEntity(congeDataDto));
                    numberLine++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return congeDataList;
    }
}
