package me.lauriichan.minecraft.wildcard.migration;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;

public final class MigrationProcessor extends AbstractProcessor {

    public static final String MIGRATION_RESOURCE = "META-INF/migrations";

    private Types typeHelper;
    private Elements elementHelper;

    private TypeMirror migrationType;

    private final HashSet<String> migrations = new HashSet<>();

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.typeHelper = processingEnv.getTypeUtils();
        this.elementHelper = processingEnv.getElementUtils();
        this.migrationType = elementHelper.getTypeElement(MigrationProvider.class.getName()).asType();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("*");
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }

        log(Kind.NOTE, "Processing @%s", Migration.class.getName());
        for (final Element element : roundEnv.getElementsAnnotatedWith(Migration.class)) {
            if (element.getKind() == ElementKind.ANNOTATION_TYPE) {
                continue;
            }
            processElement(element);
        }

        log(Kind.NOTE, "Processing nested @%s", Migration.class.getName());
        for (final TypeElement typeElement : annotations) {
            if (getAnnotationMirror(typeElement, Migration.class) == null) {
                continue;
            }
            for (final Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                processElement(element);
            }
        }

        try (Writer writer = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", MIGRATION_RESOURCE).openWriter()) {
            final Iterator<String> iterator = migrations.iterator();
            while (iterator.hasNext()) {
                writer.write(iterator.next());
                if (iterator.hasNext()) {
                    writer.write('\n');
                }
            }
            writer.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void processElement(final Element element) {
        log(Kind.NOTE, "Processing Extension '%s'", element.asType().toString());
        if (!(element instanceof TypeElement)) {
            log(Kind.ERROR, "Extension annotation is only available for classes");
            return;
        }
        final TypeMirror type = element.asType();
        if (!typeHelper.isAssignable(type, migrationType)) {
            log(Kind.ERROR, "%s is not an Migration (doesn't extend Migration)", element);
            return;
        }
        final TypeElement typeElement = (TypeElement) element;
        if (typeElement.getModifiers().contains(Modifier.ABSTRACT)) {
            log(Kind.WARNING, "%s is an abstract class and can't be a Migration", typeElement);
            return;
        }
        final String typeName = type.toString();
        if (migrations.contains(typeName)) {
            return; // Don't know if that will even happen
        }
        log(Kind.NOTE, "Found migration: %s", typeName);
        migrations.add(typeName);
    }

    /*
     * Logging
     */

    public void log(final Kind kind, final String message, final Object... arguments) {
        final String out = String.format(message, arguments);
        processingEnv.getMessager().printMessage(kind, out);
        if (kind == Kind.ERROR) {
            System.out.println("[ERROR] " + out);
            return;
        }
        if (kind == Kind.WARNING) {
            System.out.println("[WARNING] " + out);
            return;
        }
        System.out.println("[INFO] " + out);
    }

    /*
     * Helper
     */

    public AnnotationMirror getAnnotationMirror(final TypeElement element, final Class<?> annotation) {
        final String annotationName = annotation.getName();
        for (final AnnotationMirror mirror : element.getAnnotationMirrors()) {
            if (mirror.getAnnotationType().toString().equals(annotationName)) {
                return mirror;
            }
        }
        return null;
    }
}
