package com.seneau.helpers;

import com.seneau.dto.CongeDataDto;
import com.seneau.helpers.interfaces.ExcelExtracterInterface;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class ExcelExtracter implements ExcelExtracterInterface {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String SHEET = "fichierRecuperations";


    @Override
    public  boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }


    @Override
    public List<CongeDataDto> getListCongeDataFromExcelFile(InputStream is) {

        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<CongeDataDto> congeDatas = new ArrayList<>();

            Map<Integer,String> libelles = new HashMap<Integer, String>();
            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();



                Iterator<Cell> cellsInRow = currentRow.iterator();
                // skip header
                if (rowNumber == 0) {
                    int j = 0;
                    while(cellsInRow.hasNext()){

                        Cell currentCell = cellsInRow.next();

                        libelles.put(j, currentCell.getStringCellValue() );
                        j++;
                    }
                    rowNumber++;
                    continue;
                }
                CongeDataDto congeData = new CongeDataDto();

                for(int cellIdx=0; cellIdx < libelles.size(); cellIdx++){

                    Cell currentCell = currentRow.getCell(cellIdx);

                    if (currentCell == null) {
                        continue;
                    }

                    String type = getType(libelles.get(cellIdx));

                    Method setter;
                    switch (type) {
                        case "date":
                            try{
                                setter = Converter.getMethodOfClasse(libelles.get(cellIdx), CongeDataDto.class, Date.class);
                                if( currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    setter.invoke(congeData,currentCell.getDateCellValue());
                                }
                                break;
                            }catch (NoSuchMethodException e){
                                break;
                            }

                        case "int":
                            try{
                                setter = Converter.getMethodOfClasse(libelles.get(cellIdx), CongeDataDto.class, Integer.class);
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    setter.invoke(congeData,(int) currentCell.getNumericCellValue());
                                }
                                break;
                            }catch (NoSuchMethodException e){
                                break;
                            }

                        case "long":
                            try{
                                setter = Converter.getMethodOfClasse(libelles.get(cellIdx), CongeDataDto.class, Long.class);
                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                                    setter.invoke(congeData, (long) currentCell.getNumericCellValue());
                                }
                                break;
                            }
                            catch (NoSuchMethodException e){
                                break;
                            }



                        case "float":
                            try{
                                setter = Converter.getMethodOfClasse(libelles.get(cellIdx), CongeDataDto.class, Float.class);

                                if(currentCell.getCellTypeEnum() == CellType.NUMERIC){
                                    setter.invoke(congeData,(float) currentCell.getNumericCellValue());}
                                break;
                            }
                            catch (NoSuchMethodException e){
                                break;
                            }

                        default:
                            try{
                                if(currentCell.getCellTypeEnum() == CellType.STRING){
                                    setter = Converter.getMethodOfClasse(libelles.get(cellIdx), CongeDataDto.class, String.class);
                                    setter.invoke(congeData,currentCell.getStringCellValue());
                                }
                                break;
                            }
                            catch (NoSuchMethodException e){
                                break;
                            }
                    }
                }
                congeDatas.add(congeData);
            }

            workbook.close();

            return congeDatas;
        } catch (IOException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

    private String getType(String attribut){
        String type ;
        if( attribut.equals("matricule")
                || attribut.equals("nbrJourRecup")){
            type = "long";
        }else if(attribut.equals("dateEntree") || attribut.equals("dateNaissance") || attribut.equals("dateRetraite") || attribut.equals("dateEffect")){
            type = "date";
        }else if(attribut.equals("taux")){
            type = "float";
        }
        else if(attribut.equals("encadrement") || attribut.equals("mouvement")){
            type = "boolean";
        }
        else{
            type= "string";
        }

        return type;
    }
}
