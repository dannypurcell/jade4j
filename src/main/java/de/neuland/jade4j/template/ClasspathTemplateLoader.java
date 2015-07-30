package de.neuland.jade4j.template;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.MissingResourceException;

/**
 * Loads a Jade template from Classpath
 * It is useful when Jade templates are in the same JAR or WAR
 *
 * Supports reloading on modification when the requested template is not in a JAR or WAR.
 *
 * @author emiguel
 */
public class ClasspathTemplateLoader implements TemplateLoader {

    private static final String SUFFIX = ".jade";
    private Charset charset;

    public ClasspathTemplateLoader() {
        this(Charset.defaultCharset());
    }

    public ClasspathTemplateLoader(Charset encoding) {
        setEncoding(encoding);
    }

    @Override
    public long getLastModified(String name) throws IOException {
        return locateResource(name).openConnection().getLastModified();
    }

    @Override
    public Reader getReader(String name) throws IOException {
        assert name != null && !name.isEmpty() : "name may not be null or empty";
        return new InputStreamReader(locateResource(name).openStream(), charset);
    }

    public String getEncoding() {
        return charset.name();
    }

    public void setEncoding(String encoding) {
        assert encoding != null && !encoding.isEmpty() : "charset may not be null or empty";
        setEncoding(Charset.forName(encoding));
    }

    public void setEncoding(Charset charset) {
        this.charset = charset;
    }

    private URL locateResource(String name) {
        if (!name.toLowerCase().endsWith(SUFFIX)) {
            name = name + SUFFIX;
        }
        final URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        if (url == null) {
            throw new MissingResourceException("could not locate resource on classpath: " + name, name, name);
        }
        return url;
    }
}
