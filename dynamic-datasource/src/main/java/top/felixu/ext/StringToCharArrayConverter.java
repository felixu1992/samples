package top.felixu.ext;

import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a Char Array.
 *
 * @author felixu
 * @author Phillip Webb
 * @date 2019.07.23
 */
class StringToCharArrayConverter implements Converter<String, char[]> {

    @Override
    public char[] convert(String source) {
        return source.toCharArray();
    }

}
