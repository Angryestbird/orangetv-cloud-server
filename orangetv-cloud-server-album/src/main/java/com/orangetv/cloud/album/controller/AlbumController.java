package com.orangetv.cloud.album.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@RequestMapping("store")
@RequiredArgsConstructor
@Slf4j
public class AlbumController {

    private final GridFsTemplate gridFsTemplate;

    @GetMapping(value = "{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<GridFsResource> getById(@PathVariable String id) {
        log.info("get image from gridFS,id is {}", id);
        var file = gridFsTemplate.findOne(query(where("id").is(new ObjectId(id))));
        return ResponseEntity.ok(gridFsTemplate.getResource(Objects.requireNonNull(file)));
    }
}
