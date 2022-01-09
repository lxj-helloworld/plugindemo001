package files.files;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntheticElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.ui.IdeUICustomization;

public class AddMethodAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        //得到编辑区对象
        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        if(editor == null){
            return;
        }
        //获取用户选择的字符
        SelectionModel model = editor.getSelectionModel();
        String selectedText = model.getSelectedText();
        System.out.println("选中的文本 " + selectedText);


        //获取光标位置
        Document document = editor.getDocument();
        //获取光标对象
        CaretModel caretModel = editor.getCaretModel();
        //得到光标的位置 ,该编辑区左上角到当前光标位置的字符数量
        int catetOffset = caretModel.getOffset();
        //得到一行开始和结束的位置
        int lineNum = document.getLineNumber(catetOffset);
        int lineStartOffset = document.getLineStartOffset(lineNum);
        int lineEndOffset = document.getLineEndOffset(lineNum);
        //


        //得到一个PsiFlle
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor,project);
        PsiClass psiClass = getTargetClass(editor,psiFile);




    }



    private void generateFields(){

    }


    /**
     * 根据当前文件获取对应的psiClass文件
     * @param editor
     * @param file
     * @return
     */
    public static PsiClass getTargetClass(Editor editor, PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return null;
        } else {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);
            return target instanceof SyntheticElement ? null : target;
        }
    }

}
