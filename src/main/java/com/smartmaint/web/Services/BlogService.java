package com.smartmaint.web.Services;

import com.smartmaint.web.Models.Blogs;
import com.smartmaint.web.Repositorises.BlogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepo blogRepo;
    @Autowired
    private FileService fileService;

    public Blogs addBlog(Blogs blog , MultipartFile image)throws IOException
    {   String id = fileService.addFile(image);
        blog.setImageId(id);
        blog.setDate(new Date());
        return blogRepo.save(blog);
    }

    public List<Blogs>getAllBlogs() {
        return blogRepo.findAll();
    }




    public long countBlogs() {
        return blogRepo.count();
    }
    public long countActiveBlogs() {
        return blogRepo.countByArchivedFalse();
    }


    public Blogs getBlogById(String id) {
        return blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog introuvable pour l'ID : " + id));
    }


    public void deleteBlog(String id) {
        blogRepo.deleteById(id);
    }

    public Page<Blogs> getAllBlogsPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return blogRepo.findAll(pageable);
    }

    public Page<Blogs> getAllNonArchivedBlogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return blogRepo.findByArchivedFalse(pageable);
    }

    public Blogs reactivateBlog(String id) {
        Blogs blog = blogRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog introuvable pour l'ID : " + id));
        blog.setArchived(false);
        return blogRepo.save(blog);
    }



    // Autowire MongoTemplate dans votre service
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Map> countBlogsBySubject() {
        Aggregation agg = newAggregation(
                match(Criteria.where("archived").is(false)),
                group("subject").count().as("count"),
                project("count").and("subject").previousOperation()
        );

        // Execute Aggregation
        AggregationResults<Map> groupResults
                = mongoTemplate.aggregate(agg, Blogs.class, Map.class);

        return groupResults.getMappedResults();
    }
    public Blogs updateBlog(Blogs blog) {
        return blogRepo.save(blog);
    }


}

