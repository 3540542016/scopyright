package lltw.scopyright.service;

import lltw.scopyright.VO.ResultVO;
import lltw.scopyright.entity.Works;
import com.baomidou.mybatisplus.extension.service.IService;
import lltw.scopyright.form.UploadForm;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
public interface WorksService extends IService<Works> {

    ResultVO uploadWork(UploadForm uploadForm);

    ResultVO showAll(Long creatorId);

    ResultVO submitCopyrightApplication(Long workId);

    ResultVO reviewCopyrightApplication(Long workId, boolean approval);

    ResultVO getAllWorksWithCreatorName();
}
