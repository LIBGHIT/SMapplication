package com.smartmaint.web.Services;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.GridFSBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {


    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoDatabaseFactory mongoDbFactory;

    public String addFile(MultipartFile file) throws IOException {
        Object id = gridFsTemplate.store(
                file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return id.toString();
    }

    public GridFSFile getFile(String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }

    public GridFSBucket getGridFsBucket() {
        return GridFSBuckets.create(mongoDbFactory.getMongoDatabase());
    }

}

