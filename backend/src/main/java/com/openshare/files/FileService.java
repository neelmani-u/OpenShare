//package com.openshare.files;
//
//
//import java.io.InputStream;
//import java.util.List;
//import java.util.Optional;
//
//import com.mongodb.client.gridfs.model.GridFSFile;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.gridfs.GridFsTemplate;
//import org.springframework.data.mongodb.gridfs.GridFsResource;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//public class FileService {
//
//    private final GridFsTemplate gridFsTemplate;
//    private final FileDocRepository fileDocRepository;
//
//    public FileService(GridFsTemplate gridFsTemplate, FileDocRepository fileDocRepository) {
//        this.gridFsTemplate = gridFsTemplate;
//        this.fileDocRepository = fileDocRepository;
//    }
//
//    public FileDoc upload(MultipartFile file) throws Exception {
//        String filename = file.getOriginalFilename();
//        String contentType = file.getContentType();
//        try (InputStream in = file.getInputStream()) {
//            ObjectId id = gridFsTemplate.store(in, filename, contentType);
//            FileDoc doc = new FileDoc();
//            doc.setId(id.toString());
//            doc.setFilename(filename);
//            doc.setContentType(contentType);
//            doc.setLength(file.getSize());
//            return fileDocRepository.save(doc);
//        }
//    }
//
//    public List<FileDoc> listAll() {
//        return fileDocRepository.findAll();
//    }
//
//    public GridFsResource downloadById(String id) {
//        GridFSFile gridFile = gridFsTemplate.findOne(
//            org.springframework.data.mongodb.core.query.Query.query(
//                org.springframework.data.mongodb.core.query.Criteria.where("_id").is(new ObjectId(id))
//            )
//        );
//        if (gridFile == null) return null;
//        return gridFsTemplate.getResource(gridFile);
//    }
//
//    public Optional<FileDoc> getMeta(String id) {
//        return fileDocRepository.findById(id);
//    }
//}
