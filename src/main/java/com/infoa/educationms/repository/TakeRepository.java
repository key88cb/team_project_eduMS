package com.infoa.educationms.repository;
import java.util.List;
import com.infoa.educationms.entities.Take;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TakeRepository extends JpaRepository<Take, Integer> {
    /**
     * 查询学生在多个section中的选课记录
     * @param studentId 学生ID
     * @param sectionIds section ID列表
     * @return 选课记录(Optional包装)
     */
    @Query("SELECT t FROM Take t WHERE t.studentId = :studentId AND t.sectionId IN :sectionIds")
    Optional<Take> findByStudentIdAndSectionIdIn(
            @Param("studentId") int studentId,
            @Param("sectionIds") List<Integer> sectionIds);

    /**
     * 根据多个sectionId查询选课记录
     * @param sectionIds section ID列表
     * @return 符合条件的选课记录列表
     */
    List<Take> findBySectionIdIn(List<Integer> sectionIds);


    Take findOneByTakeId(Integer takeId);

    List<Take> findBySectionId(Integer sectionId);
    Take findOneBySectionId(Integer sectionId);
    /**
     * 根据学生ID查询所有选课记录
     * @param studentId 学生用户ID
     * @return 该学生的所有选课记录列表
     */
    List<Take> findByStudentId(Integer studentId);

    Take findOneBySectionIdAndStudentId(int sectionId, int studentId);
}
