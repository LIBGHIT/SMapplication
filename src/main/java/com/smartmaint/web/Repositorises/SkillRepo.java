package com.smartmaint.web.Repositorises;

import com.smartmaint.web.Models.Skill;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SkillRepo extends MongoRepository<Skill, String> {

}
