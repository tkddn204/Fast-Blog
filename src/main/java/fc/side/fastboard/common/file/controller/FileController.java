package fc.side.fastboard.common.file.controller;

import fc.side.fastboard.common.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/images/{storedFileName}")
    @ResponseBody
    public Resource getImage(
            @PathVariable("storedFileName") String storedFileName
    ) throws MalformedURLException {
        return new UrlResource("file:" + fileService.getFile(storedFileName).filePath());
    }
}
