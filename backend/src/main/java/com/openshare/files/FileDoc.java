//package com.openshare.files;
//
//
//import java.time.Instant;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//
//@Document("file_docs")
//public class FileDoc {
//    @Id
//    private String id;
//    private String filename;
//    private String contentType;
//    private long length;
//    private Instant uploadDate = Instant.now();
//
//    // getters & setters
//    public String getId(){ return id; }
//    public void setId(String id){ this.id = id; }
//    public String getFilename(){ return filename; }
//    public void setFilename(String filename){ this.filename = filename; }
//    public String getContentType(){ return contentType; }
//    public void setContentType(String contentType){ this.contentType = contentType; }
//    public long getLength(){ return length; }
//    public void setLength(long length){ this.length = length; }
//    public Instant getUploadDate(){ return uploadDate; }
//    public void setUploadDate(Instant uploadDate){ this.uploadDate = uploadDate; }
//}
//
//
