package com.infoa.educationms.service;

import com.infoa.educationms.DTO.GradeChangeDTO;
import com.infoa.educationms.entities.*;
import com.infoa.educationms.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeChangeServiceImpl implements GradeChangeService {
    
    @Autowired
    private GradeChangeRepository gradeChangeRepository;

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private TakeRepository takeRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private  GradeRepository gradeRepository;

    // 时间格式转换器
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public List<GradeChangeDTO> getPendingGradeChanges() {
        // 获取所有待审核的申请（result 为 null）
        List<GradeChange> pendingChanges = gradeChangeRepository.findByResultIsNull();
        
        return pendingChanges.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GradeChangeDTO createGradeChange(GradeChangeDTO gradeChangeDTO) {
        GradeChange gradeChange = convertToEntity(gradeChangeDTO);
        
        // 设置申请时间为当前时间
        gradeChange.setApplyTime(LocalDateTime.now());
        
        GradeChange savedChange = gradeChangeRepository.save(gradeChange);
        return convertToDTO(savedChange);
    }

    @Override
    public GradeChangeDTO updateGradeChange(Integer gradeChangeId, GradeChangeDTO gradeChangeDTO) {
        GradeChange gradeChange = gradeChangeRepository.findOneByChangeId(gradeChangeId);
        if (gradeChange == null) {
            throw new IllegalArgumentException("成绩修改申请不存在: " + gradeChangeId);
        }
        // 只允许更新审核结果和审核时间
        if (gradeChangeDTO.getResult() != null) {
            gradeChange.setResult(gradeChangeDTO.getResult());
            gradeChange.setCheckTime(LocalDateTime.now());
        }
        // 更新成绩
        Grade grade = gradeRepository.findOneByGradeId(gradeChange.getGradeId());
        if (grade != null) {
            grade.setGrade(gradeChange.getNewGrade());
            gradeRepository.save(grade);
        }
        // 其他字段不允许更新
        GradeChange updatedChange = gradeChangeRepository.save(gradeChange);
        return convertToDTO(updatedChange);
    }

    // 实体转DTO（包含时间格式转换）
    private GradeChangeDTO convertToDTO(GradeChange gradeChange) {
        GradeChangeDTO dto = new GradeChangeDTO();
        Take take = takeRepository.findOneByTakeId(gradeChange.getTakeId());
        Student student = studentRepository.findOneByUserId(take.getStudentId());
        Section section = sectionRepository.findOneBySectionId(take.getSectionId());
        Course course = courseRepository.findOneByCourseId(section.getCourseId());
        PersonalInfor personalInfor = personalInfoRepository.findOneByPersonalInforId(student.getPersonalInfoId());
        Grade grade = gradeRepository.findOneByGradeId(gradeChange.getGradeId());
        // 复制基础字段
        dto.setGradeChangeId(gradeChange.getChangeId());
        dto.setTakesId(gradeChange.getTakeId());
        dto.setTeacherId(gradeChange.getTeacherId());
        dto.setResult(gradeChange.getResult());
        dto.setName(grade.getName());
        dto.setProportion(grade.getProportion());
        dto.setNewGrade(gradeChange.getNewGrade());
        dto.setGradeId(gradeChange.getGradeId());
        dto.setStudentId(student.getUserId());
        dto.setStudentName(personalInfor.getName());
        dto.setCourseId(course.getCourseId());
        dto.setCourseName(course.getTitle());
        dto.setOriginalGrade(grade.getGrade());
        dto.setReason(gradeChange.getReason());
        dto.setType(gradeChange.getType());
        
        // 转换时间格式
        if (gradeChange.getApplyTime() != null) {
            dto.setApplyTime(gradeChange.getApplyTime().format(formatter));
        }
        if (gradeChange.getCheckTime() != null) {
            dto.setCheckTime(gradeChange.getCheckTime().format(formatter));
        }
        
        return dto;
    }

    // DTO转实体（包含时间格式转换）
    private GradeChange convertToEntity(GradeChangeDTO dto) {
        Grade grade = gradeRepository.findOneByGradeId(dto.getGradeId());
        GradeChange entity = new GradeChange();
        entity.setType(grade.getGradeType());
        entity.setGradeId(dto.getGradeId());
        entity.setReason(dto.getReason());
        entity.setTakeId(grade.getTakeId());
        entity.setNewGrade(dto.getNewGrade());
        entity.setTeacherId(dto.getTeacherId());
        // 转换时间格式
        if (dto.getApplyTime() != null) {
            entity.setApplyTime(LocalDateTime.parse(dto.getApplyTime(), formatter));
        }
        if (dto.getCheckTime() != null) {
            entity.setCheckTime(LocalDateTime.parse(dto.getCheckTime(), formatter));
        }
        return entity;
    }
}