package com.imageplc.imageplace.controller;

import com.imageplc.imageplace.components.JwtTokenProvider;
import com.imageplc.imageplace.dto.PicturesRequestDTO;
import com.imageplc.imageplace.dto.AlbumInfoDTO;
import com.imageplc.imageplace.dto.ImageInfoDTO;
import com.imageplc.imageplace.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AlbumController {
    private final JwtTokenProvider tokenProvider = new JwtTokenProvider();
    @Autowired
    private AlbumService albumService;

    @PostMapping("/add-album")
    ResponseEntity<Map<String, String>> addAlbum(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumName = request.get("album_name");
        albumService.addAlbum(username, albumName);
        return ResponseEntity.ok(Collections.singletonMap("message", "添加相册成功"));
    }

    @PostMapping("/get-user-all-albums")
    ResponseEntity<List<AlbumInfoDTO>> getUserAllAlbums(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albums = albumService.getUserAllAlbums(username);
        return ResponseEntity.ok(albums);
    }

    @PostMapping("/get-album-all-pictures")
    ResponseEntity<List<ImageInfoDTO>> getAlbumAllPictures(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonList(null));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumId = Integer.parseInt(request.get("album_id"));
        return ResponseEntity.ok(albumService.getImagesByAlbumIdAndUsername(albumId, username));
    }

    @PostMapping("/add-pictures-into-album")
    ResponseEntity<Map<String, String>> addPicturesIntoAlbum(@RequestBody PicturesRequestDTO request) {
        var token = request.getToken();
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumId = Integer.parseInt(request.getAlbum());
        var picturesUUID = request.getPictures();
        if (albumService.addPicturesIntoAlbum(username, albumId, picturesUUID)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "添加图片成功"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "身份不符"));
    }

    @PostMapping("/delete-pictures-from-album")
    ResponseEntity<Map<String, String>> deletePicturesFromAlbum(@RequestBody PicturesRequestDTO request) {
        var token = request.getToken();
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumId = Integer.parseInt(request.getAlbum());
        var picturesUUID = request.getPictures();
        if (albumService.deletePicturesFromAlbum(username, albumId, picturesUUID)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "移除图片成功"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "身份不符"));
    }

    @PostMapping("/delete-album")
    ResponseEntity<Map<String, String>> deleteAlbum(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumId = Integer.parseInt(request.get("album"));
        if (albumService.deleteAlbum(username, albumId)) {
            return ResponseEntity.ok(Collections.singletonMap("message", "删除相册成功"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "身份不符"));
    }

    @PostMapping("/rename-album")
    ResponseEntity<Map<String, String>> renameAlbum(@RequestBody Map<String, String> request) {
        var token = request.get("token");
        if (!tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token无效"));
        }
        var username = tokenProvider.getUsernameFromToken(token);
        var albumId = Integer.parseInt(request.get("album"));
        var albumName = request.get("new_album_name");
        albumService.renameAlbum(username, albumId, albumName);
        return ResponseEntity.ok(Collections.singletonMap("message", "重命名成功"));
    }
}
