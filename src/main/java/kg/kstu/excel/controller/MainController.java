package kg.kstu.excel.controller;

import kg.kstu.excel.model.InputDateModel;
import kg.kstu.excel.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/excel")
public class MainController {
    @Autowired
    ExcelService excelService;

    @GetMapping()
    public String getIndex1(Model model) {
        System.out.println(model);
        return "sheet";
    }

    @GetMapping("/download")
    public String download(Model model) {
        System.out.println(model);
        return "download";
    }

    @PostMapping
    String createFile(@RequestBody InputDateModel inputDateModel){
        excelService.createFile(inputDateModel.getStartDate(), inputDateModel.getEndDate());
        return "Success";
    }

    @PostMapping("/hi")
    String createFile2(@RequestBody InputDateModel inputDateModel){
        System.out.println("Hello");
        System.out.println(inputDateModel);
        return "Success";
    }

    @GetMapping("/hi")
    String createFile3(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate) throws ParseException {
        Date start =new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
        Date end =new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        startDate = dateFormat.format(start);
        endDate = dateFormat.format(end);
        excelService.createFile(startDate, endDate);
        return "redirect:/excel";
    }

//    @RequestMapping(path = "/download", method = RequestMethod.GET)
//    public ResponseEntity<Resource> download(String param) throws IOException {
//
//        // ...
//        File file = new File("C:\\Users\\Professional\\Desktop\\work\\test.xlsx");
//        Path path = Paths.get(file.getAbsolutePath());
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
//        HttpHeaders headers = new HttpHeaders();
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
}
