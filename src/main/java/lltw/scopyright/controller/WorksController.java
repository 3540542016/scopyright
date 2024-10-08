package lltw.scopyright.controller;


import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import lltw.scopyright.form.UploadForm;
import lltw.scopyright.service.WorksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@RestController
@RequestMapping("/works")
@Slf4j
public class WorksController {

    private final WorksService worksService;
    @Autowired
    public WorksController(WorksService worksService ){
        this.worksService = worksService;
    }

    // 显示内容创作者所有作品
    @GetMapping("/creator/show")
    public ResultVO showWorks(@RequestParam("id") Long creatorId) {
        return worksService.showAll(creatorId);
    }

    // 查询全部用户作品接口
    @GetMapping("/creator/all")
    public ResultVO getAllWorks() {
        return worksService.getAllWorksWithCreatorName();
    }

    // 内容创作者上传作品接口
    @PostMapping("/creator/upload")
    public ResultVO uploadWork(@RequestBody UploadForm uploadForm) {
        return worksService.uploadWork(uploadForm);
    }

    // 内容创作者提交申请版权接口
    @GetMapping("/creator/submitCopyright")
    public ResultVO submitCopyrightApplication(@RequestParam("workId") Long workId) {
        return worksService.submitCopyrightApplication(workId);
    }

    // 审核机构审核版权接口
    @GetMapping("/auditor/reviewCopyright")
    public ResultVO reviewCopyrightApplication(@RequestParam("id") Long workId, @RequestParam("approval") boolean approval) {
            return worksService.reviewCopyrightApplication(workId, approval);
    }
}
