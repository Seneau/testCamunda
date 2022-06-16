package com.seneau.helpers.interfaces;

import com.seneau.dto.CongeDataDto;
import com.seneau.dto.CongeDataExtractDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface ExcelExtracterInterface {

    List<CongeDataDto> getListCongeDataFromExcelFile(InputStream is);

    boolean hasExcelFormat(MultipartFile file);
}
