package com.smartmaint.web.Services;

import com.smartmaint.web.Models.Skill;
import com.smartmaint.web.Repositorises.SkillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepo skillRepo;

    public List<Skill> getAllSkills() {
        return skillRepo.findAll();
    }

    public void addSkill(Skill skill) {
        skillRepo.save(skill);
    }

}
