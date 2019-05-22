import com.squareup.javapoet.*;
import com.sun.net.httpserver.Headers;
import sun.plugin2.message.Conversation;
import sun.plugin2.message.Message;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.LogRecord;

public class CodeGenertor {
    public static void main(String[] args) {
        try {
            new CodeGenertor().generatorHelloWorld();
            new CodeGenertor().generatorInterface();
            new CodeGenertor().generatorEnum();
            new CodeGenertor().generationAnonymousInnerClass();
            new CodeGenertor().generatorCodeBlock();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatorHelloWorld() throws IOException {

        FieldSpec fieldSpec = FieldSpec.builder(String.class,"name")
                .initializer("$S","aaa")
                .build();

        ParameterSpec parameterSpec = ParameterSpec.builder(String.class, "str").build();
        MethodSpec methodSpec = MethodSpec.methodBuilder("sayHello")
                .addParameter(parameterSpec)
                .returns(TypeName.VOID)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T.out.println($S)", System.class, "Hello World!")
                .build();

        MethodSpec methodSpec1 = MethodSpec.methodBuilder("add")
                .addParameter(int.class, "a")
                .addParameter(int.class, "b")
                .returns(int.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return a+b")
                .build();
        MethodSpec methodSpec2 = MethodSpec.methodBuilder("test")
                .addModifiers(Modifier.PUBLIC)
                .addCode(""
                        + "int total = 0;\n"
                        + "for (int i = 0; i < 10; i++) {\n"
                        + "  total += i;\n"
                        + "}\n")
                .build();

        MethodSpec methodSpec3 = MethodSpec.methodBuilder("test2")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("int total = 0")
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement(" total += i")
                .endControlFlow()
                .build();

        MethodSpec dateMethod = MethodSpec.methodBuilder("getDate")
                .returns(Date.class)
                .addStatement("return $T()", Date.class)
                .build();

        MethodSpec methodSpec4 = computeRange("test2",10,20, "*");

        ClassName hoverboard = ClassName.get("com.mattel", "Hoverboard");

        MethodSpec today = MethodSpec.methodBuilder("tomorrow")
                .returns(hoverboard)
                .addStatement("return new $T()", hoverboard)
                .build();

        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listOfHoverboards = ParameterizedTypeName.get(list, hoverboard);

        MethodSpec beyond = MethodSpec.methodBuilder("beyond")
                .returns(listOfHoverboards)
                .addStatement("$T result = new $T<>()", listOfHoverboards, arrayList)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("result.add(new $T())", hoverboard)
                .addStatement("return result")
                .build();

        MethodSpec hexDigit = MethodSpec.methodBuilder("hexDigit")
                .addParameter(int.class, "i")
                .returns(char.class)
                .addStatement("return (char) (i < 10 ? i + '0' : i - 10 + 'a')")
                .build();

        MethodSpec byteToHex = MethodSpec.methodBuilder("byteToHex")
                .addParameter(int.class, "b")
                .returns(String.class)
                .addStatement("char[] result = new char[2]")
                .addStatement("result[0] = $N((b >>> 4) & 0xf)", hexDigit)
                .addStatement("result[1] = $N(b & 0xf)", hexDigit)
                .addStatement("return new String(result)")
                .build();

        //constructor
        MethodSpec flux = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "greeting")
                .addStatement("this.$N = $N", "greeting", "greeting")
                .build();


        TypeSpec typeSpec = TypeSpec.classBuilder("HelloWorld")
                .addMethod(methodSpec)
                .addMethod(methodSpec1)
                .addMethod(methodSpec2)
                .addMethod(methodSpec3)
                .addMethod(dateMethod)
                .addMethod(methodSpec4)
                .addMethod(today)
                .addMethod(beyond)
                .addMethod(hexDigit)
                .addMethod(byteToHex)
                .addField(String.class, "greeting", Modifier.PRIVATE, Modifier.FINAL)
                .addMethod(flux)
                .addModifiers(Modifier.PUBLIC)
                .addField(fieldSpec)
                .build();
        //import static
        JavaFile javaFile = JavaFile.builder("com.xsy.test", typeSpec)
                .addStaticImport(hoverboard, "createNimbus")
                .addStaticImport(Collections.class, "*")
                .build();
        javaFile.writeTo(System.out);

        File file = new File("HelloWorld");
        javaFile.writeTo(file);

    }


    public void generatorInterface() throws IOException {
        TypeSpec helloWorld = TypeSpec.interfaceBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(String.class, "ONLY_THING_THAT_IS_CONSTANT")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                        .initializer("$S", "change")
                        .build())
                .addMethod(MethodSpec.methodBuilder("beep")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build())
                .build();

        JavaFile javaFile = JavaFile.builder("com.xsy.test", helloWorld)
                .build();
        javaFile.writeTo(System.out);
    }


    public void generatorEnum() throws IOException {
        TypeSpec helloWorld = TypeSpec.enumBuilder("Roshambo")
                .addModifiers(Modifier.PUBLIC)
                .addEnumConstant("ROCK")
                .addEnumConstant("SCISSORS")
                .addEnumConstant("PAPER")
                .build();
        JavaFile javaFile = JavaFile.builder("com.xsy.test", helloWorld)
                .build();
        javaFile.writeTo(System.out);
    }


    public void generationAnonymousInnerClass() throws IOException {
        TypeSpec comparator = TypeSpec.anonymousClassBuilder("")
                .addSuperinterface(ParameterizedTypeName.get(Comparator.class, String.class))
                .addMethod(MethodSpec.methodBuilder("compare")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(String.class, "a")
                        .addParameter(String.class, "b")
                        .returns(int.class)
                        .addStatement("return $N.length() - $N.length()", "a", "b")
                        .build())
                .build();

        MethodSpec toString = MethodSpec.methodBuilder("toString")
                .addAnnotation(Override.class)
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return $S", "Hoverboard")
                .build();

        MethodSpec logRecord = MethodSpec.methodBuilder("recordEvent")
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(Headers.class)
                        .addMember("accept", "$S", "application/json; charset=utf-8")
                        .addMember("userAgent", "$S", "Square Cash")
                        .build())
                .addParameter(LogRecord.class, "logRecord")
                .returns(LogRecord.class)
                .build();

        MethodSpec dismiss = MethodSpec.methodBuilder("dismiss")
                .addJavadoc("Hides {@code message} from the caller's history. Other\n"
                        + "participants in the conversation will continue to see the\n"
                        + "message in their own history unless they also delete it.\n")
                .addJavadoc("\n")
                .addJavadoc("<p>Use {@link #delete($T)} to delete the entire\n"
                        + "conversation for all participants.\n", Conversation.class)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addParameter(Message.class, "message")
                .build();

        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addMethod(toString)
                .addMethod(logRecord)
                .addMethod(dismiss)
                .addMethod(MethodSpec.methodBuilder("sortByLength")
                        .addParameter(ParameterizedTypeName.get(List.class, String.class), "strings")
                        .addStatement("$T.sort($N, $L)", Collections.class, "strings", comparator)
                        .build())
                .build();
        JavaFile javaFile = JavaFile.builder("com.xsy.test", helloWorld)
                .build();
        javaFile.writeTo(System.out);
    }


