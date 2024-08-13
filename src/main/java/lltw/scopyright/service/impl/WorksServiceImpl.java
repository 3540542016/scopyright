package lltw.scopyright.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.VO.WorksDTO;
import lltw.scopyright.entity.Users;
import lltw.scopyright.entity.Works;
import lltw.scopyright.form.UploadForm;
import lltw.scopyright.mapper.UsersMapper;
import lltw.scopyright.mapper.WorksMapper;
import lltw.scopyright.service.WorksService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Service
@Slf4j
@Transactional
public class WorksServiceImpl extends ServiceImpl<WorksMapper, Works> implements WorksService {

    private final WorksMapper worksMapper;
    private final UsersMapper usersMapper;

    @Autowired
    public WorksServiceImpl(WorksMapper worksMapper, UsersMapper usersMapper){
        this.worksMapper = worksMapper;
        this.usersMapper = usersMapper;
    }

    @Override
    public ResultVO uploadWork(UploadForm uploadForm) {
        Users user = usersMapper.selectById(uploadForm.getId());
        if (user != null){
            Works works = new Works();
            works.setCreatorId(uploadForm.getId());
            works.setTitle(uploadForm.getTitle());
            works.setDescription(uploadForm.getDescription());
            int rows = worksMapper.insert(works);
            if (rows > 0){
                return ResultVO.success("作品上传成功");
            }
            return ResultVO.error(-1,"作品上传失败");
        }
        return ResultVO.error(-2,"用户不存在");
    }

    @Override
    public ResultVO showAll(Long creatorId) {
        QueryWrapper<Works> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        List<Works> worksList = worksMapper.selectList(queryWrapper);
        if (worksList != null) {
            return ResultVO.success(worksList);
        }
        return ResultVO.error(-1, "查询作品失败，用户不存在");
    }

    /**
     * 用户申请作品版权
     * @param workId 作品ID
     * @return 申请结果
     */
    @Override
    // WorksService.java
    public ResultVO submitCopyrightApplication(Long workId) {
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }

        if ("pending".equals(work.getStatus())) {
            return ResultVO.error(-2, "作品正在审核中");
        }

        UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", workId)
                .set("status", "pending")
                .set("copyright_applied", true);

        worksMapper.update(null, updateWrapper);
        return ResultVO.success("版权申请已提交，等待审核");
    }

    /**
     * 审核机构审核
     */
    @Override
    public ResultVO reviewCopyrightApplication(Long workId, boolean approval) {
        Works work = worksMapper.selectById(workId);
        if (work == null) {
            return ResultVO.error(-1, "作品不存在");
        }

        if (!"pending".equals(work.getStatus())) {
            return ResultVO.error(-2, "作品未处于审核状态");
        }

        if (approval) {
            // 生成唯一版权编号
            String copyrightNumber = UUID.randomUUID().toString();
            UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", workId)
                    .set("status", "approved")
                    .set("copyright_applied", true)
                    .set("copyright_number", copyrightNumber)
                    .set("updated_at", LocalDateTime.now());

            worksMapper.update(null, updateWrapper);
            return ResultVO.success(copyrightNumber);
        } else {
            UpdateWrapper<Works> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", workId)
                    .set("status", "rejected")
                    .set("copyright_applied", false)
                    .set("updated_at", LocalDateTime.now());

            worksMapper.update(null, updateWrapper);
            return ResultVO.success("版权申请被拒绝");
        }
    }

    @Override
    public ResultVO getAllWorksWithCreatorName() {
        // 查询所有作品
        List<Works> worksList = worksMapper.selectList(null);

        // 对每个作品查询对应的内容创作者
        List<WorksDTO> worksWithCreatorName = worksList.stream().map(work -> {
            Users creator = usersMapper.selectById(work.getCreatorId());
            return new WorksDTO(work.getId(), work.getTitle(), work.getDescription(), work.getStatus(), creator.getUsername());
        }).collect(Collectors.toList());

        return ResultVO.success(worksWithCreatorName);
    }
}
