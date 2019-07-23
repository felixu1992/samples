package top.felixu.ext;

import org.springframework.core.env.PropertySource;

/**
 * The origin of a property, specifically its source and its name before any prefix was
 * removed.
 *
 * @author felixu
 * @author Andy Wilkinson
 * @date 2019.07.23
 * @since 1.3.0
 */
public class PropertyOrigin {

    private final PropertySource<?> source;

    private final String name;

    PropertyOrigin(PropertySource<?> source, String name) {
        this.name = name;
        this.source = source;
    }

    public PropertySource<?> getSource() {
        return this.source;
    }

    public String getName() {
        return this.name;
    }
}
