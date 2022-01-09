package files;

import com.intellij.ide.IdeView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.apache.commons.collections.map.HashedMap;
import org.jetbrains.jps.builders.logging.ProjectBuilderLogger;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 插件生成类demo
 */
public class PluginDemoActionABC extends AnAction {

    private String pasteStr = "name String\n" +
            "age int\n" + "id int\n";

    /**
     * 点击时会执行该方法
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        generateFile(e,"NameClass"+System.currentTimeMillis(),pasteStr);
    }

    /**
     * 生成文件
     */
    private void generateFile(AnActionEvent anActionEvent,String fileName,String pasteStr){
        //得到当前工程对象
        Project project = anActionEvent.getProject();
        System.out.println("project.getBasePath() = " + project.getBasePath());
        System.out.println(project.getProjectFilePath());
        //得到目录服务
        JavaDirectoryService directoryService = JavaDirectoryService.getInstance();
        //得到当前路径 - 得到相对路径
        IdeView ideView = anActionEvent.getRequiredData(LangDataKeys.IDE_VIEW);
        PsiDirectory psiDirectory = ideView.getOrChooseDirectory();
        System.out.println(psiDirectory.toString());

        //填入模板文件中的参数
        Map<String,String> map = new HashMap();
        map.put("NAME",fileName);
        map.put("INTERFACES","implements Serializable");
        map.put("PACKAGE_ NAME","");

        //生成文件
        PsiClass psiClass = directoryService.createClass(psiDirectory,fileName,"GeneFile",false,map);
        //开始加入字段
        WriteCommandAction.runWriteCommandAction(project, new Runnable() {
            @Override
            public void run() {
                geneModelFields(pasteStr,project,psiClass);
            }
        });
    }

    /**
     * 生成类中的属性
     * @param pasteStr
     * @param project
     * @param psiClass
     */
    private void geneModelFields(String pasteStr,Project project,PsiClass psiClass){
        if(pasteStr == null){
            return;
        }
        PsiElementFactory factory = JavaPsiFacade.getInstance(project).getElementFactory();
        ///根据字符串生成代码
        String[] lineString = pasteStr.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        for(String s : lineString){
            String[] temp = s.split(" ");
            String fieldName = temp[0];
            String fieldType = temp[1];
            stringBuilder.append("public" + " " + fieldType + " " + fieldName + ";");
            PsiField psiField = factory.createFieldFromText(stringBuilder.toString(),psiClass);
            psiClass.add(psiField);
            stringBuilder.delete(0,stringBuilder.length());
        }
    }
}