    public void generatorCodeBlock() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "tea");

        CodeBlock codeBlock = CodeBlock.builder()

                .add("$T a = $L", String.class, "A") // 设置代码，此方法并不添加分号和换行。
                .add(";")
                .add("\n")

                .addNamed("\"id: $id:L, name: $name:L\"", map) // 设置代码块，通过命名参数方式格式化字符串，此方法并不添加分号和换行。
                .add("\n")

                .addStatement("int result = 1") // for
                .beginControlFlow("for (int i = 0; i < 10; i++)")
                .addStatement("result = result +  i")
                .endControlFlow()

                .addStatement("long now = $T.currentTimeMillis()",
                        System.class) // if - else if - ... - else
                .beginControlFlow("if ($T.currentTimeMillis() < now)", System.class)
                .addStatement("$T.out.println($S)", System.class, "Time travelling, woo hoo!")
                .nextControlFlow("else if ($T.currentTimeMillis() == now)", System.class)
                .addStatement("$T.out.println($S)", System.class, "Time stood still!")
                .nextControlFlow("else")
                .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
                .endControlFlow()
                .addStatement("$T.out.println($S)", System.class, "Ok, time still moving forward")
                .addStatement("$T i = $L", int.class, 0)

                .addStatement("int id = 10") // while
                .beginControlFlow("while( id > 0)")
                .addStatement("$T.out.println(\"id: \" + $L)", System.class, "id")
                .addStatement("id--")
                .endControlFlow()

                .beginControlFlow("do") // do while
                .addStatement("i++")
                .endControlFlow("while(i < 5)")

                .beginControlFlow("try") // try - catch
                .addStatement("throw new Exception($S)", "Failed")
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow()
                .build();
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .addInitializerBlock(codeBlock)
                .build();
        JavaFile javaFile = JavaFile.builder("com.xsy.test", helloWorld)
                .build();
        javaFile.writeTo(System.out);
    }

    private MethodSpec computeRange(String name, int from, int to, String op) {
        return MethodSpec.methodBuilder(name)
                .returns(int.class)
                .addStatement("int result = 0")
                .beginControlFlow("for (int i = $L; i < $L; i++)", from, to)
                .addStatement("result = result $L i", op)
                .endControlFlow()
                .addStatement("return result")
                .build();
    }


}
