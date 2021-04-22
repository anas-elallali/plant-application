package ma.ump.plant.web.rest;

import ma.ump.plant.domain.EcologicalStatus;
import ma.ump.plant.domain.Family;
import ma.ump.plant.domain.Plant;
import ma.ump.plant.repository.EcologicalStatusRepository;
import ma.ump.plant.repository.FamilyRepository;

import ma.ump.plant.repository.PlantRepository;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


/**
 * REST controller for managing {@link Family}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ExcelResource {

    private final Logger log = LoggerFactory.getLogger(ExcelResource.class);

    @Autowired
    private FamilyRepository familyRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private EcologicalStatusRepository ecologicalStatusRepository;

    @PostMapping("/import")
    public void mapReapExcelDatatoDB(@RequestParam("file") MultipartFile reapExcelDataFile) throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for(int i = 2; i < worksheet.getPhysicalNumberOfRows(); i++) {

            XSSFRow row = worksheet.getRow(i);
            String familyNameString = row.getCell(0).getStringCellValue();
            if(familyNameString != null && familyNameString.trim().length() > 0) {

                Optional<Family> familyOp = familyRepository.findTop1ByName(familyNameString.trim());
                Family family = null;
                if(familyOp.isPresent()){
                    family = familyOp.get();
                }
                else{
                    family = familyRepository.save(new Family().name(familyNameString));
                }
                log.debug("AEL "+row.getCell(0).getStringCellValue());
                log.debug("AEL "+row.getCell(1).getStringCellValue());
                log.debug("AEL "+row.getCell(2).getStringCellValue());
                log.debug("AEL "+row.getCell(3).getStringCellValue());
                log.debug("AEL "+row.getCell(4).getStringCellValue());
                log.debug("AEL "+row.getCell(5).getStringCellValue());
                log.debug("AEL "+row.getCell(6).getStringCellValue());
                log.debug("AEL "+row.getCell(7).getStringCellValue());
                log.debug("AEL "+row.getCell(8).getStringCellValue());
                log.debug("AEL "+row.getCell(9).getStringCellValue());
                log.debug("AEL "+row.getCell(10).getStringCellValue());
                log.debug("AEL "+row.getCell(11).getStringCellValue());
                log.debug("AEL "+row.getCell(12).getStringCellValue());

                Plant plant = new Plant();
                plant.scientificName(row.getCell(1).getStringCellValue())
                    .family(family)
                    .synonym(row.getCell(2).getStringCellValue())
                    .localName(row.getCell(3).getStringCellValue())
                    .englishName(row.getCell(4).getStringCellValue())
                    .voucherNumber(row.getCell(5).getStringCellValue())
                    .botanicalDescription(row.getCell(7).getStringCellValue())
                    .therapeuticUses(row.getCell(8).getStringCellValue())
                    .usedParts(row.getCell(9).getStringCellValue())
                    .preparation(row.getCell(10).getStringCellValue())
                    .pharmacologicalActivities(row.getCell(11).getStringCellValue())
                    .majorPhytochemicals(row.getCell(12).getStringCellValue());

                String ecologicalStatusString = row.getCell(6).getStringCellValue() ;

                if(ecologicalStatusString != null && ecologicalStatusString.trim().length() > 0){
                    Optional<EcologicalStatus> ecologicalStatusOp = ecologicalStatusRepository.findTop1ByName(ecologicalStatusString.trim());
                    if(ecologicalStatusOp.isPresent()){
                        plant.setEcologicalStatus(ecologicalStatusOp.get());
                    }
                }



                plantRepository.save(plant);
            }
        }

    }

}
