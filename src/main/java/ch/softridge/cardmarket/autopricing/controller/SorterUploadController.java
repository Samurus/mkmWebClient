package ch.softridge.cardmarket.autopricing.controller;

import ch.softridge.cardmarket.autopricing.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Kevin Zellweger
 * @Date 05.07.20
 */
@Controller
public class SorterUploadController {
    private static Logger log = LoggerFactory.getLogger(SorterUploadController.class);
    private final String UPLOAD_DIR = "./uploads/";
    private CardService cardService;

    @Autowired
    public SorterUploadController(CardService cardService){
        this.cardService = cardService;
    }

    @GetMapping("/sorterUpload")
    public String main(){
        return "sorterUpload";
    }

    @PostMapping("/sorterUpload/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes) {

        // check if file is empty
        if (file.isEmpty()) {
            attributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/sorterUpload";
        }

        // normalize the file path
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info(fileName);
        // save the file on the local file system
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            log.info(path.toString());
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return success response
        attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');
        cardService.addAll(cardService.readSorterCSV(fileName));
        return "redirect:/sorterUpload";
    }

}
