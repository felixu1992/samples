package top.felixu.sqlite.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author jobob
 * @since 2020-05-11
 */
@Data
@Accessors(chain = true)
public class User  {

    private static final long serialVersionUID=1L;

//    private Integer id;

    private String name;

    private Integer age;
}
