package com.openshare.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.openshare.models.FileMeta;
import com.openshare.models.Folder;
import com.openshare.repos.FileMetaRepository;
import com.openshare.repos.FolderRepository;

import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

	private final FileMetaRepository fileMetaRepository;
	private final GridFsTemplate gridFsTemplate;
	private final FolderRepository folderRepository;

	public FileController(FileMetaRepository fileMetaRepository, GridFsTemplate gridFsTemplate, FolderRepository folderRepository) {
		this.fileMetaRepository = fileMetaRepository;
		this.gridFsTemplate = gridFsTemplate;
		this.folderRepository = folderRepository;
	}

	private String getUserId(HttpSession session) {
		Object uid = session.getAttribute("userId");
		return uid == null ? null : uid.toString();
	}

	@PostMapping("/{folderId}")
	public ResponseEntity<?> upload(@PathVariable String folderId, @RequestPart("file") MultipartFile file,
			HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		try {
			var input = file.getInputStream();
			ObjectId gridId = gridFsTemplate.store(input, file.getOriginalFilename(), file.getContentType());
			FileMeta meta = new FileMeta();
			meta.setFilename(file.getOriginalFilename());
			meta.setContentType(file.getContentType());
			meta.setSize(file.getSize());
			meta.setOwnerId(userId);
			meta.setFolderId(folderId);
			meta.setGridFsId(gridId.toString());
			fileMetaRepository.save(meta);
			return ResponseEntity.ok(meta);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Upload failed");
		}
	}

	@GetMapping("/by-folder/{folderId}")
	public ResponseEntity<?> listByFolder(@PathVariable String folderId, HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = folderRepository.findById(folderId);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		Folder f = opt.get();
		
		// System.out.println(userId);
		// System.out.println(f.getOwnerId());
		// System.out.println(f.getSharedWith());
		
		boolean isOwner = f.getOwnerId().equals(userId);
	    boolean isSharedUser = f.getSharedWith() != null && f.getSharedWith().contains(userId);
	    
	    if (!isOwner && !isSharedUser) {
	        return ResponseEntity.status(403).body("You don't have permission to view this resource");
	    }
		
	    /*if (!f.getSharedWith().contains(userId))
			return ResponseEntity.status(403).body("You don't have permission to view this resource");
		if (!f.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("You don't have permission to view this resource");*/
	    
		List<FileMeta> files = fileMetaRepository.findByFolderId(folderId);
		return ResponseEntity.ok(files);
	}

	@GetMapping("/{id}/download")
	public ResponseEntity<?> download(@PathVariable String id, HttpSession session) throws IOException {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		Optional<FileMeta> opt = fileMetaRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		FileMeta meta = opt.get();
		GridFSFile gf = gridFsTemplate.findOne(org.springframework.data.mongodb.core.query.Query
				.query(org.springframework.data.mongodb.core.query.Criteria.where("_id")
						.is(new ObjectId(meta.getGridFsId()))));
		if (gf == null)
			return ResponseEntity.notFound().build();
		GridFsResource resource = gridFsTemplate.getResource(gf);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", resource.getFilename());
		MediaType mt = MediaType.APPLICATION_OCTET_STREAM;
		if (resource.getContentType() != null)
			mt = MediaType.parseMediaType(resource.getContentType());
		return ResponseEntity.ok().headers(headers).contentType(mt)
				.body(new InputStreamResource(resource.getInputStream()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = fileMetaRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		FileMeta meta = opt.get();
		if (!meta.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("Only owner can delete");
		gridFsTemplate.delete(org.springframework.data.mongodb.core.query.Query
				.query(org.springframework.data.mongodb.core.query.Criteria.where("_id")
						.is(new ObjectId(meta.getGridFsId()))));
		fileMetaRepository.deleteById(id);
		return ResponseEntity.ok(Map.of("status", "ok"));
	}

	@PutMapping("/{id}/rename")
	public ResponseEntity<?> rename(@PathVariable String id, @RequestBody Map<String, String> body,
			HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = fileMetaRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		FileMeta meta = opt.get();
		if (!meta.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("Only owner can rename");
		meta.setFilename(body.get("name"));
		fileMetaRepository.save(meta);
		return ResponseEntity.ok(meta);
	}
}
