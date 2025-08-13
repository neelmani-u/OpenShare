//package com.openshare.files;
//
//
//import java.io.IOException;
//import java.util.List;
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.data.mongodb.gridfs.GridFsResource;
//
//@RestController
//@RequestMapping("/api/files")
//@CrossOrigin(origins = "http://localhost:5173")
//public class FileController {
//
//    private final FileService fileService;
//
//    public FileController(FileService fileService) { this.fileService = fileService; }
//
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> upload(@RequestPart("file") MultipartFile file) {
//        try {
//            FileDoc saved = fileService.upload(file);
//            return ResponseEntity.ok(saved);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(500).body("Upload failed");
//        }
//    }
//
//    @GetMapping
//    public List<FileDoc> list() {
//        return fileService.listAll();
//    }
//
//    @GetMapping("/{id}/download")
//    public ResponseEntity<?> download(@PathVariable String id) throws IOException {
//        GridFsResource res = fileService.downloadById(id);
//        if (res == null) return ResponseEntity.notFound().build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDispositionFormData("attachment", res.getFilename());
//        MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
//        if (res.getContentType() != null) mt = MediaType.parseMediaType(res.getContentType());
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(mt)
//                .body(new InputStreamResource(res.getInputStream()));
//    }
//}
//
