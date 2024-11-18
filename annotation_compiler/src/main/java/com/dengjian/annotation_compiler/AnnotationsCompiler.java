package com.dengjian.annotation_compiler;

import com.dengjian.annotation_compiler.utils.ProcessorConfig;
import com.dengjian.annotation_compiler.utils.ProcessorUtils;
import com.dengjian.annotations.BindPath;
import com.dengjian.annotations.bean.RouterBean;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@AutoService(Processor.class)   // 启用服务，注册注解处理器
@SupportedAnnotationTypes({"com.dengjian.annotations.BindPath"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)    // 环境版本
// 接受安卓工程的传参
@SupportedOptions("appInfo")
public class AnnotationsCompiler extends AbstractProcessor {
    private Filer mFiler;    // 生成文件对象
    private Elements mElementTools;
    private Types mTypeTool;
    private Messager mMessager;

    private String mOptions;    // 各个模块传递过来的模块名，比如app，nutlogin等
    private String mAptPackage; // 各个模块传递来的目录，用于同一存放apt生成的文件

    // 仓库一 Path，比如Map<"nutlogin", List<RouterBean>>
    private Map<String, List<RouterBean>> mAllPathMap = new HashMap<>();

    // 仓库二 Group，比如Map<"nutlogin", "BindPath$$Path$$nutlogin.class">
    private Map<String, String> mAllGroupMap = new HashMap<>();

    private final boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();

//        if (ProcessorUtils.isEmpty(path) || !path.startsWith("/")) {
//            mMessager.printMessage(Diagnostic.Kind.ERROR, "注解中的path必须/开头");
//            return false;
//        }
//
//        if (0 == path.lastIndexOf("/")) {
//            mMessager.printMessage(Diagnostic.Kind.ERROR, "未按规定配置路径，如/app/MainActivity");
//            return false;
//        }
//
//        // 从第一个/到第二个/中间截取，如/app/MainActivity 截取出app作为group
//        String finalGroup = path.substring(1, path.indexOf("/", 1));

        return true;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mElementTools = processingEnvironment.getElementUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();

        String value = processingEnvironment.getOptions().get("appInfo");
        mMessager.printMessage(Diagnostic.Kind.NOTE, ">>>>>>>>>>>>>>>>>>>>>" + value);
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
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process run...");
        if (set.isEmpty()) {
            return false;
        }

        // 1. TypeElement 类节点 2. VariableElement 成员变量节点 3. ExecutableElement 方法节点
        // 返回带有BindPath注解的所有节点
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(BindPath.class);
        TypeElement activityType = mElementTools.getTypeElement(ProcessorConfig.ACTIVITY_PACKAGE);
        TypeMirror activityMirror = activityType.asType();

        Map<String, String> map = new HashMap<>();
        for (Element element : elementsAnnotatedWith) {
            /**
             * javapoet test code, 生成代码模板1：
             *  package com.dengjian.helloworld;
             *  public final class HelloWorld {
             *      public static void main(String[] args) {
             *          System.out.println("Hello, JavaPoet!");
             *      }
             *  }
             */
//            // 1. 方法
//            MethodSpec mainMethod = MethodSpec.methodBuilder("main")
//                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                    .returns(void.class)
//                    .addParameter(String[].class, "args")
//                    // 增加main方法的内容
//                    .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet！")
//                    .build();
//            // 2. 类
//            TypeSpec mainClass = TypeSpec.classBuilder("HelloWorld")
//                    .addMethod(mainMethod)
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .build();
//
//            // 3. 包
//            JavaFile mainPackage = JavaFile.builder("com.dengjian.helloworld", mainClass).build();
//
//            try {
//                mainPackage.writeTo(mFiler);
//            } catch (IOException e) {
//                e.printStackTrace();
//                mMessager.printMessage(Diagnostic.Kind.WARNING, "生成测试文件失败，异常："
//                        + e.getMessage());
//            }

            /**
             * javapoet test code, 生成代码模板2：
             *  package com.dengjian.helloworld;
             *  public final class MainActivity$$$$$$$$$BindPath {
             *      public static void findTargetClass(String path) {
             *          return path.equals("/app/MainActivity") ? MainActivity.class : null;
             *      }
             *  }
             */

//            TypeElement typeElement = (TypeElement) element;
//            // 获取该类节点上的注解
//            BindPath annotation = typeElement.getAnnotation(BindPath.class);
//            String key = annotation.path();  // 获取注解所传的参数
//
//            // 获取该类节点的包名+类名(getSimpleName只有类名没有包名)
//            String activityName = typeElement.getQualifiedName().toString();
//            map.put(key, activityName + ".class");

//            RouterBean routerBean = new RouterBean().Builder()
//                    .addGroup()
//                    .addPath()
//                    .addElement()
//                    .build();
//            // 必须是继承自Activity
//            TypeMirror elementMirror = element.asType();
//            if (mTypeTool.isSameType(elementMirror, activityMirror)) {
//                routerBean.setTypeEnum(RouterBean.TypeEnum.ACTIVITY);
//            } else {
//                throw new RuntimeException("@BindPath注解仅用于Activity之上");
//            }
//
//            if (checkRouterPath(routerBean)) {
//                mMessager.printMessage(Diagnostic.Kind.NOTE, "Router check success: "
//                        + routerBean.toString());
//
//                List<RouterBean> routerBeans = mAllPathMap.get(routerBean.getGroup());
//                if (ProcessorUtils.isEmpty(routerBeans)) {  // 仓库1没东西
//                    routerBeans = new ArrayList<>();
//                    routerBeans.add(routerBean);
//                    mAllPathMap.put(routerBean.getGroup(), routerBeans);
//                } else {
//                    routerBeans.add(routerBean);
//                }
//            } else {
//                mMessager.printMessage(Diagnostic.Kind.ERROR, "未按规定配置路径，如/app/MainActivity: "
//                        + routerBean.toString());
//            }
        }

        // 开始生成文件和代码
        if (map.size() > 0) {
            Writer writer = null;
            // 创建生成文件的类名
            String activityName = "ActivityRouter" + System.currentTimeMillis();
            try {
                // 创建文件并返回java文件对象
                JavaFileObject sourceFile = mFiler.createSourceFile("com.dengjian.router." + activityName);
                writer = sourceFile.openWriter();
                writer.write("package com.dengjian.router;\n");
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

        return true;   // false不干活了， true活干完了
    }
}
