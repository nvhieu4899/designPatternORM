package org.example.map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

@SupportedAnnotationTypes("org.example.map.Table")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class TableProcessor extends AbstractProcessor {


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        processingEnv.getMessager()
                .printMessage(Diagnostic.Kind.NOTE, "Start");

        for (TypeElement annotation : annotations) {
            processingEnv.getMessager()
                    .printMessage(Diagnostic.Kind.NOTE, "Processing");

            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

            JSONArray jsonArray = new JSONArray();

            for (Element annotatedElement : annotatedElements) {
                if (annotatedElement.getKind() == ElementKind.CLASS) {
                    TypeElement typeElement = (TypeElement) annotatedElement;
                    try {
                        processingEnv.getMessager()
                                .printMessage(Diagnostic.Kind.NOTE, typeElement.getQualifiedName().toString());

//                        Class annotatedClass = Class.forName(typeElement.getQualifiedName().toString());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(0, typeElement.getSimpleName().toString());

                        for (Element enclosedEle : typeElement.getEnclosedElements()) {
                            processingEnv.getMessager()
                                    .printMessage(Diagnostic.Kind.NOTE, enclosedEle.getSimpleName().toString());

                            if (enclosedEle.getKind() != ElementKind.FIELD)
                                continue;

                            if (enclosedEle.getAnnotation(Column.class) != null) {
                                jsonObject.put(enclosedEle.getSimpleName(), enclosedEle.getAnnotation(Column.class).fieldName());
                            } else {
                                jsonObject.put(enclosedEle.getSimpleName().toString(), enclosedEle.getSimpleName().toString());
                            }
                        }

                        processingEnv.getMessager()
                                .printMessage(Diagnostic.Kind.NOTE, "Writing");
                        jsonArray.add(jsonObject);

                    } catch (Exception e) {
                        processingEnv.getMessager()
                                .printMessage(Diagnostic.Kind.NOTE, "Class not exist!");
                    }

                }
            }

            writeObjects("map.json", jsonArray);
        }
        return true;
    }

    private void writeObjects(String fileName, JSONArray array) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(array.toJSONString());
//            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
