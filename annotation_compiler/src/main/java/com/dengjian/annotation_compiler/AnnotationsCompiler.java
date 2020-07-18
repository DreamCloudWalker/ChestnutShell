package com.dengjian.annotation_compiler;

import com.dengjian.annotations.BindPath;
import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)   // 注册注解处理器
public class AnnotationsCompiler extends AbstractProcessor {
    Filer filer;    // 生成文件对象

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
    }

    // 声明当前注解处理器支持的Java源版本
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    // Notice: 声明当前注解处理器需要处理的注解
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(BindPath.class.getCanonicalName()); // 得到包名+类名
        return ret;
    }

    /**
     * 生成代码文件
     * @param set
     * @param roundEnvironment
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        // 1. TypeElement 类节点 2. VariableElement 成员变量节点 3. ExecutableElement 方法节点
        // 返回带有BindPath注解的所有节点
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            TypeElement typeElement = (TypeElement) element;
            // 获取该类节点上的注解
            BindPath annotation = typeElement.getAnnotation(BindPath.class);
            String key = annotation.value();  // 获取注解所传的参数

            // 获取该类节点的包名+类名(getSimpleName只有类名没有包名)
            String activityName = typeElement.getQualifiedName().toString();
            map.put(key, activityName + ".class");
        }

        // 开始生成文件和代码
        if (map.size() > 0) {
            Writer writer = null;
            // 创建生成文件的类名
            String activityName = "ActivityRouter" + System.currentTimeMillis();
            try {
                // 创建文件并返回java文件对象
                JavaFileObject sourceFile = filer.createSourceFile("com.dengjian.router." + activityName);
                writer = sourceFile.openWriter();
                writer.write("package com.dengjian.nutrouter;\n");
                writer.write("import com.dengjian.nutrouter.NutRouter;\n");
                writer.write("import com.dengjian.nutrouter.IRouter;\n");
                writer.write("public class " + activityName + " implements IRouter {\n");
                writer.write("@Override\n");
                writer.write("public void putActivity() {\n");
                for (String key : map.keySet()) {
                    String value = map.get(key);
                    writer.write("\tNutRouter.getInstance().putActivity(\"" + key + "\"," + value + ");\n");
                }
                writer.write("}\n}\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (null != writer) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return false;
    }
}
