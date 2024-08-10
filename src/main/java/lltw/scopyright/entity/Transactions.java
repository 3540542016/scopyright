package lltw.scopyright.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author sakura
 * @since 2024-08-08
 */
@Data
  @EqualsAndHashCode(callSuper = false)
    public class Transactions implements Serializable {

    private static final long serialVersionUID=1L;

      /**
     * 主键，自增
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 作品ID，外键
     */
      private Long workId;

      /**
     * 买方ID，外键
     */
      private Long buyerId;

      /**
     * 卖方ID，外键
     */
      private Long sellerId;

      /**
     * 状态（pending, completed）
     */
      private String status;

      /**
     * 交易哈希值
     */
      private String hash;

      /**
     * 创建时间
     */
      private LocalDateTime createdAt;

      /**
     * 更新时间
     */
      private LocalDateTime updatedAt;


}
