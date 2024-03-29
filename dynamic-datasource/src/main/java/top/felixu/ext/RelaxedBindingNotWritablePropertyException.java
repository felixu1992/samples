package top.felixu.ext;

import org.springframework.beans.NotWritablePropertyException;

/**
 * A custom {@link NotWritablePropertyException} that is thrown when a failure occurs
 * during relaxed binding.
 *
 * @author felixu
 * @author Andy Wilkinson
 * @date 2019.07.23
 * @see RelaxedDataBinder
 * @since 1.3.0
 */
public class RelaxedBindingNotWritablePropertyException
        extends NotWritablePropertyException {

    private final String message;

    private final PropertyOrigin propertyOrigin;

    RelaxedBindingNotWritablePropertyException(NotWritablePropertyException ex,
                                               PropertyOrigin propertyOrigin) {
        super(ex.getBeanClass(), ex.getPropertyName());
        this.propertyOrigin = propertyOrigin;
        this.message = "Failed to bind '" + propertyOrigin.getName() + "' from '"
                + propertyOrigin.getSource().getName() + "' to '" + ex.getPropertyName()
                + "' property on '" + ex.getBeanClass().getName() + "'";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public PropertyOrigin getPropertyOrigin() {
        return this.propertyOrigin;
    }

}

