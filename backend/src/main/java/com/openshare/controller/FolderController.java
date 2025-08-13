package com.openshare.controller;

import org.springframework.web.bind.annotation.*;

import com.openshare.models.Folder;
import com.openshare.models.User;
import com.openshare.repos.FolderRepository;
import com.openshare.repos.UserRepository;

import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/folders")
@CrossOrigin(origins = "http://localhost:5173")
public class FolderController {

	private final UserRepository userRepository;
	private final FolderRepository folderRepository;

	public FolderController(FolderRepository folderRepository, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.folderRepository = folderRepository;
	}

	private String getUserId(HttpSession session) {
		Object uid = session.getAttribute("userId");
		return uid == null ? null : uid.toString();
	}

	@PostMapping
	public ResponseEntity<?> create(@RequestBody Map<String, String> body, HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		String name = body.get("name");
		if (name == null || name.isBlank())
			return ResponseEntity.badRequest().body("Invalid name");
		Folder f = new Folder();
		f.setName(name);
		f.setOwnerId(userId);
		f.setOwnerName(userRepository.findById(userId).get().getUsername());
		folderRepository.save(f);
		return ResponseEntity.ok(f);
	}

	@GetMapping
	public ResponseEntity<?> list(HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");

		List<Folder> mine = folderRepository.findByOwnerId(userId);
		List<Folder> shared = folderRepository.findBySharedWithContaining(userId);

		return ResponseEntity.ok(Map.of("mine", mine, "shared", shared));
	}

	@PutMapping("/{id}/rename")
	public ResponseEntity<?> rename(@PathVariable String id, @RequestBody Map<String, String> body,
			HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = folderRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		Folder f = opt.get();
		if (!f.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("Only owner can rename");
		String name = body.get("name");
		f.setName(name);
		folderRepository.save(f);
		return ResponseEntity.ok(f);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id, HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = folderRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		Folder f = opt.get();
		if (!f.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("Only owner can delete");
		folderRepository.deleteById(id);
		return ResponseEntity.ok(Map.of("status", "ok"));
	}

	@PostMapping("/{id}/share")
	public ResponseEntity<?> share(@PathVariable String id, @RequestBody Map<String, String> body,
			HttpSession session) {
		String userId = getUserId(session);
		if (userId == null)
			return ResponseEntity.status(401).body("Login required");
		var opt = folderRepository.findById(id);
		if (opt.isEmpty())
			return ResponseEntity.notFound().build();
		Folder f = opt.get();
		if (!f.getOwnerId().equals(userId))
			return ResponseEntity.status(403).body("Only owner can share");
		String shareWithUserName = body.get("username");
		if (shareWithUserName == null)
			return ResponseEntity.badRequest().body("Missing username");

		var uopt = userRepository.findByUsername(shareWithUserName);
		if (uopt.isEmpty())
			return ResponseEntity.badRequest().body("Invalid useranme!");
		User u = uopt.get();
		if (!f.getSharedWith().contains(u.getId())) {
			f.getSharedWith().add(u.getId());
			folderRepository.save(f);
		}
		return ResponseEntity.ok(Map.of("status", "ok"));
	}
}
